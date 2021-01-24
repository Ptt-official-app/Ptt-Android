package tw.y_studio.ptt.di

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModules = module {
    single<CoroutineDispatcher>(named("IO")) { Dispatchers.IO }

    single<SharedPreferences> {
        androidContext().getSharedPreferences("MainSetting", Context.MODE_PRIVATE)
    }
}
