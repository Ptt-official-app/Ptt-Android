package tw.y_studio.ptt.source.remote.user

import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import tw.y_studio.ptt.api.model.user.exist_user.ExistUserRequest
import tw.y_studio.ptt.api.model.user.login.LoginRequest
import tw.y_studio.ptt.api.user.UserApiService

class UserRemoteDataSourceImpl(
    private val userApiService: UserApiService,
    private val dispatcher: CoroutineDispatcher,
) : IUserRemoteDataSource {
    override suspend fun login(
        clientId: String,
        clientSecret: String,
        userName: String,
        password: String
    ) = withContext(dispatcher) {
        val param: String = Gson().toJson(
            LoginRequest(
                clientId = clientId,
                clientSecret = clientSecret,
                userName = userName,
                password = password
            )
        )
        val body = param.toRequestBody("text/plain; charset=utf-8".toMediaType())

        return@withContext userApiService.login(body)
    }

    override suspend fun existUser(
        clientId: String,
        clientSecret: String,
        userName: String
    ) = withContext(dispatcher) {
        val param = Gson().toJson(
            ExistUserRequest(
                clientId = clientId,
                clientSecret = clientSecret,
                userName = userName
            )
        )
        val body = param.toRequestBody("text/plain; charset=utf-8".toMediaType())
        return@withContext userApiService.existUser(body)
    }
}
