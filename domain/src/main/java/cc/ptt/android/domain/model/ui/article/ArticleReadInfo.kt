package cc.ptt.android.domain.model.ui.article

import android.text.SpannableStringBuilder

sealed class ArticleReadInfo {
    data class HeaderInfo(
        val title: String,
        val auth: String,
        val date: String,
        val type: String,
        val board: String
    ) : ArticleReadInfo()

    data class ContentLineInfo(val text: String, val richText: SpannableStringBuilder) : ArticleReadInfo()
    data class ImageInfo(val index: Int, val url: String) : ArticleReadInfo()
    data class CenterBarInfo(val like: String, val floor: String) : ArticleReadInfo()
    data class CommentInfo(val index: Int, val text: String, val auth: String) : ArticleReadInfo()
    data class CommentBarInfo(val index: Int, val time: String, val floor: String, val like: String) : ArticleReadInfo()
}
