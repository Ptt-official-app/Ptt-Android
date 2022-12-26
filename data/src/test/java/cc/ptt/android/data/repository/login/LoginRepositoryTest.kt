package cc.ptt.android.data.repository.login

import cc.ptt.android.common.network.api.apihelper.ApiHelper
import cc.ptt.android.data.ApiTestBase
import cc.ptt.android.data.BuildConfig
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
        if (BuildConfig.TEST_ACCOUNT.isBlank() || BuildConfig.TEST_PASSWORD.isBlank()) {
            // Skip when no testing account
            assert(true)
            return@runBlocking
        }
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
