package cc.ptt.android.presentation.hotarticle

import android.util.Log
import androidx.lifecycle.*
import cc.ptt.android.common.utils.Log
import cc.ptt.android.data.model.ui.hotarticle.HotArticleUI
import cc.ptt.android.domain.usecase.GetPopularArticlesUIUseCase
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class HotArticleListViewModel constructor(
    private val getPopularArticlesUIUseCase: GetPopularArticlesUIUseCase,
    private val uiDispatcher: CoroutineDispatcher,
    ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    companion object {
        private val TAG = HotArticleListViewModel::class.java.simpleName
    }

    private val job = Job()

    private val coroutineContext: CoroutineContext = job + ioDispatcher

    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> = _loadingState

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    val data: MutableList<HotArticleUI> = arrayListOf()

    private var startIndex = ""
    private var hasNext = false

    fun loadData(getNext: Boolean) {
        if (_loadingState.value == true) {
            return
        }
        if (getNext && !hasNext) {
            return
        }
        _loadingState.value = true
        viewModelScope.launch(coroutineContext) {
            getPopularArticlesUIUseCase(
                GetPopularArticlesUIUseCase.Params(startIndex, getNext)
            ).onSuccess {
                Log(TAG, "onSuccess: ${it.data}")
                withContext(uiDispatcher) {
                    startIndex = it.nextIdx
                    hasNext = it.nextIdx.isNotEmpty()
                    if (!getNext) {
                        data.clear()
                    }
                    data.addAll(it.data)
                    _loadingState.value = false
                }
            }.onFailure {
                Log.d(HotArticleListViewModel.TAG, "onFailure : ${it.localizedMessage}")
                withContext(uiDispatcher) {
                    _loadingState.value = false
                    _errorMessage.value = it.localizedMessage
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancelChildren()
    }
}
