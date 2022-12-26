package cc.ptt.android.articleread

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cc.ptt.android.common.StringUtils
import cc.ptt.android.common.date.DateFormatUtils
import cc.ptt.android.common.date.DatePatternConstants
import cc.ptt.android.common.logger.PttLogger
import cc.ptt.android.data.model.remote.board.article.Article
import cc.ptt.android.data.repository.article.ArticleRepository
import cc.ptt.android.data.repository.login.LoginRepository
import cc.ptt.android.domain.model.ui.article.PostRankMark
import cc.ptt.android.domain.usecase.articlecomment.CreateArticleCommentUseCase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class ArticleReadViewModel constructor(
    private val articleRepository: ArticleRepository,
    private val createArticleCommentUseCase: CreateArticleCommentUseCase,
    private val loginRepository: LoginRepository,
    private val logger: PttLogger,
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
        data class CreateCommentSuccess(val comment: cc.ptt.android.data.model.remote.article.ArticleComment) : ActionEvent()
    }

    private fun emitActionState(action: ActionEvent) = viewModelScope.launch {
        _actionState.emit(action)
    }

    fun isLogin(): Boolean = loginRepository.isLogin().apply {
        logger.d(TAG, "isLogin: ${loginRepository.getUserInfo()}")
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

    private fun dataFromApi(
        article: Article
    ) = viewModelScope.launch {
        _loadingState.value = true
        articleRepository.getArticleDetail(
            article.boardId,
            article.articleId
        ).combine(articleRepository.getArticleComments(article.boardId, article.articleId)) { detail, comments ->
            val items = mutableListOf<ArticleReadAdapter.Item>()
            val headerItem = ArticleReadAdapter.Item.HeaderItem(
                detail.title,
                detail.owner,
                DateFormatUtils.secondsToDateTime(
                    detail.createTime.toLong(),
                    DatePatternConstants.articleDateTime
                ),
                detail.classX,
                detail.boardName
            )
            items.add(headerItem)

            val contentBuilder = StringBuilder()
            detail.content.forEach listContent@{ listContent ->
                if (listContent.isEmpty()) {
                    contentBuilder.appendLine()
                    items.add(ArticleReadAdapter.Item.ContentLineItem(contentBuilder.toString()))
                    contentBuilder.clear()
                    return@listContent
                }
                listContent.forEach { content ->
                    val urlM = StringUtils.UrlPattern.matcher(content.text)
                    if (urlM.find()) {
                        items.add(ArticleReadAdapter.Item.ContentLineItem(contentBuilder.toString()))
                        contentBuilder.clear()
                        items.add(ArticleReadAdapter.Item.ContentLineItem(content.text))
                        val imageUrl = StringUtils.getImgUrl(content.text)
                        for (urlString in imageUrl) {
                            items.add(ArticleReadAdapter.Item.ImageItem(-2, urlString))
                        }
                    } else {
                        contentBuilder.append(content.text)
                    }
                }
                items.add(ArticleReadAdapter.Item.ContentLineItem(contentBuilder.toString()))
                contentBuilder.clear()
            }

            items.add(
                ArticleReadAdapter.Item.CenterBarItem(
                    detail.recommend.toString(),
                    detail.nComments.toString()
                )
            )

            for ((index, articleComment) in comments.list.withIndex()) {
                articleComment.content.forEach { listContent ->
                    listContent.forEach { content ->
                        items.add(
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
                            items.add(ArticleReadAdapter.Item.ImageItem(index, urlString))
                        }
                        items.add(
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

            listOf(items, detail.rank.toString())
        }.catch { e ->
            _loadingState.value = false
            _errorMessage.value = "Error : $e"
        }.collect {
            // _likeNumber.postValue(articleDetail.rank.toString())
            data.clear()
            val items = it.firstOrNull() as? MutableList<ArticleReadAdapter.Item> ?: return@collect
            val rank = it.lastOrNull() as? String ?: return@collect
            data.addAll(items)
            _likeNumber.postValue(rank)
            _loadingState.value = false
        }
    }

    fun loadData(article: Article) {
        if (_loadingState.value == true) return
        dataFromApi(article)
    }

    fun setRank(
        article: Article,
        rankMark: PostRankMark
    ) = viewModelScope.launch {
        _loadingState.value = true
        articleRepository.postArticleRank(rankMark.value, article.boardId, article.articleId).flatMapMerge {
            articleRepository.getArticleDetail(article.boardId, article.articleId)
        }.catch { e ->
            _progressDialogState.value = false
            _errorMessage.value = "Error : $e"
        }.collect { detail ->
            for (i in data.indices) {
                val item = data[i]
                if (item !is ArticleReadAdapter.Item.CenterBarItem) continue
                data[i] =
                    ArticleReadAdapter.Item.CenterBarItem(detail.recommend.toString(), item.floor)
                break
            }
            val rank = detail.rank
            _likeNumber.value = rank.toString()
            _loadingState.value = false
            _progressDialogState.value = false
        }
    }

    fun createComment(article: Article, text: String?, type: cc.ptt.android.data.model.remote.article.ArticleCommentType?) {
        if (text.isNullOrEmpty()) {
            return
        }

        type?.let {
            _progressDialogState.value = true
            viewModelScope.launch {
                createArticleCommentUseCase.createArticleComment(article.boardId, article.articleId, it, text).catch { e ->
                    _errorMessage.value = e.localizedMessage
                    _progressDialogState.value = false
                }.collect {
                    _progressDialogState.value = false
                    emitActionState(ActionEvent.CreateCommentSuccess(it))
                }
            }
        } ?: run {
            emitActionState(ActionEvent.ChooseCommentType)
        }
    }

    companion object {
        private val TAG = ArticleReadViewModel::class.java.simpleName
    }
}
