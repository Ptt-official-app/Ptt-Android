package tw.y_studio.ptt.source.remote.user

import tw.y_studio.ptt.api.model.user.Login

interface IUserRemoteDataSource {
    suspend fun login(
        clientId: String,
        clientSecret: String,
        userName: String,
        password: String
    ): Login

    suspend fun existLogin()
}
