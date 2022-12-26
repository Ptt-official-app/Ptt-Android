package cc.ptt.android.data.repository.login

import cc.ptt.android.data.model.remote.user.exist_user.ExistUser
import cc.ptt.android.data.model.remote.user.login.Login
import cc.ptt.android.data.model.ui.user.UserInfo
import cc.ptt.android.data.source.local.LoginLocalDataSource
import cc.ptt.android.data.source.remote.login.LoginRemoteDataSource
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class LoginRepositoryImpl constructor(
    private val loginLocalDataSource: LoginLocalDataSource,
    private val loginRemoteDataSource: LoginRemoteDataSource
) : LoginRepository, CoroutineScope by MainScope() {

    companion object {
        private val TAG = LoginRepository::class.java.simpleName
    }

    private val _userType = MutableStateFlow<UserType>(UserType.Guest)
    override val userType: StateFlow<UserType> = _userType.asStateFlow()

    init {
        initUserType()
    }

    private fun initUserType() = launch {
        getUserInfo()?.let {
            _userType.emit(UserType.Login(it))
        } ?: run {
            _userType.emit(UserType.Guest)
        }
    }

    override fun login(
        clientId: String,
        clientSecret: String,
        userName: String,
        password: String
    ): Flow<Login> {
        return loginRemoteDataSource.login(clientId, clientSecret, userName, password).onEach {
            val userInfo = UserInfo(it.userId, it.accessToken, it.tokenType)
            loginLocalDataSource.setUserInfo(userInfo)
            initUserType()
        }.flowOn(Dispatchers.IO)
    }

    override fun logout(): Flow<Unit> {
        return flow {
            emit(Unit)
        }.onEach {
            loginLocalDataSource.cleanUserInfo()
            initUserType()
        }.flowOn(Dispatchers.IO)
    }

    override fun existUser(
        clientId: String,
        clientSecret: String,
        userName: String
    ): Flow<ExistUser> {
        return loginRemoteDataSource.existUser(clientId, clientSecret, userName).flowOn(Dispatchers.IO)
    }

    override fun isLogin(): Boolean {
        initUserType()
        return loginLocalDataSource.isLogin()
    }

    override fun isGuest(): Boolean {
        initUserType()
        return !loginLocalDataSource.isLogin()
    }

    override fun getUserInfo(): UserInfo? {
        return loginLocalDataSource.getUserInfo()
    }
}
