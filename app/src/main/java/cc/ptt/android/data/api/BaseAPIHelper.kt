package cc.ptt.android.data.api

import android.content.Context
import cc.ptt.android.BuildConfig
import cc.ptt.android.common.utils.OkHttpUtils
import okhttp3.OkHttpClient

abstract class BaseAPIHelper : IBaseAPI {
    constructor() {
        okHttpClient = OkHttpUtils().trustAllClient
    }
    constructor(context: Context?) {
        okHttpClient = OkHttpUtils().trustAllClient
    }

    @JvmField
    protected val hostUrl = BuildConfig.APIDomain

    protected val okHttpClient: OkHttpClient?
    override fun close() {
        okHttpClient?.dispatcher?.cancelAll()
    }
}
