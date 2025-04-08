package cc.ptt.android.data

import cc.ptt.android.common.network.api.apihelper.ApiHelper

class TestApiHelperImpl : ApiHelper {
    override fun getHost(): String =
        BuildConfig.API_HOST.ifBlank {
            ApiHelper.API_HOST
        }

    override fun getClientId(): String = ApiHelper.CLIENT_ID

    override fun getClientSecret(): String = ApiHelper.CLIENT_SECRET
}
