package cc.ptt.android.common

import android.content.Context
import android.util.TypedValue
import androidx.annotation.ColorInt

object ResourcesUtils {
    @ColorInt
    fun getColor(context: Context, attrResId: Int): Int {
        val typedValue = TypedValue()
        val theme = context.theme
        theme.resolveAttribute(attrResId, typedValue, true)
        return typedValue.data
    }
}
