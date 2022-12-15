package cc.ptt.android.presentation.home.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cc.ptt.android.data.repository.login.LoginRepository
import cc.ptt.android.domain.usecase.login.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    val loginState get() = loginRepository.userType

    fun isLogin(): Boolean {
        return loginRepository.isLogin()
    }

    fun logout() = viewModelScope.launch {
        logoutUseCase(Unit)
    }
}
