package tw.y_studio.ptt.ui.article.read

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tw.y_studio.ptt.api.PostRankMark
import tw.y_studio.ptt.ptt.AidConverter
import tw.y_studio.ptt.source.remote.post.IPostRemoteDataSource
import tw.y_studio.ptt.utils.Log
import tw.y_studio.ptt.utils.PreferenceConstants
import tw.y_studio.ptt.utils.StringUtils
import tw.y_studio.ptt.utils.date.DateFormatUtils
import tw.y_studio.ptt.utils.date.DatePatternConstants
import java.util.regex.Pattern

class ArticleReadViewModel(
    private val postRemoteDataSource: IPostRemoteDataSource,
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

    private var floorNum = 0

    var originalArticleTitle = ""

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
        board: String,
        fileName: String,
        aid: String,
        articleBoard: String
    ) = withContext(ioDispatcher) {
        Log("onAR", "get data from web start")
        val post = postRemoteDataSource.getPost(board, fileName)
        val postRank = postRemoteDataSource.getPostRank(board, aid)
        floorNum = post.comments.size
        originalArticleTitle = post.title
        data.add(
            ArticleReadAdapter.Item.HeaderItem(
                post.title,
                "${post.auth} (${post.authNickName})",
                post.date,
                post.classString,
                articleBoard
            )
        )

        val contents = post.content.split("\r\n".toRegex()).toTypedArray()
        var contentTemp = StringBuilder()
        for (i in contents.indices) {
            val cmd = contents[i]
            val urlM = StringUtils.UrlPattern.matcher(cmd)
            if (urlM.find()) {
                data.add(ArticleReadAdapter.Item.ContentLineItem(contentTemp.toString()))
                contentTemp = StringBuilder()
                data.add(ArticleReadAdapter.Item.ContentLineItem(cmd))
                val imageUrl = StringUtils.getImgUrl(cmd)
                for (urlString in imageUrl) {
                    data.add(ArticleReadAdapter.Item.ImageItem(-2, urlString))
                }
            } else {
                contentTemp.append(cmd)
                if (i < contents.size - 1) {
                    contentTemp.append("\n")
                }
            }
        }
        if (contentTemp.toString().isNotEmpty()) {
            data.add(ArticleReadAdapter.Item.ContentLineItem(contentTemp.toString()))
        }
        data.add(ArticleReadAdapter.Item.CenterBarItem(postRank.getLike().toString(), floorNum.toString()))
        post.comments.forEachIndexed { index, comment ->
            data.add(ArticleReadAdapter.Item.CommentItem(index, comment.content, comment.userid))
            val imageUrl: List<String> = StringUtils.getImgUrl(StringUtils.notNullString(comment.content))
            for (urlString in imageUrl) {
                data.add(ArticleReadAdapter.Item.ImageItem(index, urlString))
            }
            data.add(
                ArticleReadAdapter.Item.CommentBarItem(
                    index,
                    comment.date,
                    "${index + 1}F",
                    "0"
                )
            )
        }
        postRank
    }

    fun loadData(
        board: String,
        fileName: String,
        aid: String,
        articleBoard: String
    ) = viewModelScope.launch {
        if (_loadingState.value == true) return@launch
        data.clear()
        _loadingState.value = true
        putDefaultHeader()
        try {
            val postRank = dataFromApi(board, fileName, aid, articleBoard)
            _likeNumber.value = postRank.getLike().toString()
            Log("onAL", "get data from web over")
        } catch (e: Exception) {
            Log("onAL", "Error : $e")
            _errorMessage.value = "Error : $e"
        }

        _loadingState.value = false
    }

    fun setRank(
        board: String,
        aid: String,
        orgUrl: String,
        rank: PostRankMark
    ) = viewModelScope.launch {
        try {
            loadRankData(board, aid, orgUrl, rank)
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
            postRemoteDataSource.setPostRank(
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
            val postRank = postRemoteDataSource.getPostRank(board, aid)
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
        postRemoteDataSource.disposeAll()
        super.onCleared()
    }
}
