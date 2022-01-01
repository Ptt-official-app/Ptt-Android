package cc.ptt.android

import androidx.multidex.MultiDexApplication
import cc.ptt.android.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class PttApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@PttApplication)
            modules(
                listOf(
                    appModules,
                    dataStoreModules,
                    viewModelModules,
                    apiModules,
                    dataSourceModules,
                    useCaseModels,
                    repositoryModules
                )
            )
        }
    }
}
