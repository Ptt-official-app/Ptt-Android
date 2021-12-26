package cc.ptt.android.presentation.hotboard

import androidx.lifecycle.*
import cc.ptt.android.data.model.remote.board.hotboard.HotBoardsItem
import cc.ptt.android.data.source.remote.board.IBoardRemoteDataSource
import kotlinx.coroutines.*

class HotBoardsViewModel(
    private val boardRemoteDataSource: IBoardRemoteDataSource,
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
            val popularBoards = boardRemoteDataSource.getPopularBoards()
            val boardData = popularBoards.list.map {
                HotBoardsItem(
                    it.boardId,
                    it.boardName,
                    it.title,
                    it.onlineUser.toString(),
                    "7"
                )
            }
            data.addAll(boardData)
        } catch (e: Exception) {
            _errorMessage.postValue("Error: $e")
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}
