package cc.ptt.android.di

import cc.ptt.android.data.source.local.LoginLocalDataSource
import cc.ptt.android.data.source.local.LoginLocalDataSourceImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

val localDataSourceModules = module {
    single <LoginLocalDataSource> { LoginLocalDataSourceImpl(get(named(MainPreferences)), get()) }
}
