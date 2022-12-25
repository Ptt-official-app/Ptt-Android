package cc.ptt.android.presentation.searchboards

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cc.ptt.android.data.model.remote.board.searchboard.SearchBoardsItem
import cc.ptt.android.data.repository.search.SearchBoardRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class SearchBoardsModel constructor(
    private val searchBoardRepository: SearchBoardRepository
) : ViewModel() {

    val data: MutableList<SearchBoardsItem> = mutableListOf()

    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> = _loadingState

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _nowSearchText = MutableLiveData<String>()
    val nowSearchText: LiveData<String> = _nowSearchText

    private var searchJob: Job? = null

    fun loadData() {
        _nowSearchText.value?.let { searchBoard(it) }
    }

    fun searchBoard(keyWords: String) {
        fetchData(keyWords)
    }

    private fun fetchData(text: String) {
        _nowSearchText.value = text
        data.clear()
        _loadingState.value = false
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            _loadingState.value = true
            searchBoardRepository.searchBoardByKeyword(text).catch { e ->
                _errorMessage.postValue("Error: $e")
                _loadingState.value = false
            }.collect { boards ->
                val boardData = boards.list.map {
                    SearchBoardsItem(
                        boardId = it.boardId,
                        title = it.boardName,
                        subtitle = it.title
                    )
                }
                data.clear()
                data.addAll(boardData)
                _loadingState.value = false
            }
        }
    }

    fun changeBoardLikeSate(position: Int) {
    }
}
