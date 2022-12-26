package cc.ptt.android.home.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cc.ptt.android.common.logger.PttLogger
import cc.ptt.android.data.repository.login.LoginRepository
import cc.ptt.android.domain.usecase.login.LoginUseCase
import kotlinx.coroutines.launch

class SettingViewModel constructor(
    private val loginRepository: LoginRepository,
    private val loginUseCase: LoginUseCase,
    private val logger: PttLogger
) : ViewModel() {

    val loginState get() = loginUseCase.userType

    fun isLogin(): Boolean {
        return loginRepository.isLogin()
    }

    fun logout() = viewModelScope.launch {
        loginUseCase.logout().collect {
            logger.d("SettingViewModel", "logout success")
        }
    }
}
