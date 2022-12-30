package cc.ptt.android.common

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager

object KeyboardUtils {

    fun hideSoftInput(activity: Activity) {
        val inputMethodManager: InputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            ?: return
        val view = activity.currentFocus ?: View(activity)
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun showSoftInput(activity: Activity) {
        val inputMethodManager: InputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            ?: return
        val view = activity.currentFocus ?: View(activity)
        inputMethodManager.showSoftInput(view, 0)
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }
}
