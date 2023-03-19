package cc.ptt.android.data.model.remote.article

import cc.ptt.android.common.ptt.PttColor
import com.google.gson.annotations.SerializedName

data class Color(
    @SerializedName("background")
    val background: Int,
    @SerializedName("blink")
    val blink: Boolean,
    @SerializedName("foreground")
    val foreground: Int,
    @SerializedName("highlight")
    val highlight: Boolean,
    @SerializedName("reset")
    val reset: Boolean
) {

    val backgroundColor: Int
        get() = PttColor.backgroundColor(background)

    val foregroundColor: Int
        get() = PttColor.foregroundColor(foreground, highlight)
}
