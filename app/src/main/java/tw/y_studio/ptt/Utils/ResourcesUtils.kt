package tw.y_studio.ptt.Utils

import android.content.Context
import android.util.TypedValue
import androidx.annotation.ColorInt

@ColorInt
fun getColor(context: Context, attrResId: Int): Int {
    val typedValue = TypedValue()
    val theme = context.theme
    theme.resolveAttribute(attrResId, typedValue, true)
    return typedValue.data
}
