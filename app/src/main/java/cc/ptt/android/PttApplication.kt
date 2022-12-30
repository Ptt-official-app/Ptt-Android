package cc.ptt.android

import androidx.multidex.MultiDexApplication
import cc.ptt.android.common.di.commonModules
import cc.ptt.android.data.di.*
import cc.ptt.android.di.*
import cc.ptt.android.domain.di.useCaseModules
import kotlinx.coroutines.FlowPreview
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class PttApplication : MultiDexApplication() {

    @FlowPreview
    override fun onCreate() {
        super.onCreate()
        startKoin {
            // declare used modules
            androidContext(this@PttApplication)
            modules(
                listOf(
                    apiModules,
                    remoteDataSourceModules,
                    localDataSourceModules,
                    repositoryModules,
                    useCaseModules,
                    viewModelModules,
                    commonModules,
                    preferenceModules,
                )
            )
        }
    }
}
