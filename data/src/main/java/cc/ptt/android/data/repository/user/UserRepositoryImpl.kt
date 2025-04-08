package cc.ptt.android.data.repository.user

import cc.ptt.android.data.model.remote.user.existuser.ExistUser
import cc.ptt.android.data.model.remote.user.login.LoginEntity
import cc.ptt.android.data.source.local.LoginLocalDataSource
import cc.ptt.android.data.source.remote.user.UserRemoteDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class UserRepositoryImpl constructor(
    private val loginLocalDataSource: LoginLocalDataSource,
    private val userRemoteDataSource: UserRemoteDataSource,
) : UserRepository,
    CoroutineScope by MainScope() {
    companion object {
        private val TAG = UserRepository::class.java.simpleName
    }

    override fun login(
        clientId: String,
        clientSecret: String,
        userName: String,
        password: String,
    ): Flow<LoginEntity> =
        userRemoteDataSource
            .login(clientId, clientSecret, userName, password)
            .onEach {
                loginLocalDataSource.setUserInfo(it)
            }.flowOn(Dispatchers.IO)

    override fun logout(): Flow<Unit> =
        flow {
            emit(Unit)
        }.onEach {
            loginLocalDataSource.cleanUserInfo()
        }.flowOn(Dispatchers.IO)

    override fun existUser(
        clientId: String,
        clientSecret: String,
        userName: String,
    ): Flow<ExistUser> = userRemoteDataSource.existUser(clientId, clientSecret, userName).flowOn(Dispatchers.IO)

    override fun isLogin(): Boolean = loginLocalDataSource.isLogin()

    override fun isGuest(): Boolean = !loginLocalDataSource.isLogin()

    override fun getUserInfo(): LoginEntity? = loginLocalDataSource.getUserInfo()

    override fun userId(): Flow<String> =
        userRemoteDataSource
            .userId()
            .map {
                it.userId
            }.flowOn(Dispatchers.IO)
}
