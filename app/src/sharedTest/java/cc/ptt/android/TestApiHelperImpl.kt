package cc.ptt.android

import cc.ptt.android.common.apihelper.ApiHelper

class TestApiHelperImpl : ApiHelper {

    override fun getHost(): String {
        return ApiHelper.API_HOST
    }

    override fun getClientId(): String {
        return ApiHelper.CLIENT_ID
    }

    override fun getClientSecret(): String {
        return ApiHelper.CLIENT_SECRET
    }
}
