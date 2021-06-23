package tw.y_studio.ptt.di

import android.content.SharedPreferences
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tw.y_studio.ptt.BuildConfig
import tw.y_studio.ptt.api.PostAPI
import tw.y_studio.ptt.api.SearchBoardAPI
import tw.y_studio.ptt.api.article.ArticleApiService
import tw.y_studio.ptt.api.board.BoardApiService
import tw.y_studio.ptt.api.user.UserApiService
import tw.y_studio.ptt.utils.PreferenceConstants
import tw.y_studio.ptt.utils.TokenInterceptor
import java.util.concurrent.TimeUnit

val apiModules = module {
    factory { SearchBoardAPI() }
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
        .baseUrl(preferences.getString(PreferenceConstants.apiDomain, BuildConfig.develop_domain))
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
