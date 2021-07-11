package tw.y_studio.ptt

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import tw.y_studio.ptt.fragment.*
import tw.y_studio.ptt.fragment.login.LoginPageFragment
import tw.y_studio.ptt.fragment.search_boards.SearchBoardsFragment
import tw.y_studio.ptt.ui.BaseActivity
import tw.y_studio.ptt.ui.StaticValue
import tw.y_studio.ptt.ui.article.list.ArticleListFragment
import tw.y_studio.ptt.ui.article.list.search.ArticleListSearchFragment
import tw.y_studio.ptt.ui.article.read.ArticleReadFragment
import tw.y_studio.ptt.ui.common.extension.navigateForward
import tw.y_studio.ptt.utils.PreferenceConstants
import java.util.*
import kotlin.math.abs

class HomeActivity : BaseActivity() {
    private var themeType = 0
    private var timeTemp: Long = 0
    private var navController: NavController? = null

    var fragmentTouchListener: FragmentTouchListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        val preference2 = getSharedPreferences(PreferenceConstants.prefName, MODE_PRIVATE)
        themeType = preference2.getInt(PreferenceConstants.theme, 0)
        StaticValue.ThemMode = themeType
        when (themeType) {
            1 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            0 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            else -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
        }
        val typedValue = TypedValue()
        val theme = theme
        theme.resolveAttribute(R.attr.black, typedValue, true)
        @ColorInt val color = typedValue.data
        val window = this.window
        window.statusBarColor = color
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        if (themeType == 0) {
            window.decorView.systemUiVisibility = 0
        } else if (themeType == 1) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        setContentView(R.layout.activity_main)
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        StaticValue.densityDpi = metrics.densityDpi.toDouble()
        StaticValue.ScreenDensity = metrics.density.toDouble()
        StaticValue.widthPixels = metrics.widthPixels.toDouble()
        StaticValue.highPixels = metrics.heightPixels.toDouble()
        /**
         * TODO when refactor to Kotlin we can declare: private val navController by lazy {
         * (supportFragmentManager.findFragmentById( R.id.nav_host_fragment) as
         * NavHostFragment).navController }
         */
        navController = (
            Objects.requireNonNull(
                supportFragmentManager
                    .findFragmentById(R.id.nav_host_fragment)
            ) as NavHostFragment
            )
            .navController
        isReadyLaunch = true
    }

    private var isReadyLaunch = false
    override fun closeAllFragment() {
        try {
            while (supportFragmentManager.backStackEntryCount > 0) {
                val backStackId = supportFragmentManager
                    .getBackStackEntryAt(
                        supportFragmentManager.backStackEntryCount - 1
                    )
                    .name
                supportFragmentManager
                    .popBackStackImmediate(
                        backStackId, FragmentManager.POP_BACK_STACK_INCLUSIVE
                    )
            }
        } catch (e: Exception) {
        }
    }

    @Throws(Exception::class)
    override fun loadFragment(toFragment: Fragment?, thisFragment: Fragment?) {
        var id = 0
        when (toFragment) {
            is LoginPageFragment -> {
                id = R.id.include_login
            }
            is ArticleListFragment -> {
                id = R.id.articleListFragment
            }
            is ArticleReadFragment -> {
                id = R.id.articleReadFragment
            }
            is ArticleListSearchFragment -> {
                id = R.id.articleListSearchFragment
            }
            is PostArticleFragment -> {
                id = R.id.postArticleFragment
            }
        }
        navController?.navigateForward(id, toFragment?.arguments, isSingleTop = false, useDefaultAnim = true)
    }

    @Throws(Exception::class)
    override fun loadFragmentNoAnim(toFragment: Fragment?, thisFragment: Fragment?) {
        var id = 0
        if (toFragment is SearchBoardsFragment) {
            id = R.id.searchBoardsFragment
        } else if (toFragment is HotArticleFilterFragment) {
            id = R.id.hotArticleFilterFragment
        }
        navController?.navigateForward(id, toFragment?.arguments, isSingleTop = false, useDefaultAnim = false)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK &&
            event.repeatCount == 0
        ) { // 確定按下退出鍵and防止重複按下退出鍵
            // TODO refactor this check
            if (navController
                ?.currentDestination
                ?.label != HomeFragment::class.java.simpleName
            ) {
                onBackPressed()
            } else {
                val myDate = Date()
                val myDate2 = myDate.time
                if (abs(timeTemp - myDate2) > 1500) {
                    Toast.makeText(
                        this,
                        getString(R.string.press_again_to_leave),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    timeTemp = myDate2
                } else {
                    finish()
                    System.gc()
                }
            }
        } else {
            super.onKeyDown(keyCode, event)
        }
        return false
    }

    public override fun onPause() {
        super.onPause()
        try {
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        } catch (e: Exception) {
        }
    }

    public override fun onDestroy() {
        super.onDestroy()
    }

    public override fun onResume() {
        super.onResume()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return ev?.let {
            fragmentTouchListener?.onTouchEvent(it, super.dispatchTouchEvent(it))
        } ?: super.dispatchTouchEvent(ev)
    }
}

interface FragmentTouchListener {
    fun onTouchEvent(event: MotionEvent, defaultTouchEvent: Boolean): Boolean
}
