package tw.y_studio.ptt.network.api

import android.content.Context
import okhttp3.OkHttpClient
import tw.y_studio.ptt.BuildConfig
import tw.y_studio.ptt.utils.OkHttpUtils

abstract class BaseAPIHelper : IBaseAPI {
    constructor() {
        okHttpClient = OkHttpUtils().trustAllClient
    }
    constructor(context: Context?) {
        okHttpClient = OkHttpUtils().trustAllClient
    }

    @JvmField
    protected val hostUrl = BuildConfig.API_Domain

    protected val okHttpClient: OkHttpClient?
    override fun close() {
        okHttpClient?.dispatcher?.cancelAll()
    }
}
