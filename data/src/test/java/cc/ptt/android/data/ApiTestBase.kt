package cc.ptt.android.data

import cc.ptt.android.common.network.api.apihelper.ApiHelper
import cc.ptt.android.data.di.localDataSourceModules
import cc.ptt.android.data.model.remote.user.login.LoginEntity
import cc.ptt.android.data.repository.user.UserRepository
import cc.ptt.android.data.source.local.LoginLocalDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.koin.core.context.GlobalContext.loadKoinModules
import org.koin.core.context.GlobalContext.unloadKoinModules
import org.koin.dsl.module
import org.koin.test.inject

@FlowPreview
@ExperimentalCoroutinesApi
open class ApiTestBase constructor(
    private val needLogin: Boolean = true
) : KoinTestBase() {

    companion object {
        @JvmStatic
        var userInfo: LoginEntity? = null
    }

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() = runBlocking {
        unloadKoinModules(localDataSourceModules)
        loadKoinModules(testLocalDataSourceModules)
        if (needLogin) {
            login()
        }
    }

    @After
    fun tearDown() {
        unloadKoinModules(testLocalDataSourceModules)
        loadKoinModules(localDataSourceModules)
    }

    protected suspend fun login() {
        if (BuildConfig.TEST_ACCOUNT.isBlank() || BuildConfig.TEST_PASSWORD.isBlank()) {
            println("[Test] Login error: No testing account")
            return
        }
        val userRepository: UserRepository by inject()
        val apiHelper: ApiHelper by inject()
        userRepository.login(
            apiHelper.getClientId(),
            apiHelper.getClientSecret(),
            BuildConfig.TEST_ACCOUNT,
            BuildConfig.TEST_PASSWORD
        ).catch { e ->
            println("[Test] Login error: $e")
        }.collect()
    }

    private val testLocalDataSourceModules = module {
        single <LoginLocalDataSource> { TestLoginLocalDataSource() }
    }

    class TestLoginLocalDataSource : LoginLocalDataSource {
        override fun isLogin(): Boolean {
            return userInfo != null
        }

        override fun cleanUserInfo() {
            userInfo = null
        }

        override fun setUserInfo(userInfo: LoginEntity) {
            Companion.userInfo = userInfo
        }

        override fun getUserInfo(): LoginEntity? {
            return userInfo
        }
    }
}
