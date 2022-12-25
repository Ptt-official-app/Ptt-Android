package cc.ptt.android.common.api

import cc.ptt.android.common.apihelper.ApiHelper
import cc.ptt.android.common.retrofit.RetrofitFlowCallAdapterFactory
import cc.ptt.android.data.source.local.LoginLocalDataSource
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitServiceProvider constructor(
    private val apiHelper: ApiHelper,
    private val loginLocalDataSource: LoginLocalDataSource
) {

    companion object {
        const val TIMEOUT = 30L
    }

    fun <T> create(serviceClass: Class<T>): T {
        val maxLogLevel: HttpLoggingInterceptor.Level = when (serviceClass.getAnnotation(ApiMaxLogLevel::class.java)?.level) {
            MaxLogLevel.NONE -> HttpLoggingInterceptor.Level.NONE
            MaxLogLevel.BASIC -> HttpLoggingInterceptor.Level.BASIC
            MaxLogLevel.HEADERS -> HttpLoggingInterceptor.Level.HEADERS
            MaxLogLevel.BODY -> HttpLoggingInterceptor.Level.BODY
            else -> HttpLoggingInterceptor.Level.BODY
        }
        return createRetrofit(apiHelper.getHost(), maxLogLevel).create(serviceClass)
    }

    private fun createRetrofit(host: String, level: HttpLoggingInterceptor.Level): Retrofit {
        return Retrofit.Builder()
            .baseUrl(host)
            .client(createOkHttpClient(level))
            .addCallAdapterFactory(RetrofitFlowCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun createOkHttpClient(level: HttpLoggingInterceptor.Level): OkHttpClient {
        return OkHttpClient.Builder().connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(createLoggingInterceptor(level))
            .addInterceptor(TokenInterceptor(loginLocalDataSource))
            .addInterceptor(ApiResponseInterceptor())
            .build()
    }

    private fun createLoggingInterceptor(level: HttpLoggingInterceptor.Level): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            this.level = level
        }
    }
}
