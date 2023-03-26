package cc.ptt.android.data.repository.user

import cc.ptt.android.data.model.remote.user.exist_user.ExistUser
import cc.ptt.android.data.model.remote.user.login.LoginEntity
import cc.ptt.android.data.source.local.LoginLocalDataSource
import cc.ptt.android.data.source.remote.user.UserRemoteDataSource
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class UserRepositoryImpl constructor(
    private val loginLocalDataSource: LoginLocalDataSource,
    private val userRemoteDataSource: UserRemoteDataSource
) : UserRepository, CoroutineScope by MainScope() {

    companion object {
        private val TAG = UserRepository::class.java.simpleName
    }

    override fun login(
        clientId: String,
        clientSecret: String,
        userName: String,
        password: String
    ): Flow<LoginEntity> {
        return userRemoteDataSource.login(clientId, clientSecret, userName, password).onEach {
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
        return userRemoteDataSource.existUser(clientId, clientSecret, userName).flowOn(Dispatchers.IO)
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

    override fun userId(): Flow<String> {
        return userRemoteDataSource.userId().map {
            it.userId
        }.flowOn(Dispatchers.IO)
    }
}
