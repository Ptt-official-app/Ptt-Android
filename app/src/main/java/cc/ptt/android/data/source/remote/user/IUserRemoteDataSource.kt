package cc.ptt.android.data.source.remote.user

import cc.ptt.android.data.model.remote.user.exist_user.ExistUser
import cc.ptt.android.data.model.remote.user.login.Login

interface IUserRemoteDataSource {
    suspend fun login(
        clientId: String,
        clientSecret: String,
        userName: String,
        password: String
    ): Login

    suspend fun existUser(
        clientId: String,
        clientSecret: String,
        userName: String
    ): ExistUser
}
