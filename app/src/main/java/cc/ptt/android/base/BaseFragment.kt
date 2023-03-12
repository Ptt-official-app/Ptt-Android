package cc.ptt.android.base

import android.view.View
import androidx.fragment.app.Fragment
import cc.ptt.android.common.KeyboardUtils
import cc.ptt.android.common.logger.PttLogger
import org.koin.android.ext.android.inject

open class BaseFragment : Fragment() {

    protected val TAG get() = this::class.java.simpleName

    private var isFirstStart = false
    protected val logger: PttLogger by inject()

    @Deprecated("", ReplaceWith("", ""))
    fun <T : View?> findViewById(id: Int): T? {
        return view?.findViewById<View>(id) as T
    }

    protected open fun onAnimFinished() {
        logger.d(TAG, "onAnimOver")
    }

    override fun onResume() {
        super.onResume()
        if (!isFirstStart) {
            onAnimFinished()
            isFirstStart = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideSoftInput()
    }

    protected fun hideSoftInput() {
        KeyboardUtils.hideSoftInput(requireActivity())
    }
}
