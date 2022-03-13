package cc.ptt.android.di

import cc.ptt.android.data.source.local.LoginDataStore
import cc.ptt.android.data.source.local.LoginDataStoreImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataStoreModules {
    @Binds
    @Singleton
    abstract fun provideLoginDataStore(loginDataStoreImpl: LoginDataStoreImpl): LoginDataStore
}
