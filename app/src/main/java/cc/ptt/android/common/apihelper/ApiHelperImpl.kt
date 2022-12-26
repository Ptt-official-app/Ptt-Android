package cc.ptt.android.common.apihelper

import android.content.SharedPreferences
import cc.ptt.android.BuildConfig
import cc.ptt.android.common.apihelper.ApiHelper.Companion.CLIENT_ID
import cc.ptt.android.common.apihelper.ApiHelper.Companion.CLIENT_SECRET
import cc.ptt.android.data.common.PreferenceConstants

class ApiHelperImpl constructor(
    private val preferences: SharedPreferences
) : ApiHelper {

    override fun getHost(): String {
        return preferences.getString(PreferenceConstants.apiDomain, BuildConfig.API_HOST) ?: BuildConfig.API_HOST
    }

    override fun getClientId(): String {
        return CLIENT_ID
    }

    override fun getClientSecret(): String {
        return CLIENT_SECRET
    }
}
