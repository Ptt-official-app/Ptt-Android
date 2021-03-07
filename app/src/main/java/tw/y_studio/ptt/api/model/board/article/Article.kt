package tw.y_studio.ptt.api.model.board.article

import com.google.gson.annotations.SerializedName

/**
 * Created by Michael.Lien
 * on 2021/2/18
 */
data class Article(
    @SerializedName("aid")
    val articleId: String,
    @SerializedName("bid")
    val boardId: String,
    @SerializedName("class")
    val classX: String,
    @SerializedName("create_time")
    val createTime: Int,
    val deleted: Boolean,
    @SerializedName("idx")
    val index: String,
    val mode: Int,
    val modified: Int,
    val money: Int,
    @SerializedName("n_comments")
    val nComments: Int,
    val owner: String,
    var read: Boolean,
    val recommend: Int,
    val title: String,
    val url: String
)
