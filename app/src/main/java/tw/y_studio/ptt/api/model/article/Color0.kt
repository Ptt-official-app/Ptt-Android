package tw.y_studio.ptt.api.model.article

import com.google.gson.annotations.SerializedName
import tw.y_studio.ptt.R

data class Color0(
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
        get() = when (background) {
            40 -> R.color.black
            41 -> R.color.coral
            42 -> R.color.darkMint
            43 -> R.color.dandelion
            44 -> R.color.deepSkyBlue
            45 -> R.color.reddishPink
            46 -> R.color.robinSEgg
            47 -> R.color.paleGrey
            else -> throw IllegalStateException()
        }
    val foregroundColor: Int
        get() = when (background) {
            30 -> R.color.black
            31 -> R.color.coral
            32 -> R.color.darkMint
            33 -> R.color.dandelion
            34 -> R.color.deepSkyBlue
            35 -> R.color.reddishPink
            36 -> R.color.robinSEgg
            37 -> R.color.paleGrey
            else -> throw IllegalStateException()
        }
}
