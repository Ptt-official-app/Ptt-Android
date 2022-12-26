package cc.ptt.android.domain.usecase.login

import cc.ptt.android.common.network.api.apihelper.ApiHelper
import cc.ptt.android.data.repository.login.LoginRepository
import cc.ptt.android.domain.base.UseCaseBase
import cc.ptt.android.domain.model.UserType
import cc.ptt.android.domain.model.ui.user.UserInfo
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LoginUseCase constructor(
    private val loginRepository: LoginRepository,
    private val apiHelper: ApiHelper
) : UseCaseBase() {

    private val _userType = MutableStateFlow<UserType>(UserType.Guest)
    val userType: StateFlow<UserType> = _userType.asStateFlow()

    init {
        initUserType()
    }

    private fun initUserType() = launch {
        loginRepository.getUserInfo()?.let {
            _userType.emit(UserType.Login(UserInfo(it.userId, it.accessToken, it.tokenType)))
        } ?: run {
            _userType.emit(UserType.Guest)
        }
    }

    fun login(id: String, password: String): Flow<UserInfo> {
        return loginRepository.login(apiHelper.getClientId(), apiHelper.getClientSecret(), id, password).map {
            UserInfo(it.userId, it.accessToken, it.tokenType)
        }.onEach { initUserType() }
    }

    fun logout(): Flow<Unit> {
        return loginRepository.logout().onEach { initUserType() }
    }

    fun getUserInfo(): UserInfo? {
        return loginRepository.getUserInfo()?.let { UserInfo(it.userId, it.accessToken, it.tokenType) }
    }
}
