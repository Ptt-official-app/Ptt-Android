package cc.ptt.android.data.source.remote.user

import cc.ptt.android.data.model.remote.user.exist_user.ExistUser
import cc.ptt.android.data.model.remote.user.login.LoginEntity
import cc.ptt.android.data.model.remote.user.user_id.UserIdEntity
import kotlinx.coroutines.flow.Flow

interface UserRemoteDataSource {

    fun login(
        clientId: String,
        clientSecret: String,
        userName: String,
        password: String
    ): Flow<LoginEntity>

    fun existUser(
        clientId: String,
        clientSecret: String,
        userName: String
    ): Flow<ExistUser>

    fun userId(): Flow<UserIdEntity>
}
