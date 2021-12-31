package cc.ptt.android.presentation.articleread

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cc.ptt.android.common.date.DateFormatUtils
import cc.ptt.android.common.date.DatePatternConstants
import cc.ptt.android.common.utils.Log
import cc.ptt.android.data.api.PostRankMark
import cc.ptt.android.data.common.StringUtils
import cc.ptt.android.data.model.remote.article.ArticleComment
import cc.ptt.android.data.model.remote.article.ArticleCommentType
import cc.ptt.android.data.model.remote.article.ArticleDetail
import cc.ptt.android.data.model.remote.board.article.Article
import cc.ptt.android.data.repository.login.LoginRepository
import cc.ptt.android.data.source.remote.article.IArticleRemoteDataSource
import cc.ptt.android.domain.usecase.articlecomment.CreateArticleCommentUseCase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class ArticleReadViewModel(
    private val articleRemoteDataSource: IArticleRemoteDataSource,
    private val preferences: SharedPreferences,
    private val ioDispatcher: CoroutineDispatcher,
    private val createArticleCommentUseCase: CreateArticleCommentUseCase,
    private val loginRepository: LoginRepository
) : ViewModel() {

    val data: MutableList<ArticleReadAdapter.Item> = mutableListOf()

    private var headerItem: ArticleReadAdapter.Item? = null

    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> = _loadingState

    private val _progressDialogState = MutableLiveData<Boolean>()
    val progressDialogState: LiveData<Boolean> = _progressDialogState

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _likeNumber = MutableLiveData<String>()
    val likeNumber: LiveData<String> = _likeNumber

    private val _actionState = MutableSharedFlow<ActionEvent>()
    val actionState = _actionState.asSharedFlow()

    sealed class ActionEvent {
        object ChooseCommentType : ActionEvent()
        data class CreateCommentSuccess(val comment: ArticleComment) : ActionEvent()
    }

    private fun emitActionState(action: ActionEvent) = viewModelScope.launch {
        _actionState.emit(action)
    }

    fun isLogin(): Boolean = loginRepository.isLogin().apply {
        Log(ArticleReadViewModel.TAG, "isLogin: ${loginRepository.getUserInfo()}")
    }

    fun originalTitle(classX: String, title: String) = if (classX.isBlank()) {
        title
    } else {
        "[$classX] $title"
    }

    fun createDefaultHeader(
        articleTitle: String,
        articleAuth: String,
        articleTime: Int,
        articleClass: String,
        articleBoard: String
    ) {

        headerItem = ArticleReadAdapter.Item.HeaderItem(
            articleTitle,
            articleAuth,
            DateFormatUtils.secondsToDateTime(
                articleTime.toLong(),
                DatePatternConstants.articleDateTime
            ),
            articleClass,
            articleBoard
        )
    }

    fun putDefaultHeader() {
        headerItem?.let { data.add(it) }
    }

    private suspend fun dataFromApi(
        article: Article
    ) = withContext(ioDispatcher) {
        Log("onAR", "get data from web start")
        val asyncDetail: Deferred<ArticleDetail> = async {
            articleRemoteDataSource.getArticleDetail(article.boardId, article.articleId)
        }
        val asyncComments = async {
            articleRemoteDataSource.getArticleComments(article.boardId, article.articleId)
        }
        val articleDetail = asyncDetail.await()
        val articleComments = asyncComments.await()

        data.add(
            ArticleReadAdapter.Item.HeaderItem(
                articleDetail.title,
                articleDetail.owner,
                DateFormatUtils.secondsToDateTime(
                    articleDetail.createTime.toLong(),
                    DatePatternConstants.articleDateTime
                ),
                articleDetail.classX,
                articleDetail.boardName
            )
        )
        val contentBuilder = StringBuilder()
        articleDetail.content.forEach listContent@{ listContent ->
            if (listContent.isEmpty()) {
                contentBuilder.appendLine()
                data.add(ArticleReadAdapter.Item.ContentLineItem(contentBuilder.toString()))
                contentBuilder.clear()
                return@listContent
            }
            listContent.forEach { content ->
                val urlM = StringUtils.UrlPattern.matcher(content.text)
                if (urlM.find()) {
                    data.add(ArticleReadAdapter.Item.ContentLineItem(contentBuilder.toString()))
                    contentBuilder.clear()
                    data.add(ArticleReadAdapter.Item.ContentLineItem(content.text))
                    val imageUrl = StringUtils.getImgUrl(content.text)
                    for (urlString in imageUrl) {
                        data.add(ArticleReadAdapter.Item.ImageItem(-2, urlString))
                    }
                } else {
                    contentBuilder.append(content.text)
                }
            }
            data.add(ArticleReadAdapter.Item.ContentLineItem(contentBuilder.toString()))
            contentBuilder.clear()
        }

        data.add(
            ArticleReadAdapter.Item.CenterBarItem(
                articleDetail.recommend.toString(),
                articleDetail.nComments.toString()
            )
        )
        for ((index, articleComment) in articleComments.list.withIndex()) {
            articleComment.content.forEach { listContent ->
                listContent.forEach { content ->
                    data.add(
                        ArticleReadAdapter.Item.CommentItem(
                            index,
                            content.text,
                            articleComment.owner
                        )
                    )
                    val imageUrl: List<String> = StringUtils.getImgUrl(
                        StringUtils.notNullString(content.text)
                    )
                    for (urlString in imageUrl) {
                        data.add(ArticleReadAdapter.Item.ImageItem(index, urlString))
                    }
                    data.add(
                        ArticleReadAdapter.Item.CommentBarItem(
                            index,
                            DateFormatUtils.secondsToDateTime(
                                articleComment.createTime.toLong(),
                                DatePatternConstants.articleCommentDateTime
                            ),
                            "${index + 1}F",
                            "0"
                        )
                    )
                }
            }
        }

        _likeNumber.postValue(articleDetail.rank.toString())
    }

    fun loadData(
        article: Article
    ) = viewModelScope.launch {
        if (_loadingState.value == true) return@launch
        data.clear()
        _loadingState.value = true
        try {
            dataFromApi(article)
            Log("onAL", "get data from web over")
        } catch (e: Exception) {
            Log("onAL", "Error : $e")
            _errorMessage.value = "Error : $e"
        }

        _loadingState.value = false
    }

    fun setRank(
        article: Article,
        rank: PostRankMark
    ) = viewModelScope.launch {
        try {
            // TODO: 4/11/21 post rank api
            setArticleRank(article, rank)
            _loadingState.value = true
            val newRank = refreshRank(article.boardId, article.articleId)
            _likeNumber.value = newRank.toString()
            _loadingState.value = false
            _progressDialogState.value = false
        } catch (e: Exception) {
            _progressDialogState.value = false
            _errorMessage.value = "Error : $e"
        }
    }

    private suspend fun setArticleRank(
        article: Article,
        rankMark: PostRankMark
    ) = withContext(ioDispatcher) {
        try {
            _likeNumber.value.apply {
                articleRemoteDataSource.postArticleRank(
                    rank = rankMark.value,
                    boardId = article.boardId,
                    articleId = article.articleId
                )
            }
        } catch (e: Exception) {
            Log("onAL", "Error : $e")
            _errorMessage.value = "Error : $e"
        }
    }

    private suspend fun refreshRank(
        boardId: String,
        articleId: String
    ): Int = withContext(ioDispatcher) {
        val detail = articleRemoteDataSource.getArticleDetail(boardId, articleId)
        for (i in data.indices) {
            val item = data[i]
            if (item !is ArticleReadAdapter.Item.CenterBarItem) continue
            data[i] = ArticleReadAdapter.Item.CenterBarItem(detail.recommend.toString(), item.floor)
            break
        }
        Log("onAL", "get data from web over")
        return@withContext detail.rank
    }

    fun createComment(article: Article, text: String?, type: ArticleCommentType?) {
        if (text.isNullOrEmpty()) {
            return
        }

        type?.let {
            _progressDialogState.value = true
            viewModelScope.launch(ioDispatcher) {
                createArticleCommentUseCase(
                    CreateArticleCommentUseCase.Params(article.boardId, article.articleId, it, text)
                ).onSuccess {
                    withContext(Dispatchers.Main) {
                        _progressDialogState.value = false
                    }
                    emitActionState(ActionEvent.CreateCommentSuccess(it.comment))
                }.onFailure {
                    Log(TAG, "createComment error: $it")
                    withContext(Dispatchers.Main) {
                        _errorMessage.value = it.localizedMessage
                        _progressDialogState.value = false
                    }
                }
            }
        } ?: run {
            emitActionState(ActionEvent.ChooseCommentType)
        }
    }

    override fun onCleared() {
        articleRemoteDataSource.disposeAll()
        super.onCleared()
    }

    companion object {
        private val TAG = ArticleReadViewModel::class.java.simpleName
    }
}
