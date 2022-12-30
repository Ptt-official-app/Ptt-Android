package cc.ptt.android.data.model.remote.board.article
import com.google.gson.annotations.SerializedName

/**
 * Created by Michael.Lien
 * on 2021/2/18
 */
data class ArticleList(
    val list: List<Article>,
    @SerializedName("next_create_time")
    val nextCreateTime: Int,
    @SerializedName("next_idx")
    val nextIndex: String,
    @SerializedName("start_num_idx")
    val startNumIndex: Int
)
