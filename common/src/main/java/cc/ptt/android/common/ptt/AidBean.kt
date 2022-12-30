package cc.ptt.android.common.ptt

data class AidBean(val boardName: String? = null, val aid: String? = null) {
    fun isEmpty(): Boolean {
        return boardName.isNullOrEmpty() || aid.isNullOrEmpty()
    }

    fun toUrl(): String {
        return AidConverter.aidToUrl(this)
    }

    companion object {
        fun parse(url: String): AidBean {
            return AidConverter.urlToAid(url)
        }
    }
}
