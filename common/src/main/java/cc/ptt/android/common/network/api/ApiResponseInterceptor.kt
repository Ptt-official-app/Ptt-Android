package cc.ptt.android.common.network.api

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ApiResponseInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        if (response.isSuccessful) {
            return response
        } else {
            throw ApiException(response.code, response.body?.string().orEmpty())
        }
    }
}
