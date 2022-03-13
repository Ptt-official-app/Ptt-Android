package cc.ptt.android.data.repository.login

import cc.ptt.android.common.utils.Log
import cc.ptt.android.data.api.user.UserApiService
import cc.ptt.android.data.model.remote.user.exist_user.ExistUserRequest
import cc.ptt.android.data.model.remote.user.login.LoginRequest
import cc.ptt.android.data.model.ui.user.UserInfo
import cc.ptt.android.data.source.local.LoginDataStore
import cc.ptt.android.di.IODispatchers
import com.google.gson.Gson
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val loginDataStore: LoginDataStore,
    private val userApiService: UserApiService,
    @IODispatchers private val dispatcher: CoroutineDispatcher,
) : LoginRepository {

    companion object {
        private val TAG = LoginRepository::class.java.simpleName
    }

    private val _userType = MutableStateFlow<UserType>(UserType.Guest)
    override val userType: StateFlow<UserType> = _userType.asStateFlow()

    init {
        initUserType()
    }

    private fun initUserType() = GlobalScope.launch {
        getUserInfo()?.let {
            Log(TAG, "UserInfo: $it")
            _userType.emit(UserType.Login(it))
        } ?: run {
            Log(TAG, "UserInfo: Guest")
            _userType.emit(UserType.Guest)
        }
    }

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

        return@withContext userApiService.login(body).apply {
            val userInfo = UserInfo(userId, accessToken, tokenType)
            loginDataStore.setUserInfo(userInfo)
            initUserType()
        }
    }

    override suspend fun logout(): Unit = withContext(dispatcher) {
        loginDataStore.cleanUserInfo()
        initUserType()
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

    override fun isLogin(): Boolean {
        initUserType()
        return loginDataStore.isLogin()
    }

    override fun isGuest(): Boolean {
        initUserType()
        return !loginDataStore.isLogin()
    }

    override fun getUserInfo(): UserInfo? {
        return loginDataStore.getUserInfo()
    }
}
