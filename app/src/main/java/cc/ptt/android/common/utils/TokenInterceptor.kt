package cc.ptt.android.common.utils

import android.content.SharedPreferences
import cc.ptt.android.data.common.PreferenceConstants
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor(private val preferences: SharedPreferences) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        with(preferences) {
            getString(PreferenceConstants.accessToken, null)?.let { accessToken ->
                getString(PreferenceConstants.tokenType, null)?.let { tokenType ->
                    requestBuilder.addHeader("Authorization", "$tokenType $accessToken")
                }
            }
        }

        return chain.proceed(requestBuilder.build())
    }
}
