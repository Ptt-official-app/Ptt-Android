package cc.ptt.android.data.model.remote.article.hotarticle

import com.google.gson.annotations.SerializedName

data class HotArticle(
    @SerializedName("aid")
    var aid: String,
    @SerializedName("bid")
    var bid: String,
    @SerializedName("deleted")
    var deleted: Boolean,
    @SerializedName("filename")
    var filename: String,
    @SerializedName("create_time")
    var createTime: Int,
    @SerializedName("modified")
    var modified: Int,
    @SerializedName("recommend")
    var recommend: Int,
    @SerializedName("n_comments")
    var nComments: Int,
    @SerializedName("owner")
    var owner: String,
    @SerializedName("date")
    var date: String,
    @SerializedName("title")
    var title: String,
    @SerializedName("money")
    var money: Int,
    @SerializedName("type")
    var type: String,
    @SerializedName("class")
    var `class`: String,
    @SerializedName("mode")
    var mode: Int,
    @SerializedName("url")
    var url: String,
    @SerializedName("read")
    var read: Boolean,
    @SerializedName("idx")
    var idx: String,
    @SerializedName("rank")
    var rank: Int,
    @SerializedName("subject_type")
    var subjectType: String
)
