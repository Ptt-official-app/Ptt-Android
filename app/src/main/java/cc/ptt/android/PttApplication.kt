package cc.ptt.android

import android.app.Application
import cc.ptt.android.common.di.commonModules
import cc.ptt.android.data.di.apiModules
import cc.ptt.android.data.di.localDataSourceModules
import cc.ptt.android.data.di.preferenceModules
import cc.ptt.android.data.di.remoteDataSourceModules
import cc.ptt.android.data.di.repositoryModules
import cc.ptt.android.di.viewModelModules
import cc.ptt.android.domain.di.useCaseModules
import kotlinx.coroutines.FlowPreview
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class PttApplication : Application() {
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
                ),
            )
        }
    }
}
