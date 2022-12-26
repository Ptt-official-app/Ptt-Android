package cc.ptt.android.data.repository.login

import cc.ptt.android.data.model.remote.user.exist_user.ExistUser
import cc.ptt.android.data.model.remote.user.login.LoginEntity
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

    override fun login(
        clientId: String,
        clientSecret: String,
        userName: String,
        password: String
    ): Flow<LoginEntity> {
        return loginRemoteDataSource.login(clientId, clientSecret, userName, password).onEach {
            loginLocalDataSource.setUserInfo(it)
        }.flowOn(Dispatchers.IO)
    }

    override fun logout(): Flow<Unit> {
        return flow {
            emit(Unit)
        }.onEach {
            loginLocalDataSource.cleanUserInfo()
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
        return loginLocalDataSource.isLogin()
    }

    override fun isGuest(): Boolean {
        return !loginLocalDataSource.isLogin()
    }

    override fun getUserInfo(): LoginEntity? {
        return loginLocalDataSource.getUserInfo()
    }
}
