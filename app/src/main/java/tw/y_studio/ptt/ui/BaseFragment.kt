package tw.y_studio.ptt.ui

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import tw.y_studio.ptt.utils.Log

open class BaseFragment : Fragment() {

    private var _mContext: Context? = null
    protected val mContext get() = _mContext!!

    private var mActivity: BaseActivity? = null

    private var _mainView: View? = null
    protected val mainView get() = _mainView!!

    private val mUIHandler = Handler(Looper.getMainLooper())

    fun setActivity(activity: BaseActivity?) {
        mActivity = activity
    }

    fun setMainView(view: View) {
        _mainView = view
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState).apply {
            _mainView = this
        }
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        if (activity is BaseActivity) {
            mActivity = activity
        }
    }

    fun <T : View?> findViewById(id: Int): T {
        return mainView.findViewById<View>(id) as T
    }

    protected open fun onAnimOver() {}
    protected fun runOnUI(r: Runnable) {
        mUIHandler.post(r)
    }

    private var isFirstStart = false
    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        return if (isFirstStart) super.onCreateAnimation(transit, enter, nextAnim) else try {
            val anim = AnimationUtils.loadAnimation(mActivity, nextAnim)
            anim.setAnimationListener(
                object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation) {}
                    override fun onAnimationRepeat(animation: Animation) {}
                    override fun onAnimationEnd(animation: Animation) {
                        isFirstStart = true
                        onAnimOver()
                        animation.setAnimationListener(null)
                    }
                })
            anim
        } catch (e: Exception) {
            onAnimOver()
            isFirstStart = true
            super.onCreateAnimation(transit, enter, nextAnim)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        _mContext = context
    }

    override fun onDetach() {
        super.onDetach()
        _mContext = null
        mActivity = null
        _mainView = null
    }

    override fun getContext(): Context {
        return if (_mContext != null) {
            _mContext!!
        } else if (mActivity != null) {
            mActivity!!
        } else {
            mainView.context!!
        }
    }

    val currentActivity: Activity
        get() = requireActivity()

    fun closeAllFragment() {
        mActivity?.closeAllFragment()
    }

    protected val currentFragment: Fragment get() = this

    fun loadFragment(toFragment: Fragment?, thisFragment: Fragment?) {
        try {
            mActivity?.loadFragment(toFragment, thisFragment)
        } catch (e: Exception) {
            e.printStackTrace()
            Log("loadFragment", "Error : " + e.localizedMessage)
        }
    }

    fun loadFragmentNoAnim(toFragment: Fragment?, thisFragment: Fragment?) {
        try {
            mActivity?.loadFragmentNoAnim(toFragment, thisFragment)
        } catch (e: Exception) {
            e.printStackTrace()
            Log("loadFragmentNoAnim", "Error : " + e.localizedMessage)
        }
    }
}
