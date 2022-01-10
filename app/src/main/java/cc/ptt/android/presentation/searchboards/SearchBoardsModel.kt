package cc.ptt.android.presentation.searchboards

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cc.ptt.android.data.model.remote.board.searchboard.SearchBoardsItem
import cc.ptt.android.data.source.remote.search.ISearchBoardRemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class SearchBoardsModel(
    private val searchBoardRemoteDataSource: ISearchBoardRemoteDataSource,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    val data: MutableList<SearchBoardsItem> = mutableListOf()

    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> = _loadingState

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _nowSearchText = MutableLiveData<String>()
    val nowSearchText: LiveData<String> = _nowSearchText

    private val _waitSearchText = MutableLiveData<String>()
    val waitSearchText: LiveData<String> = _waitSearchText

    fun loadData() {
        if (_loadingState.value == true) return
        _nowSearchText.value?.let { searchBoard(it) }
    }

    fun searchBoard(keyWords: String) {
        if (_loadingState.value == true) {
            _waitSearchText.value = keyWords
            return
        }

        _nowSearchText.value = keyWords

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
            val boards = searchBoardRemoteDataSource.searchBoardByKeyword(
                (_nowSearchText.value ?: "").replace(" ", "")
            )
            val boardData = boards.list.map {
                SearchBoardsItem(
                    boardId = it.boardId,
                    title = it.boardName,
                    subtitle = it.title
                )
            }
            data += boardData
        } catch (e: Exception) {
            _errorMessage.postValue("Error: $e")
        }
    }

    fun changeBoardLikeSate(position: Int) {
    }

    override fun onCleared() {
        super.onCleared()
    }
}
