package cc.ptt.android.domain.usecase.user

import cc.ptt.android.common.network.api.apihelper.ApiHelper
import cc.ptt.android.data.repository.user.UserRepository
import cc.ptt.android.domain.base.UseCaseBase
import cc.ptt.android.domain.model.UserType
import cc.ptt.android.domain.model.ui.user.UserInfo
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class UserUseCase constructor(
    private val userRepository: UserRepository,
    private val apiHelper: ApiHelper
) : UseCaseBase() {

    private val _userType = MutableStateFlow<UserType>(UserType.Guest)
    val userType: StateFlow<UserType> = _userType.asStateFlow()

    init {
        initUserType()
    }

    private fun initUserType() = launch {
        userRepository.getUserInfo()?.let {
            _userType.emit(UserType.Login(UserInfo(it.userId, it.accessToken, it.tokenType)))
        } ?: run {
            _userType.emit(UserType.Guest)
        }
    }

    fun login(id: String, password: String): Flow<UserInfo> {
        return userRepository.login(apiHelper.getClientId(), apiHelper.getClientSecret(), id, password).map {
            UserInfo(it.userId, it.accessToken, it.tokenType)
        }.onEach { initUserType() }
    }

    fun logout(): Flow<Unit> {
        return userRepository.logout().onEach { initUserType() }
    }

    fun getUserInfo(): UserInfo? {
        return userRepository.getUserInfo()?.let { UserInfo(it.userId, it.accessToken, it.tokenType) }
    }
}
