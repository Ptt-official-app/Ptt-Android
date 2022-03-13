package cc.ptt.android.presentation.articlelist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cc.ptt.android.common.utils.Log
import cc.ptt.android.data.model.remote.board.article.Article
import cc.ptt.android.data.model.remote.board.article.ArticleList
import cc.ptt.android.data.source.remote.article.IArticleRemoteDataSource
import cc.ptt.android.data.source.remote.board.IBoardRemoteDataSource
import cc.ptt.android.di.IODispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

@HiltViewModel
class ArticleListViewModel @Inject constructor(
    private val articleRemoteDataSource: IArticleRemoteDataSource,
    private val boardRemoteDataSource: IBoardRemoteDataSource,
    @IODispatchers private val ioDispatcher: CoroutineDispatcher
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
        articleRemoteDataSource.disposeAll()
    }
}
