package cc.ptt.android.data.model.remote.article

import com.google.gson.annotations.SerializedName

data class ArticleComment(
    @SerializedName("aid")
    val articleId: String,
    @SerializedName("bid")
    val boardId: String,
    @SerializedName("cid")
    val commentId: String,
    @SerializedName("content")
    val content: List<List<Content>>?,
    @SerializedName("create_time")
    val createTime: Int,
    @SerializedName("deleted")
    val deleted: Boolean,
    @SerializedName("host")
    val host: String,
    @SerializedName("ip")
    val ip: String,
    @SerializedName("owner")
    val owner: String,
    /**
     * 是在回應哪個 comment. 第 0 層是 ''
     */
    @SerializedName("refid")
    val refId: String,
    /**
     * 推/噓/→
     */
    @SerializedName("type")
    val type: Int
) {
    val articleCommentType get() = ArticleCommentType.parse(
        type
    )
}

enum class ArticleCommentType(val value: Int) {
    PUSH(1),
    HUSH(2),
    COMMENT(3);

    companion object {
        fun parse(value: Int): ArticleCommentType {
            return values().find { it.value == value } ?: COMMENT
        }
    }
}
