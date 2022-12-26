package cc.ptt.android.base

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

open class BaseActivity : AppCompatActivity() {
    open fun closeAllFragment() {}

    @Throws(Exception::class)
    open fun loadFragment(toFragment: Fragment?, thisFragment: Fragment?) {
    }

    @Throws(Exception::class)
    open fun loadFragmentNoAnim(toFragment: Fragment?, thisFragment: Fragment?) {
    }
}
