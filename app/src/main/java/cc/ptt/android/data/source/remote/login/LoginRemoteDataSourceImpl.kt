package cc.ptt.android.data.source.remote.login

import cc.ptt.android.data.api.user.UserApi
import cc.ptt.android.data.model.remote.user.exist_user.ExistUser
import cc.ptt.android.data.model.remote.user.exist_user.ExistUserRequest
import cc.ptt.android.data.model.remote.user.login.Login
import cc.ptt.android.data.model.remote.user.login.LoginRequest
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

class LoginRemoteDataSourceImpl constructor(
    private val userApi: UserApi
) : LoginRemoteDataSource {

    override fun login(
        clientId: String,
        clientSecret: String,
        userName: String,
        password: String
    ): Flow<Login> {
        val param: String = Gson().toJson(
            LoginRequest(
                clientId = clientId,
                clientSecret = clientSecret,
                userName = userName,
                password = password
            )
        )
        val body = param.toRequestBody("text/plain; charset=utf-8".toMediaType())

        return userApi.login(body)
    }

    override fun existUser(
        clientId: String,
        clientSecret: String,
        userName: String
    ): Flow<ExistUser> {
        val param = Gson().toJson(
            ExistUserRequest(
                clientId = clientId,
                clientSecret = clientSecret,
                userName = userName
            )
        )
        val body = param.toRequestBody("text/plain; charset=utf-8".toMediaType())
        return userApi.existUser(body)
    }
}
