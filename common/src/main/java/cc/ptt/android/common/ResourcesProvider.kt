package cc.ptt.android.common

import android.content.Context

class ResourcesProvider(private val context: Context) {

    fun getString(resTd: Int): String {
        return context.getString(resTd)
    }
}
