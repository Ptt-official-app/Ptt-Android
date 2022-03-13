package cc.ptt.android.di

import android.content.SharedPreferences
import cc.ptt.android.BuildConfig
import cc.ptt.android.common.utils.TokenInterceptor
import cc.ptt.android.data.api.PostAPI
import cc.ptt.android.data.api.article.ArticleApiService
import cc.ptt.android.data.api.board.BoardApiService
import cc.ptt.android.data.api.user.UserApiService
import cc.ptt.android.data.common.PreferenceConstants
import cc.ptt.android.data.source.local.LoginDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModules {

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, preferences: SharedPreferences): Retrofit {
        return Retrofit.Builder()
            .baseUrl(preferences.getString(PreferenceConstants.apiDomain, BuildConfig.APIDomain) ?: BuildConfig.APIDomain)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(logInterceptor: HttpLoggingInterceptor, loginDataStore: LoginDataStore): OkHttpClient {
        return OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(logInterceptor)
            .addInterceptor(TokenInterceptor(loginDataStore))
            .build()
    }

    @Provides
    fun provideLogInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
    }

    @Provides
    fun provideBoardApiService(retrofit: Retrofit): BoardApiService {
        return retrofit.create(BoardApiService::class.java)
    }

    @Provides
    fun provideUserApiService(retrofit: Retrofit): UserApiService {
        return retrofit.create(UserApiService::class.java)
    }

    @Provides
    fun provideArticleApiService(retrofit: Retrofit): ArticleApiService {
        return retrofit.create(ArticleApiService::class.java)
    }

    @Provides
    fun providePostApi(): PostAPI {
        return PostAPI()
    }
}
