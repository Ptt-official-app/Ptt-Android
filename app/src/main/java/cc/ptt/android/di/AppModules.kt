package cc.ptt.android.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import cc.ptt.android.data.common.PreferenceConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModules {

    @Provides
    @Singleton
    fun provideSharedPreferences(app: Application): SharedPreferences {
        return app.getSharedPreferences(PreferenceConstants.prefName, Context.MODE_PRIVATE)
    }

    @IODispatchers
    @Provides
    @Singleton
    fun provideIOCoroutineDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    @UIDispatchers
    @Provides
    @Singleton
    fun provideUICoroutineDispatcher(): CoroutineDispatcher {
        return Dispatchers.Main
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IODispatchers

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UIDispatchers
