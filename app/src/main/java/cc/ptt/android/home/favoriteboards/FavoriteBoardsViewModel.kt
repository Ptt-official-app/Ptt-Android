package cc.ptt.android.home.favoriteboards

import androidx.lifecycle.*
import cc.ptt.android.data.model.remote.board.hotboard.HotBoardsItem
import cc.ptt.android.data.source.remote.favorite.FavoriteRemoteDataSource
import cc.ptt.android.domain.usecase.login.LoginUseCase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn

class FavoriteBoardsViewModel constructor(
    private val favoriteRemoteDataSource: FavoriteRemoteDataSource,
    private val loginUseCase: LoginUseCase
) : ViewModel() {
    val data: MutableList<HotBoardsItem> = mutableListOf()

    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> = _loadingState

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val startIndex = MutableLiveData<String>("")

    fun loadData() {
        if (_loadingState.value == true) return
        data.clear()
        startIndex.value = ""
        fetchData("")
    }

    fun loadNextData() {
        if (startIndex.value.isNullOrEmpty()) return
        if (_loadingState.value == true) return
        fetchData(startIndex.value.orEmpty())
    }

    fun deleteBoard(item: HotBoardsItem) {
    }

    private fun fetchData(nextIndex: String) {
        viewModelScope.launch {
            _loadingState.value = true
            favoriteRemoteDataSource.getFavoriteBoards(
                userid = loginUseCase.getUserInfo()?.id.orEmpty(),
                startIndex = nextIndex
            ).flowOn(Dispatchers.IO).catch { e ->
                _loadingState.value = false
                _errorMessage.postValue("Error: $e")
            }.collect { hotBoard ->
                val boardData = hotBoard.list.map {
                    HotBoardsItem(
                        it.boardId,
                        it.boardName,
                        it.title,
                        it.onlineUser.toString(),
                        "7"
                    )
                }
                data.addAll(boardData)
                startIndex.value = hotBoard.nextId
                _loadingState.value = false
            }
        }
    }
}
