package tw.y_studio.ptt.fragment.search_boards

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tw.y_studio.ptt.source.remote.search.ISearchBoardRemoteDataSource
import tw.y_studio.ptt.utils.Log
import java.util.*

class SearchBoardsModel(
    private val searchBoardRemoteDataSource: ISearchBoardRemoteDataSource,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    val data: MutableList<Map<String, Any>> = mutableListOf()

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

            // val mDBHelper = FavoriteDBHelper(getCurrentActivity(), "Favorite.db", null, 1)
            var dataTemp: List<Map<String, Any>> = ArrayList()
            Log("onAL", "get data from web start")

            // myBoard.addAll(mDBHelper.getAllSet())
            // myBoardIndex = mDBHelper.getMaxIndex()
            dataTemp += searchBoardRemoteDataSource.searchBoardByKeyword(
                (_nowSearchText.value ?: "").replace(" ", "")
            )
            /*for (item in dataTemp) {
                if (myBoard.contains(item["title"].toString())) {
                    item["like"] = true
                } else {
                    item["like"] = false
                }
            }*/
            data += dataTemp
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
