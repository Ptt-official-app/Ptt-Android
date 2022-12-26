package cc.ptt.android.common.api

import cc.ptt.android.data.source.local.LoginLocalDataSource
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor constructor(
    private val loginLocalDataSource: LoginLocalDataSource
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        if (loginLocalDataSource.isLogin()) {
            loginLocalDataSource.getUserInfo()?.let {
                requestBuilder.addHeader("Authorization", "${it.tokenType} ${it.accessToken}")
            }
        }

        return chain.proceed(requestBuilder.build())
    }
}
