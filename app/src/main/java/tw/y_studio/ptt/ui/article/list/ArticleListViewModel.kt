package tw.y_studio.ptt.ui.article.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tw.y_studio.ptt.api.model.board.article.Article
import tw.y_studio.ptt.api.model.board.article.ArticleList
import tw.y_studio.ptt.source.remote.board.IBoardRemoteDataSource
import tw.y_studio.ptt.source.remote.post.IPostRemoteDataSource
import tw.y_studio.ptt.utils.Log
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

class ArticleListViewModel(
    private val postRemoteDataSource: IPostRemoteDataSource,
    private val boardRemoteDataSource: IBoardRemoteDataSource,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    val data: MutableList<Article> = ArrayList()
    private val page = AtomicInteger(1)

    private val loadingState = MutableLiveData<Boolean>()
    private val errorLiveData = MutableLiveData<Throwable>()

    fun getLoadingStateLiveData(): LiveData<Boolean> = loadingState

    fun getErrorLiveData(): LiveData<Throwable> = errorLiveData

    fun loadData(boardId: String, boardName: String) {
        viewModelScope.launch {
            if (loadingState.value == true) {
                return@launch
            }
            data.clear()
            page.set(1)
            loadingState.value = true
            getDataFromApi(boardId, boardName)
            loadingState.value = false
        }
    }

    fun loadNextData(boardId: String, boardName: String) {
        viewModelScope.launch {
            if (loadingState.value == true) {
                return@launch
            }
            loadingState.value = true
            getDataFromApi(boardId, boardName)
            loadingState.value = false
        }
    }

    private suspend fun getDataFromApi(boardId: String, boardName: String) = withContext(ioDispatcher) {
        try {
            val temp = mutableListOf<Article>()
            for (i in 0..2) {
                try {
                    // TODO: 2021/2/18 getArticleList()
                    val result: ArticleList = boardRemoteDataSource.getBoardArticles(boardId = boardId)
                    temp.addAll(result.list)
                } catch (e: Exception) {
                    if (page.get() > 1) {
                        throw e
                    }
                }
                page.incrementAndGet()
            }
            Log("ArticleListFragment", "get data from web success")
            data.addAll(temp)
        } catch (t: Throwable) {
            errorLiveData.postValue(t)
        }
    }

    override fun onCleared() {
        super.onCleared()
        postRemoteDataSource.disposeAll()
    }
}
