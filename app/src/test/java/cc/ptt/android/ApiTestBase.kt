package cc.ptt.android

import cc.ptt.android.common.apihelper.ApiHelper
import cc.ptt.android.data.model.ui.user.UserInfo
import cc.ptt.android.data.repository.login.LoginRepository
import cc.ptt.android.data.source.local.LoginLocalDataSource
import cc.ptt.android.di.localDataSourceModules
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
        var userInfo: UserInfo? = null
    }

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() = runBlocking {
        if (needLogin) {
            unloadKoinModules(localDataSourceModules)
            loadKoinModules(testLocalDataSourceModules)
            login()
        }
    }

    @After
    fun tearDown() {
        if (needLogin) {
            unloadKoinModules(testLocalDataSourceModules)
            loadKoinModules(localDataSourceModules)
        }
    }

    private suspend fun login() {
        val loginRepository: LoginRepository by inject()
        val apiHelper: ApiHelper by inject()
        loginRepository.login(
            apiHelper.getClientId(),
            apiHelper.getClientSecret(),
            BuildConfig.test_account,
            BuildConfig.test_password
        ).catch {
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

        override fun setUserInfo(userInfo: UserInfo) {
            Companion.userInfo = userInfo
        }

        override fun getUserInfo(): UserInfo? {
            return userInfo
        }
    }
}
