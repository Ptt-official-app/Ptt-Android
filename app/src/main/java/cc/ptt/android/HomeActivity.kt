package cc.ptt.android

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import cc.ptt.android.base.BaseActivity
import cc.ptt.android.common.KeyboardUtils
import cc.ptt.android.common.StaticValue
import cc.ptt.android.data.preference.MainPreferences
import org.koin.android.ext.android.inject
import java.util.*
import kotlin.math.abs

class HomeActivity : BaseActivity() {

    private val mainPreferences: MainPreferences by inject()
    private var themeType = 0
    private var timeTemp: Long = 0
    private var isReadyLaunch = false

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        themeType = mainPreferences.getThemeType()
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
        StaticValue.backgroundColor = ContextCompat.getColor(this, R.color.darkGreyTwo)
        onBackPressedDispatcher.addCallback(backPressedCallback)
        isReadyLaunch = true
    }

    override fun onPause() {
        super.onPause()
        KeyboardUtils.hideSoftInput(this)
    }

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (Navigation.isRoot(this@HomeActivity)) {
                Navigation.popup(this@HomeActivity)
            } else {
                val currentTime = System.currentTimeMillis()
                if (abs(timeTemp - currentTime) > 1500) {
                    Toast.makeText(this@HomeActivity, getString(R.string.press_again_to_leave), Toast.LENGTH_SHORT).show()
                    timeTemp = currentTime
                } else {
                    finish()
                    System.gc()
                }
            }
        }
    }
}
