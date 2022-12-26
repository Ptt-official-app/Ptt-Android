package cc.ptt.android.base

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import cc.ptt.android.common.logger.PttLogger
import org.koin.android.ext.android.inject

open class BaseFragment : Fragment() {

    protected val TAG get() = this::class.java.simpleName

    private val mUIHandler = Handler(Looper.getMainLooper())
    private var isFirstStart = false
    protected val logger: PttLogger by inject()
    val currentActivity: Activity get() = requireActivity()
    protected val currentFragment: Fragment get() = this

    fun <T : View?> findViewById(id: Int): T? {
        return view?.findViewById<View>(id) as T
    }

    protected open fun onAnimOver() {
        logger.d(TAG, "onAnimOver")
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
            logger.d("loadFragment", "Error : " + e.localizedMessage)
        }
    }

    fun loadFragmentNoAnim(toFragment: Fragment?, thisFragment: Fragment?) {
        try {
            (currentActivity as? BaseActivity)?.loadFragmentNoAnim(toFragment, thisFragment)
        } catch (e: Exception) {
            logger.d("loadFragmentNoAnim", "Error : " + e.localizedMessage)
        }
    }
}
