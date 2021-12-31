package cc.ptt.android.di

import cc.ptt.android.data.source.local.LoginDataStore
import cc.ptt.android.data.source.local.LoginDataStoreImpl
import org.koin.dsl.module

val dataStoreModules = module {
    factory<LoginDataStore> { LoginDataStoreImpl(get()) }
}
