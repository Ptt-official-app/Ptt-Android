package cc.ptt.android.common.ptt

data class AidBean(
    val boardName: String? = null,
    val aid: String? = null,
) {
    fun isEmpty(): Boolean = boardName.isNullOrEmpty() || aid.isNullOrEmpty()

    fun toUrl(): String = AidConverter.aidToUrl(this)

    companion object {
        fun parse(url: String): AidBean = AidConverter.urlToAid(url)
    }
}
