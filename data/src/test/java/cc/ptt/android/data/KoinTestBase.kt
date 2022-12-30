package cc.ptt.android.data

import cc.ptt.android.common.network.api.apihelper.ApiHelper
import cc.ptt.android.common.security.AESKeyStoreHelper
import cc.ptt.android.data.di.apiModules
import cc.ptt.android.data.di.localDataSourceModules
import cc.ptt.android.data.di.remoteDataSourceModules
import cc.ptt.android.data.di.repositoryModules
import io.mockk.mockkClass
import kotlinx.coroutines.FlowPreview
import org.junit.Rule
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.mock.MockProviderRule

@FlowPreview
open class KoinTestBase : KoinTest {
    companion object {

        @JvmStatic
        val testAppModules = module {
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
