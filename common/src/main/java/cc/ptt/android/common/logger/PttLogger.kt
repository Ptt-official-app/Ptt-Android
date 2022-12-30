package cc.ptt.android.common.logger

interface PttLogger {
    companion object {
        val TAG: String = PttLogger::class.java.simpleName
    }

    fun d(tag: String? = TAG, msg: String? = null, t: Throwable? = null)
    fun i(tag: String? = TAG, msg: String? = null, t: Throwable? = null)
    fun w(tag: String? = TAG, msg: String? = null, t: Throwable? = null)
    fun e(tag: String? = TAG, msg: String? = null, t: Throwable? = null)
}
