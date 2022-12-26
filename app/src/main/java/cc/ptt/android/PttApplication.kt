package cc.ptt.android

import androidx.multidex.MultiDexApplication
import cc.ptt.android.di.*
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
                    appModules,
                    remoteDataSourceModules,
                    localDataSourceModules,
                    repositoryModules,
                    useCaseModules,
                    viewModelModules,
                )
            )
        }
    }
}
