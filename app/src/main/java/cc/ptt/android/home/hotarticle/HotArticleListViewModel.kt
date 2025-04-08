package cc.ptt.android.home.hotarticle

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cc.ptt.android.domain.model.ui.hotarticle.HotArticleUI
import cc.ptt.android.domain.usecase.GetPopularArticlesUIUseCase
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HotArticleListViewModel constructor(
    private val getPopularArticlesUIUseCase: GetPopularArticlesUIUseCase,
) : ViewModel() {
    companion object {
        private val TAG = HotArticleListViewModel::class.java.simpleName
    }

    private val job = Job()

    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> = _loadingState

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    val data: MutableList<HotArticleUI> = arrayListOf()

    private var startIndex = ""
    private var hasNext = false

    @OptIn(FlowPreview::class)
    fun loadData(getNext: Boolean) {
        if (_loadingState.value == true) {
            return
        }
        if (getNext && !hasNext) {
            return
        }
        _loadingState.value = true
        viewModelScope.launch {
            getPopularArticlesUIUseCase
                .getPopularArticles(startIndex, getNext)
                .catch { e ->
                    _loadingState.value = false
                    _errorMessage.value = e.localizedMessage
                }.collect {
                    startIndex = it.nextIdx
                    hasNext = it.nextIdx.isNotEmpty()
                    if (!getNext) {
                        data.clear()
                    }
                    data.addAll(it.data)
                    _loadingState.value = false
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancelChildren()
    }
}
