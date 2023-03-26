package cc.ptt.android.data.repository.user

import cc.ptt.android.common.network.api.apihelper.ApiHelper
import cc.ptt.android.data.ApiTestBase
import cc.ptt.android.data.BuildConfig
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.koin.test.inject
import kotlin.test.assertEquals

class UserRepositoryTest : ApiTestBase(needLogin = false) {

    private val userRepository: UserRepository by inject()
    private val apiHelper: ApiHelper by inject()

    @Test
    fun testLogin() = runBlocking {
        userRepository.login(
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

    @Test
    fun testUserId() = runBlocking {
        login()
        userRepository.userId().catch {
            assert(false)
        }.collect {
            assertEquals(BuildConfig.TEST_ACCOUNT, it)
        }
    }
}
