@file:JvmName("DebugUtils")

package cc.ptt.android.common.utils

import cc.ptt.android.BuildConfig

const val useApi = true

fun Log(title: String, message: String) {
    if (BuildConfig.DEBUG) {
        android.util.Log.d(title, message)
    }
}
