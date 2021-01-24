package tw.y_studio.ptt.ui.hot_board

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.*
import tw.y_studio.ptt.model.HotBoardsItem
import tw.y_studio.ptt.source.remote.popular.IPopularRemoteDataSource

class HotBoardsViewModel(
    private val popularRemoteDataSource: IPopularRemoteDataSource,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    val data: MutableList<HotBoardsItem> = mutableListOf()

    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> = _loadingState

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun loadData() {
        viewModelScope.launch {
            if (_loadingState.value == true) return@launch
            data.clear()
            _loadingState.value = true
            getDataFromApi()
            _loadingState.value = false
        }
    }

    private suspend fun getDataFromApi() = withContext(ioDispatcher) {
        try {
            val boardData = popularRemoteDataSource.getPopularBoardData(1, 128)
                .map {
                    HotBoardsItem(
                        it.title,
                        it.subtitle,
                        it.online.toString(),
                        it.onlineColor
                    )
                }

            data.addAll(boardData)
            if (data.isNotEmpty()) Log.d("getDataFromApi", data[0].toString())
        } catch (e: Exception) {
            _errorMessage.value = "Error: $e"
        }
    }

    override fun onCleared() {
        super.onCleared()
        popularRemoteDataSource.disposeAll()
    }
}
