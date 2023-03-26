package cc.ptt.android.data.source.remote.user

import cc.ptt.android.data.apiservices.user.UserApi
import cc.ptt.android.data.model.remote.user.exist_user.ExistUser
import cc.ptt.android.data.model.remote.user.exist_user.ExistUserRequest
import cc.ptt.android.data.model.remote.user.login.LoginEntity
import cc.ptt.android.data.model.remote.user.login.LoginRequest
import cc.ptt.android.data.model.remote.user.user_id.UserIdEntity
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

class UserRemoteDataSourceImpl constructor(
    private val userApi: UserApi
) : UserRemoteDataSource {

    override fun login(
        clientId: String,
        clientSecret: String,
        userName: String,
        password: String
    ): Flow<LoginEntity> {
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

    override fun userId(): Flow<UserIdEntity> {
        return userApi.userId()
    }
}
