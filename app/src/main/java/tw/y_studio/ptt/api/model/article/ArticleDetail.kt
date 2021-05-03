package tw.y_studio.ptt.api.model.article
import com.google.gson.annotations.SerializedName

data class ArticleDetail(
    @SerializedName("aid")
    val articleId: String,
    @SerializedName("bbs")
    val bbs: String,
    @SerializedName("bid")
    val boardId: String,
    @SerializedName("brdname")
    val boardName: String,
    @SerializedName("class")
    val classX: String,
    @SerializedName("content")
    val content: List<List<Content>>,
    @SerializedName("create_time")
    val createTime: Int,
    @SerializedName("deleted")
    val deleted: Boolean,
    @SerializedName("host")
    val host: String,
    @SerializedName("ip")
    val ip: String,
    @SerializedName("mode")
    val mode: Int,
    @SerializedName("modified")
    val modified: Int,
    @SerializedName("money")
    val money: Int,
    @SerializedName("n_comments")
    val nComments: Int,
    @SerializedName("owner")
    val owner: String,
    @SerializedName("read")
    val read: Boolean,
    @SerializedName("recommend")
    val recommend: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("url")
    val url: String
)
