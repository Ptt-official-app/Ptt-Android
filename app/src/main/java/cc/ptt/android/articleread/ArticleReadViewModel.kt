package cc.ptt.android.articleread

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cc.ptt.android.common.date.DateFormatUtils
import cc.ptt.android.common.date.DatePatternConstants
import cc.ptt.android.common.logger.PttLogger
import cc.ptt.android.common.network.api.ApiException
import cc.ptt.android.data.model.remote.board.article.Article
import cc.ptt.android.data.model.remote.serverMsg
import cc.ptt.android.data.repository.article.ArticleRepository
import cc.ptt.android.data.repository.login.LoginRepository
import cc.ptt.android.domain.model.ui.article.ArticleReadInfo
import cc.ptt.android.domain.model.ui.article.PostRankMark
import cc.ptt.android.domain.usecase.article.CreateArticleCommentUseCase
import cc.ptt.android.domain.usecase.article.GetArticleUseCase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class ArticleReadViewModel constructor(
    private val articleRepository: ArticleRepository,
    private val getArticleUseCase: GetArticleUseCase,
    private val createArticleCommentUseCase: CreateArticleCommentUseCase,
    private val loginRepository: LoginRepository,
    private val logger: PttLogger,
) : ViewModel() {

    val data: MutableList<ArticleReadInfo> = mutableListOf()

    private var headerArticleReadInfo: ArticleReadInfo? = null

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

        headerArticleReadInfo = ArticleReadInfo.HeaderInfo(
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
        headerArticleReadInfo?.let { data.add(it) }
    }

    fun loadData(article: Article) {
        if (_loadingState.value == true) return
        viewModelScope.launch {
            _loadingState.value = true
            getArticleUseCase.getArticleDetail(
                article.boardId,
                article.articleId
            ).combine(getArticleUseCase.getArticleComments(article.boardId, article.articleId)) { detail, comments ->
                detail.copy(contentList = detail.contentList + comments)
            }.catch { e ->
                logger.e(TAG, "Load data error: ${Log.getStackTraceString(e)}")
                _loadingState.value = false
                if (e is ApiException) {
                    _errorMessage.value = e.serverMsg.msg
                } else {
                    _errorMessage.value = "Error : ${e.message}"
                }
            }.collect {
                data.clear()
                data.addAll(it.contentList)
                _likeNumber.postValue(it.rank.toString())
                _loadingState.value = false
            }
        }
    }

    fun setRank(
        article: Article,
        rankMark: PostRankMark
    ) = viewModelScope.launch {
        _loadingState.value = true
        articleRepository.postArticleRank(rankMark.value, article.boardId, article.articleId).flatMapMerge {
            articleRepository.getArticleDetail(article.boardId, article.articleId)
        }.catch { e ->
            logger.d(TAG, "set rank error: $e")
            _progressDialogState.value = false
            _errorMessage.value = "Error : ${e.message}"
        }.collect { detail ->
            for (i in data.indices) {
                val item = data[i]
                if (item !is ArticleReadInfo.CenterBarInfo) continue
                data[i] =
                    ArticleReadInfo.CenterBarInfo(detail.recommend.toString(), item.floor)
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
                    _errorMessage.value = e.message
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
