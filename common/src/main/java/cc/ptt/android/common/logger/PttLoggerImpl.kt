package cc.ptt.android.common.logger

import android.util.Log
import cc.ptt.android.common.BuildConfig

class PttLoggerImpl : PttLogger {

    override fun d(tag: String?, msg: String?, t: Throwable?) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, msg, t)
        }
    }

    override fun i(tag: String?, msg: String?, t: Throwable?) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, msg, t)
        }
    }

    override fun w(tag: String?, msg: String?, t: Throwable?) {
        if (BuildConfig.DEBUG) {
            Log.w(tag, msg, t)
        }
    }

    override fun e(tag: String?, msg: String?, t: Throwable?) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, msg, t)
        }
    }
}
