package tw.y_studio.ptt.di

import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tw.y_studio.ptt.BuildConfig
import tw.y_studio.ptt.api.PostAPI
import tw.y_studio.ptt.api.SearchBoardAPI
import tw.y_studio.ptt.api.board.BoardApiService
import tw.y_studio.ptt.utils.OkHttpUtils
import java.util.concurrent.TimeUnit

val apiModules = module {
    factory { SearchBoardAPI() }
    factory { PostAPI() }
    single { provideOkHttpClient() }
    single { provideRetrofit(get()) }
    factory { provideBoardApiService(get()) }
}

private fun provideRetrofit(client: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BuildConfig.develop_domain)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

private fun provideOkHttpClient(): OkHttpClient {
    return OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(OkHttpUtils.GzipRequestInterceptor()) // TODO: 2021/1/31 add TokenInterceptor
        .build()
}

private fun provideBoardApiService(retrofit: Retrofit): BoardApiService {
    return retrofit.create(BoardApiService::class.java)
}
