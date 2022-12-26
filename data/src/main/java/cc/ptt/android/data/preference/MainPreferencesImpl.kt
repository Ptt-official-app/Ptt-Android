package cc.ptt.android.data.preference

import android.content.Context
import android.content.SharedPreferences

class MainPreferencesImpl constructor(
    private val context: Context
) : MainPreferences {

    private val preference: SharedPreferences by lazy { context.getSharedPreferences(KEY_FOR_PREFERENCE, Context.MODE_PRIVATE) }

    override fun setApiDomain(text: String?) {
        text?.let {
            val editor = preference.edit()
            editor.putString(KEY_FOR_API_DOMAIN, it)
            editor.apply()
            editor.commit()
        } ?: run {
            val editor = preference.edit()
            editor.remove(KEY_FOR_API_DOMAIN)
            editor.apply()
            editor.commit()
        }
    }

    override fun getApiDomain(): String {
        return preference.getString(KEY_FOR_API_DOMAIN, "").orEmpty()
    }

    override fun setThemeType(type: Int) {
        val editor = preference.edit()
        editor.putInt(KEY_FOR_APP_THEME, type)
        editor.apply()
    }

    override fun getThemeType(): Int {
        return preference.getInt(KEY_FOR_APP_THEME, VALUE_FOR_DEFAULT_APP_THEME)
    }

    override fun setSearchStyle(type: Int) {
        val editor = preference.edit()
        editor.putInt(KEY_FOR_APP_SEARCH_STYLE, type)
        editor.apply()
    }

    override fun getSearchStyle(): Int {
        return preference.getInt(KEY_FOR_APP_SEARCH_STYLE, VALUE_FOR_DEFAULT_APP_SEARCH_STYLE)
    }

    override fun setPostBottomStyle(type: Int) {
        val editor = preference.edit()
        editor.putInt(KEY_FOR_APP_POST_BOTTOM_STYLE, type)
        editor.apply()
    }

    override fun getPostBottomStyle(): Int {
        return preference.getInt(KEY_FOR_APP_POST_BOTTOM_STYLE, VALUE_FOR_DEFAULT_APP_POST_BOTTOM_STYLE)
    }

    companion object {
        const val KEY_FOR_PREFERENCE = "main_preferences"
        const val KEY_FOR_API_DOMAIN = "key_for_api_domain"
        const val KEY_FOR_APP_THEME = "key_for_app_theme"
        const val KEY_FOR_APP_SEARCH_STYLE = "key_for_search_style"
        const val KEY_FOR_APP_POST_BOTTOM_STYLE = "key_for_post_bottom_style"

        const val VALUE_FOR_DEFAULT_APP_THEME: Int = 0
        const val VALUE_FOR_DEFAULT_APP_SEARCH_STYLE: Int = 0
        const val VALUE_FOR_DEFAULT_APP_POST_BOTTOM_STYLE: Int = 0
    }
}
