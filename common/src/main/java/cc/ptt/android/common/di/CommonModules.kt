package cc.ptt.android.common.di

import cc.ptt.android.common.logger.PttLogger
import cc.ptt.android.common.logger.PttLoggerImpl
import org.koin.dsl.module

val commonModules = module {
    single <PttLogger> { PttLoggerImpl() }
}
