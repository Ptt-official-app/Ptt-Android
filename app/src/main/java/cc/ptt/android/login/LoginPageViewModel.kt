package cc.ptt.android.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cc.ptt.android.R
import cc.ptt.android.common.StringUtils
import cc.ptt.android.common.event.Event
import cc.ptt.android.domain.usecase.login.LoginUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class LoginPageViewModel constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _passwordMessage = MutableLiveData<Int>()
    val passwordMessage: LiveData<Int> = _passwordMessage

    private val _errorMessage = MutableLiveData<Event<Int>>()
    val errorMessage: LiveData<Event<Int>> = _errorMessage

    private val _loginSuccess = MutableLiveData<Unit>()
    val loginSuccess: LiveData<Unit> = _loginSuccess

    private var loginJob: Job? = null

    fun checkLoginLegal(account: String, password: String) {
        if (!StringUtils.isAccount(account)) {
            _passwordMessage.value = R.string.not_this_password
            return
        }
        loginJob?.cancel()
        loginJob = viewModelScope.launch {
            loginUseCase.login(account, password).catch {
                _errorMessage.value = Event(R.string.server_error)
            }.collect {
                _loginSuccess.value = Unit
            }
        }
    }
}
