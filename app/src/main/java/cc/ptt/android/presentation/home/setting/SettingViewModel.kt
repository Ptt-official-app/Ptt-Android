package cc.ptt.android.presentation.home.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cc.ptt.android.common.utils.log
import cc.ptt.android.data.repository.login.LoginRepository
import cc.ptt.android.domain.usecase.login.LoginUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SettingViewModel constructor(
    private val loginRepository: LoginRepository,
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    val loginState get() = loginRepository.userType

    fun isLogin(): Boolean {
        return loginRepository.isLogin()
    }

    fun logout() = viewModelScope.launch {
        loginUseCase.logout().collect {
            log("SettingViewModel", "logout success")
        }
    }
}
