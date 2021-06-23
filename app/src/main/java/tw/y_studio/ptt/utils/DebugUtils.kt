@file:JvmName("DebugUtils")

package tw.y_studio.ptt.utils

import tw.y_studio.ptt.BuildConfig

const val useApi = true

fun Log(title: String, message: String) {
    if (BuildConfig.DEBUG) {
        android.util.Log.d(title, message)
    }
}
