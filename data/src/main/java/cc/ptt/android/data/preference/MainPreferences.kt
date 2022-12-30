package cc.ptt.android.data.preference

interface MainPreferences {
    fun setApiDomain(text: String?)
    fun getApiDomain(): String

    fun setThemeType(type: Int)
    fun getThemeType(): Int

    fun setSearchStyle(type: Int)
    fun getSearchStyle(): Int

    fun setPostBottomStyle(type: Int)
    fun getPostBottomStyle(): Int
}
