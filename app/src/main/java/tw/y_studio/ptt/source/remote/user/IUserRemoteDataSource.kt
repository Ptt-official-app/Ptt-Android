package tw.y_studio.ptt.source.remote.user

import tw.y_studio.ptt.api.model.user.exist_user.ExistUser
import tw.y_studio.ptt.api.model.user.login.Login

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
