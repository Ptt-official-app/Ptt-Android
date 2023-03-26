package cc.ptt.android.home.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cc.ptt.android.common.logger.PttLogger
import cc.ptt.android.data.repository.user.UserRepository
import cc.ptt.android.domain.usecase.user.UserUseCase
import kotlinx.coroutines.launch

class SettingViewModel constructor(
    private val userRepository: UserRepository,
    private val userUseCase: UserUseCase,
    private val logger: PttLogger
) : ViewModel() {

    val loginState get() = userUseCase.userType

    fun isLogin(): Boolean {
        return userRepository.isLogin()
    }

    fun logout() = viewModelScope.launch {
        userUseCase.logout().collect {
            logger.d("SettingViewModel", "logout success")
        }
    }
}
