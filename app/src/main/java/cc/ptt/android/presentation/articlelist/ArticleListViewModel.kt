package cc.ptt.android.presentation.articlelist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cc.ptt.android.data.model.remote.board.article.Article
import cc.ptt.android.data.repository.board.BoardRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

class ArticleListViewModel constructor(
    private val boardRepository: BoardRepository
) : ViewModel() {
    val data: MutableList<Article> = ArrayList()
    private val page = AtomicInteger(1)
    private var nextIndex: String = ""

    private val loadingState = MutableLiveData<Boolean>()
    private val errorLiveData = MutableLiveData<Throwable>()

    fun getLoadingStateLiveData(): LiveData<Boolean> = loadingState

    fun getErrorLiveData(): LiveData<Throwable> = errorLiveData

    fun loadData(boardId: String, boardName: String) {
        if (loadingState.value == true) {
            return
        }
        data.clear()
        fetchData(boardId, "")
    }

    fun loadNextData(boardId: String, boardName: String) {
        if (loadingState.value == true) {
            return
        }
        fetchData(boardId, nextIndex)
    }

    private fun fetchData(boardId: String, nextIndex: String) {
        viewModelScope.launch {
            loadingState.value = true
            boardRepository.getBoardArticles(boardId = boardId, startIndex = nextIndex).catch { e ->
                errorLiveData.postValue(e)
                loadingState.value = false
            }.collect {
                data.addAll(it.list)
                this@ArticleListViewModel.nextIndex = it.nextIndex
                loadingState.value = false
            }
        }
    }
}
