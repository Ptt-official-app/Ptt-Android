package cc.ptt.android.data

import cc.ptt.android.common.network.api.apihelper.ApiHelper
import cc.ptt.android.common.network.api.apihelper.ApiHelper.Companion.CLIENT_ID
import cc.ptt.android.common.network.api.apihelper.ApiHelper.Companion.CLIENT_SECRET
import cc.ptt.android.data.preference.MainPreferences

class ApiHelperImpl constructor(
    private val mainPreferences: MainPreferences,
) : ApiHelper {
    override fun getHost(): String = mainPreferences.getApiDomain().ifBlank { defaultHost() }

    override fun getClientId(): String = CLIENT_ID

    override fun getClientSecret(): String = CLIENT_SECRET

    private fun defaultHost(): String =
        BuildConfig.API_HOST.ifBlank {
            ApiHelper.API_HOST
        }
}
