package cc.ptt.android.di

import android.content.SharedPreferences
import cc.ptt.android.BuildConfig
import cc.ptt.android.common.utils.TokenInterceptor
import cc.ptt.android.data.api.PostAPI
import cc.ptt.android.data.api.article.ArticleApiService
import cc.ptt.android.data.api.board.BoardApiService
import cc.ptt.android.data.api.user.UserApiService
import cc.ptt.android.data.common.PreferenceConstants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val apiModules = module {
    factory { PostAPI() }
    single { provideOkHttpClient(get(), get()) }
    single { provideRetrofit(get(), get()) }
    factory { provideLogInterceptor() }
    factory { provideBoardApiService(get()) }
    factory { provideUserApiService(get()) }
    factory { provideArticleApiService(get()) }
}

private fun provideRetrofit(client: OkHttpClient, preferences: SharedPreferences): Retrofit {
    return Retrofit.Builder()
        .baseUrl(preferences.getString(PreferenceConstants.apiDomain, BuildConfig.APIDomain))
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

private fun provideOkHttpClient(logInterceptor: HttpLoggingInterceptor, preferences: SharedPreferences): OkHttpClient {
    return OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(logInterceptor)
        .addInterceptor(TokenInterceptor(preferences))
        .build()
}

private fun provideLogInterceptor(): HttpLoggingInterceptor {
    return HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
}

private fun provideBoardApiService(retrofit: Retrofit): BoardApiService {
    return retrofit.create(BoardApiService::class.java)
}

private fun provideUserApiService(retrofit: Retrofit): UserApiService {
    return retrofit.create(UserApiService::class.java)
}

private fun provideArticleApiService(retrofit: Retrofit): ArticleApiService {
    return retrofit.create(ArticleApiService::class.java)
}
