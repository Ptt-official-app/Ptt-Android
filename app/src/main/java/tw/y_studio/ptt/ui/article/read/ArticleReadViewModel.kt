package tw.y_studio.ptt.ui.article.read

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import tw.y_studio.ptt.api.PostRankMark
import tw.y_studio.ptt.api.model.article.ArticleDetail
import tw.y_studio.ptt.api.model.board.article.Article
import tw.y_studio.ptt.ptt.AidConverter
import tw.y_studio.ptt.source.remote.article.IArticleRemoteDataSource
import tw.y_studio.ptt.utils.Log
import tw.y_studio.ptt.utils.PreferenceConstants
import tw.y_studio.ptt.utils.StringUtils
import tw.y_studio.ptt.utils.date.DateFormatUtils
import tw.y_studio.ptt.utils.date.DatePatternConstants
import java.util.regex.Pattern

class ArticleReadViewModel(
    private val articleRemoteDataSource: IArticleRemoteDataSource,
    private val preferences: SharedPreferences,
    private val ioDispatcher: CoroutineDispatcher
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
    }

    fun loadData(
        article: Article
    ) = viewModelScope.launch {
        if (_loadingState.value == true) return@launch
        data.clear()
        _loadingState.value = true
        try {
            dataFromApi(article)
            _likeNumber.value = article.recommend.toString()
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
            delay(3000)
//            loadRankData(board, aid, orgUrl, rank)
            _progressDialogState.value = false
        } catch (e: Exception) {
            _progressDialogState.value = false
            _errorMessage.value = "Error : $e"
        }
    }

    private suspend fun loadRankData(
        board: String,
        aid: String,
        orgUrl: String,
        rank: PostRankMark
    ) = withContext(ioDispatcher) {
        val id = preferences.getString(PreferenceConstants.id, "")
        if (id.isNullOrEmpty()) {
            throw Exception("No Ptt id")
        }
        val p = Pattern.compile(
            "www.ptt.cc/bbs/([-a-zA-Z0-9_]{2,})/([M|G].[-a-zA-Z0-9._]{1,30}).htm"
        )
        val m = p.matcher(orgUrl)
        if (m.find()) {
            val aidBean = AidConverter.urlToAid(orgUrl)
            articleRemoteDataSource.setPostRank(
                aidBean.boardTitle,
                aidBean.aid,
                id,
                rank
            )
            refreshRank(board, aid)
        } else {
            throw Exception("error")
        }
    }

    private suspend fun refreshRank(board: String, aid: String) = withContext(ioDispatcher) {
        _loadingState.value = true
        try {
            val postRank = articleRemoteDataSource.getPostRank(board, aid)
            for (i in data.indices) {
                val item = data[i]
                if (item !is ArticleReadAdapter.Item.CenterBarItem) continue
                data[i] = ArticleReadAdapter.Item.CenterBarItem(postRank.getLike().toString(), item.floor)
                break
            }
            _likeNumber.value = postRank.getLike().toString()
            Log("onAL", "get data from web over")
        } catch (e: Exception) {
            Log("onAL", "Error : $e")
            _errorMessage.value = "Error : $e"
        }
        _loadingState.value = false
    }

    override fun onCleared() {
        articleRemoteDataSource.disposeAll()
        super.onCleared()
    }
}
