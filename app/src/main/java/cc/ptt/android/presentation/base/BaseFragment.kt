package cc.ptt.android.presentation.base

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import cc.ptt.android.common.utils.log

open class BaseFragment : Fragment() {

    protected val TAG get() = this::class.java.simpleName

    private val mUIHandler = Handler(Looper.getMainLooper())
    private var isFirstStart = false
    val currentActivity: Activity get() = requireActivity()
    protected val currentFragment: Fragment get() = this

    fun <T : View?> findViewById(id: Int): T? {
        return view?.findViewById<View>(id) as T
    }

    protected open fun onAnimOver() {
        log(TAG, "onAnimOver")
    }

    override fun onResume() {
        super.onResume()
        if (!isFirstStart) {
            onAnimOver()
            isFirstStart = true
        }
    }

    protected fun runOnUI(r: Runnable) {
        mUIHandler.post(r)
    }

    fun loadFragment(toFragment: Fragment?, thisFragment: Fragment?) {
        try {
            (currentActivity as? BaseActivity)?.loadFragment(toFragment, thisFragment)
        } catch (e: Exception) {
            e.printStackTrace()
            log("loadFragment", "Error : " + e.localizedMessage)
        }
    }

    fun loadFragmentNoAnim(toFragment: Fragment?, thisFragment: Fragment?) {
        try {
            (currentActivity as? BaseActivity)?.loadFragmentNoAnim(toFragment, thisFragment)
        } catch (e: Exception) {
            e.printStackTrace()
            log("loadFragmentNoAnim", "Error : " + e.localizedMessage)
        }
    }
}
