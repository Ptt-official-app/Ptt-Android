package tw.y_studio.ptt.ui.article.list

import android.os.Handler
import android.os.HandlerThread
import android.os.Message
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tw.y_studio.ptt.model.PartialPost
import tw.y_studio.ptt.source.remote.post.IPostRemoteDataSource
import tw.y_studio.ptt.utils.Log
import java.util.*

class ArticleListViewModel(
    private val postRemoteDataSource: IPostRemoteDataSource,
    private val boardName: String // TODO: 2020/11/21 need refactor
) : ViewModel() {
    val data: MutableList<PartialPost> = ArrayList()

    private val loadingState = MutableLiveData<Boolean>()
    private val errorLiveData = MutableLiveData<Throwable>()

    private val backgroundHandlerThread: HandlerThread = HandlerThread(this::class.toString())
    private val handler: Handler

    init {
        backgroundHandlerThread.start()
        handler = object : Handler(backgroundHandlerThread.looper) {
            private var page = 1
            var loadingState: Boolean = false
                set(value) {
                    field = value
                    this@ArticleListViewModel.loadingState.postValue(value)
                }

            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                when (msg.what) {
                    LOAD_NEXT_DATA -> loadNextData()
                    LOAD_DATA -> loadData()
                }
            }

            private fun loadData() {
                if (loadingState) return
                data.clear()
                page = 1
                getDataFromApi()
            }

            private fun loadNextData() {
                if (loadingState) return
                getDataFromApi()
            }

            private fun getDataFromApi() {
                try {
                    loadingState = true
                    val temp = mutableListOf<PartialPost>()
                    for (i in 0..2) {
                        try {
                            temp.addAll(postRemoteDataSource.getPostList(boardName, page))
                        } catch (e: Exception) {
                            if (page > 1) {
                                throw e
                            }
                        }
                        page++
                    }
                    Log("ArticleListFragment", "get data from web success")
                    data.addAll(temp)
                    loadingState = false
                } catch (t: Throwable) {
                    loadingState = false
                    errorLiveData.postValue(t)
                }
                removeCallbacksAndMessages(null)
            }
        }
    }

    fun getLoadingStateLiveData(): LiveData<Boolean> = loadingState

    fun getErrorLiveData(): LiveData<Throwable> = errorLiveData

    fun loadData() {
        if (loadingState.value == true) {
            return
        }
        handler.sendEmptyMessage(LOAD_DATA)
    }

    fun loadNextData() {
        if (loadingState.value == true) {
            return
        }
        handler.sendEmptyMessage(LOAD_NEXT_DATA)
    }

    override fun onCleared() {
        super.onCleared()
        backgroundHandlerThread.quit()
        handler.removeCallbacksAndMessages(null)
        postRemoteDataSource.disposeAll()
    }

    companion object {
        const val LOAD_DATA = 0
        const val LOAD_NEXT_DATA = 1
    }
}
