@file:JvmName("DebugUtils")

package cc.ptt.android.common.utils

import cc.ptt.android.BuildConfig

fun log(title: String, message: String) {
    if (BuildConfig.DEBUG) {
        android.util.Log.d(title, message)
    }
}
