package cc.ptt.android.data.repository.login

import cc.ptt.android.data.model.remote.user.exist_user.ExistUser
import cc.ptt.android.data.model.remote.user.login.LoginEntity
import kotlinx.coroutines.flow.Flow

interface LoginRepository {

    fun login(
        clientId: String,
        clientSecret: String,
        userName: String,
        password: String
    ): Flow<LoginEntity>

    fun logout(): Flow<Unit>

    fun existUser(
        clientId: String,
        clientSecret: String,
        userName: String
    ): Flow<ExistUser>

    fun isLogin(): Boolean

    fun isGuest(): Boolean

    fun getUserInfo(): LoginEntity?
}
