package cc.ptt.android.common.apihelper

interface ApiHelper {
    fun getHost(): String
    fun getClientId(): String
    fun getClientSecret(): String

    companion object {
        const val CLIENT_ID = "test_client_id"
        const val CLIENT_SECRET = "test_client_secret"
    }
}
