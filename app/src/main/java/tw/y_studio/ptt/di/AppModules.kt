package tw.y_studio.ptt.di

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import tw.y_studio.ptt.utils.PreferenceConstants

val appModules = module {
    single<CoroutineDispatcher>(IO) { Dispatchers.IO }

    single<SharedPreferences> {
        androidContext().getSharedPreferences(PreferenceConstants.prefName, Context.MODE_PRIVATE)
    }
}

val IO get() = named("IO")
