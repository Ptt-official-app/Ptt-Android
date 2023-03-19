package cc.ptt.android.data.model.remote.article

import com.google.gson.annotations.SerializedName

data class Content(
    @SerializedName("color0")
    val color0: Color,
    @SerializedName("color1")
    val color1: Color,
    @SerializedName("text")
    val text: String
)
