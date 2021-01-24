package tw.y_studio.ptt.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModules = module {
    single<CoroutineDispatcher>(named("IO")) { Dispatchers.IO }
}
