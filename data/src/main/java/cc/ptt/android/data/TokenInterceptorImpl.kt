package cc.ptt.android.data

import cc.ptt.android.common.network.api.TokenInterceptor
import cc.ptt.android.data.source.local.LoginLocalDataSource
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptorImpl constructor(
    private val loginLocalDataSource: LoginLocalDataSource
) : TokenInterceptor {

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
