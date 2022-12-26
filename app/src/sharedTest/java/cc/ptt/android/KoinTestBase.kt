package cc.ptt.android

import android.content.SharedPreferences
import cc.ptt.android.common.apihelper.ApiHelper
import cc.ptt.android.common.security.AESKeyStoreHelper
import cc.ptt.android.di.*
import io.mockk.mockk
import io.mockk.mockkClass
import kotlinx.coroutines.FlowPreview
import org.junit.Rule
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.mock.MockProviderRule

@FlowPreview
open class KoinTestBase : KoinTest {
    companion object {

        @JvmStatic
        val testAppModules = module {
            single(named(MainPreferences)) { mockk<SharedPreferences>(relaxed = true) }
            factory <AESKeyStoreHelper> { MockAESKeyStoreHelperImpl() }
            factory <ApiHelper> { TestApiHelperImpl() }
        }

        @JvmStatic
        val koinModules = listOf(
            apiModules,
            testAppModules,
            remoteDataSourceModules,
            localDataSourceModules,
            repositoryModules,
            useCaseModules,
            viewModelModules,
        )
    }

    @get:Rule
    val koinTestRule = KoinTestRule(
        modules = koinModules
    )

    @get:Rule
    val mockProvider = MockProviderRule.create { clazz ->
        // Your way to build a Mock here
        mockkClass(clazz)
    }
}
