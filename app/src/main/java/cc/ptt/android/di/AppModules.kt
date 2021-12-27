package cc.ptt.android.di

import android.content.Context
import android.content.SharedPreferences
import cc.ptt.android.data.common.PreferenceConstants
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModules = module {
    single<CoroutineDispatcher>(IO) { Dispatchers.IO }
    single<CoroutineDispatcher>(UI) { Dispatchers.Main }

    single<SharedPreferences> {
        androidContext().getSharedPreferences(PreferenceConstants.prefName, Context.MODE_PRIVATE)
    }
}

val IO get() = named("IO")
val UI get() = named("UI")
