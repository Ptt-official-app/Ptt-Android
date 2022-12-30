package cc.ptt.android.data

import cc.ptt.android.common.network.api.apihelper.ApiHelper

class TestApiHelperImpl : ApiHelper {

    override fun getHost(): String {
        return BuildConfig.API_HOST.ifBlank {
            ApiHelper.API_HOST
        }
    }

    override fun getClientId(): String {
        return ApiHelper.CLIENT_ID
    }

    override fun getClientSecret(): String {
        return ApiHelper.CLIENT_SECRET
    }
}
