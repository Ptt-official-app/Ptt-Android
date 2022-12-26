package cc.ptt.android.data.model.remote.article.hotarticle

import com.google.gson.annotations.SerializedName

data class HotArticleList(
    @SerializedName("list")
    var list: ArrayList<HotArticle>,
    @SerializedName("next_idx")
    var nextIdx: String
)
