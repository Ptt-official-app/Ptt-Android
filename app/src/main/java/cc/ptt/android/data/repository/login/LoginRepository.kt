package cc.ptt.android.data.repository.login

import cc.ptt.android.data.model.remote.user.exist_user.ExistUser
import cc.ptt.android.data.model.remote.user.login.Login
import cc.ptt.android.data.model.ui.user.UserInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface LoginRepository {

    val userType: StateFlow<UserType>

    fun login(
        clientId: String,
        clientSecret: String,
        userName: String,
        password: String
    ): Flow<Login>

    fun logout(): Flow<Unit>

    fun existUser(
        clientId: String,
        clientSecret: String,
        userName: String
    ): Flow<ExistUser>

    fun isLogin(): Boolean

    fun isGuest(): Boolean

    fun getUserInfo(): UserInfo?
}

sealed class UserType {
    object Guest : UserType()
    data class Login(val userInfo: UserInfo) : UserType()
}
