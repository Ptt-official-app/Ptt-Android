package cc.ptt.android.common.di

import cc.ptt.android.common.ResourcesProvider
import cc.ptt.android.common.logger.PttLogger
import cc.ptt.android.common.logger.PttLoggerImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val commonModules = module {
    single <PttLogger> { PttLoggerImpl() }
    single { ResourcesProvider(androidContext()) }
}
