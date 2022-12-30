package cc.ptt.android.data.model.remote.article
import com.google.gson.annotations.SerializedName

data class ArticleCommentsList(
    @SerializedName("list")
    val list: List<cc.ptt.android.data.model.remote.article.ArticleComment>,
    @SerializedName("next_idx")
    val nextIndex: String
)
