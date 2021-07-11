package tw.y_studio.ptt.ui

import java.util.*
import kotlin.math.abs

class ClickFix(private var defaultTime: Long = 500L) {

    private var lastClickTime: Long = 0
    val isFastDoubleClick: Boolean
        get() = isFastDoubleClick(defaultTime)

    fun isFastDoubleClick(time2: Long): Boolean {
        val time = Date().time
        if (abs(time - lastClickTime) < time2) {
            return true
        }
        lastClickTime = time
        return false
    }
}
