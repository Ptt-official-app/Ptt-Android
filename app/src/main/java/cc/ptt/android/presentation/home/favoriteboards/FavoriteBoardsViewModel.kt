package cc.ptt.android.presentation.home.favoriteboards

import android.content.SharedPreferences
import androidx.lifecycle.*
import cc.ptt.android.data.common.PreferenceConstants
import cc.ptt.android.data.model.remote.board.hotboard.HotBoardsItem
import cc.ptt.android.data.source.remote.favorite.IFavoriteRemoteDataSource
import cc.ptt.android.di.IODispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

@HiltViewModel
class FavoriteBoardsViewModel @Inject constructor(
    private val favoriteRemoteDataSource: IFavoriteRemoteDataSource,
    @IODispatchers private val ioDispatcher: CoroutineDispatcher,
    private val preferences: SharedPreferences
) : ViewModel() {
    val data: MutableList<HotBoardsItem> = mutableListOf()
    private val page = AtomicInteger(1)

    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> = _loadingState

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val startIndex = MutableLiveData<String>("")

    fun loadData() {
        viewModelScope.launch {
            if (_loadingState.value == true) return@launch
            data.clear()
            startIndex.value = ""
            _loadingState.value = true
            startIndex.value = getDataFromApi()
            _loadingState.value = false
        }
    }

    fun loadNextData() {
        if (startIndex.value.isNullOrEmpty()) return
        viewModelScope.launch {
            if (_loadingState.value == true) return@launch
            data.clear()
            _loadingState.value = true
            startIndex.value = getDataFromApi()
            _loadingState.value = false
        }
    }

    fun deleteBoard(item: HotBoardsItem) {
    }

    private suspend fun getDataFromApi(): String = withContext(ioDispatcher) {
        try {
            val favoriteBoards = favoriteRemoteDataSource
                .getFavoriteBoards(
                    userid = preferences.getString(PreferenceConstants.id, null) ?: "",
                    startIndex = startIndex.value ?: ""
                )
            val boardData = favoriteBoards.list.map {
                HotBoardsItem(
                    it.boardId,
                    it.boardName,
                    it.title,
                    it.onlineUser.toString(),
                    "7"
                )
            }
            data.addAll(boardData)
            return@withContext favoriteBoards.nextId
        } catch (e: Exception) {
            _errorMessage.postValue("Error2: $e")
            return@withContext ""
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}
