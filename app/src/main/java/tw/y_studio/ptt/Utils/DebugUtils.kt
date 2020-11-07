@file:JvmName("DebugUtils")

package tw.y_studio.ptt.Utils

import tw.y_studio.ptt.BuildConfig

const val useApi = true

// TODO maybe rename to d?
fun Log(title: String, message: String) {
    if (BuildConfig.DEBUG) {
        android.util.Log.d(title, message)
    }
}

fun e(title: String, message: String) {
    if (BuildConfig.DEBUG) {
        android.util.Log.e(title, message)
    }
}
