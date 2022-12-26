package cc.ptt.android.data.di

import cc.ptt.android.data.preference.MainPreferences
import cc.ptt.android.data.preference.MainPreferencesImpl
import cc.ptt.android.data.preference.UserInfoPreferences
import cc.ptt.android.data.preference.UserInfoPreferencesImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val preferenceModules = module {
    single <UserInfoPreferences> { UserInfoPreferencesImpl(androidContext(), get()) }
    single <MainPreferences> { MainPreferencesImpl(androidContext()) }
}
