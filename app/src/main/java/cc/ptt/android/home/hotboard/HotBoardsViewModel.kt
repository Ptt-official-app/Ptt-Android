package cc.ptt.android.home.hotboard

import androidx.lifecycle.*
import cc.ptt.android.data.model.remote.board.hotboard.HotBoardsItem
import cc.ptt.android.data.repository.board.BoardRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch

class HotBoardsViewModel constructor(
    private val boardRepository: BoardRepository
) : ViewModel() {
    val data: MutableList<HotBoardsItem> = mutableListOf()

    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> = _loadingState

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun loadData() {
        if (_loadingState.value == true) return
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch {
            _loadingState.value = true
            boardRepository.getPopularBoards().catch { e ->
                _errorMessage.postValue("Error: $e")
                _loadingState.value = false
            }.collect { popularBoards ->
                val boardData = popularBoards.list.map {
                    HotBoardsItem(
                        it.boardId,
                        it.boardName,
                        it.title,
                        it.onlineUser.toString(),
                        "7"
                    )
                }
                data.clear()
                data.addAll(boardData)
                _loadingState.value = false
            }
        }
    }
}
