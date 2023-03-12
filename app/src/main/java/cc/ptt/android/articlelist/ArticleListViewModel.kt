package cc.ptt.android.articlelist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cc.ptt.android.common.logger.PttLogger
import cc.ptt.android.data.model.remote.board.article.Article
import cc.ptt.android.domain.usecase.board.BoardUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ArticleListViewModel constructor(
    private val boardUseCase: BoardUseCase,
    private val logger: PttLogger
) : ViewModel() {

    private val _actionState: MutableSharedFlow<ActionState> = MutableSharedFlow()
    val actionState: SharedFlow<ActionState> = _actionState.asSharedFlow()

    private val articleList: MutableList<Article> = mutableListOf()
    private val _data: MutableStateFlow<List<Article>> = MutableStateFlow(listOf())
    val data: StateFlow<List<Article>> get() = _data.asStateFlow()

    private var nextIndex: String = ""

    private val _loadingState = MutableLiveData<Boolean>()
    val loadingStateLiveData: LiveData<Boolean> get() = _loadingState

    fun loadData(boardId: String) {
        if (_loadingState.value == true) {
            return
        }
        articleList.clear()
        fetchData(boardId, "")
    }

    fun loadNextData(boardId: String) {
        if (_loadingState.value == true) {
            return
        }
        if (nextIndex.isEmpty()) {
            logger.d(TAG, "No next index")
            return
        }
        fetchData(boardId, nextIndex)
    }

    fun switchToArticleReadPage(article: Article) {
        emitAction(ActionState.SwitchToArticleReadPage(article))
    }

    fun switchToPostArticlePage() {
        // TODO: emitAction(ActionState.SwitchToPostArticlePage)
        showNotImplementMsg()
    }

    fun switchToArticleListSearchPage() {
        // TODO: emitAction(ActionState.SwitchToArticleListSearchPage)
        showNotImplementMsg()
    }

    fun switchToBoardInfoPage() {
        showNotImplementMsg()
    }

    private fun showNotImplementMsg() {
        emitAction(ActionState.ShowErrorMsg("Not implement yet"))
    }

    private fun fetchData(boardId: String, nextIndex: String) {
        viewModelScope.launch {
            _loadingState.value = true
            boardUseCase.getBoardArticles(boardId = boardId, startIndex = nextIndex).catch { e ->
                emitAction(ActionState.ShowErrorMsg(e.message.orEmpty()))
                _loadingState.value = false
            }.collect {
                articleList.addAll(it.list)
                this@ArticleListViewModel.nextIndex = it.nextIndex
                _loadingState.value = false
                _data.value = articleList.toList()
            }
        }
    }

    private fun emitAction(action: ActionState) = viewModelScope.launch {
        _actionState.emit(action)
    }

    sealed class ActionState {
        data class SwitchToArticleReadPage(val article: Article) : ActionState()
        data class ShowErrorMsg(val msg: String) : ActionState()
        object SwitchToArticleListSearchPage : ActionState()
        object SwitchToPostArticlePage : ActionState()
    }

    companion object {
        private val TAG = ArticleListViewModel::class.java.simpleName
    }
}
