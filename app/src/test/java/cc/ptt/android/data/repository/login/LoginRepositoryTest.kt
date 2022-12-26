package cc.ptt.android.data.repository.login

import cc.ptt.android.ApiTestBase
import cc.ptt.android.BuildConfig
import cc.ptt.android.common.apihelper.ApiHelper
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.koin.test.inject
import kotlin.test.assertEquals

class LoginRepositoryTest : ApiTestBase(needLogin = false) {

    private val loginRepository: LoginRepository by inject()
    private val apiHelper: ApiHelper by inject()

    @Test
    fun testLogin() = runBlocking {
        loginRepository.login(
            apiHelper.getClientId(),
            apiHelper.getClientSecret(),
            BuildConfig.TEST_ACCOUNT,
            BuildConfig.TEST_PASSWORD
        ).catch {
            assert(false)
        }.collect {
            assert(it.accessToken.isNotBlank())
            assert(it.tokenType.isNotBlank())
            assertEquals(BuildConfig.TEST_ACCOUNT, it.userId)
        }
    }
}
