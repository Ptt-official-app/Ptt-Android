package cc.ptt.android.common.utils

import cc.ptt.android.data.source.local.LoginDataStore
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor constructor(
    private val loginDataStore: LoginDataStore
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        if (loginDataStore.isLogin()) {
            loginDataStore.getUserInfo()?.let {
                requestBuilder.addHeader("Authorization", "${it.tokenType} ${it.accessToken}")
            }
        }

        return chain.proceed(requestBuilder.build())
    }
}
