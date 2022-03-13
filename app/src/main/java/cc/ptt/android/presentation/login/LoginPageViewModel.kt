package cc.ptt.android.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cc.ptt.android.R
import cc.ptt.android.data.common.StringUtils
import cc.ptt.android.domain.usecase.login.LoginUseCase
import cc.ptt.android.presentation.common.event.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.*
import javax.inject.Inject

@HiltViewModel
class LoginPageViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _passwordMessage = MutableLiveData<Int>()
    val passwordMessage: LiveData<Int> = _passwordMessage

    private val _errorMessage = MutableLiveData<Event<Int>>()
    val errorMessage: LiveData<Event<Int>> = _errorMessage

    private val _loginSuccess = MutableLiveData<Unit>()
    val loginSuccess: LiveData<Unit> = _loginSuccess

    fun checkLoginLegal(account: String, password: String) = viewModelScope.launch {
        if (!StringUtils.isAccount(account)) {
            _passwordMessage.value = R.string.not_this_password
            return@launch
        }

        loginUseCase(
            LoginUseCase.Params(account, password)
        ).onSuccess {
            _loginSuccess.value = Unit
        }.onFailure {
            it.printStackTrace()
            _errorMessage.value = Event(R.string.server_error)
        }
    }
}
