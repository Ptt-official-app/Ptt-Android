package cc.ptt.android.domain.usecase.user

import cc.ptt.android.common.network.api.apihelper.ApiHelper
import cc.ptt.android.data.repository.user.UserRepository
import cc.ptt.android.domain.base.UseCaseBase
import cc.ptt.android.domain.model.UserType
import cc.ptt.android.domain.model.ui.user.UserInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class UserUseCase(
    private val userRepository: UserRepository,
    private val apiHelper: ApiHelper,
) : UseCaseBase() {
    private val _userType = MutableStateFlow<UserType>(UserType.Guest)
    val userType: StateFlow<UserType> = _userType.asStateFlow()

    init {
        initUserType()
    }

    private fun initUserType() =
        launch {
            userRepository.getUserInfo()?.let {
                _userType.emit(UserType.Login(UserInfo(it.userId, it.accessToken, it.tokenType)))
            } ?: run {
                _userType.emit(UserType.Guest)
            }
        }

    fun login(
        id: String,
        password: String,
    ): Flow<UserInfo> =
        userRepository
            .login(apiHelper.getClientId(), apiHelper.getClientSecret(), id, password)
            .map {
                UserInfo(it.userId, it.accessToken, it.tokenType)
            }.onEach { initUserType() }

    fun logout(): Flow<Unit> = userRepository.logout().onEach { initUserType() }

    fun getUserInfo(): UserInfo? = userRepository.getUserInfo()?.let { UserInfo(it.userId, it.accessToken, it.tokenType) }
}
