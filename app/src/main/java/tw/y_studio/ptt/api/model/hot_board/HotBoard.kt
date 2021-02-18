package tw.y_studio.ptt.api.model.hot_board

import com.google.gson.annotations.SerializedName

data class HotBoard(
    @SerializedName("list")
    val list: List<Board>,
    @SerializedName("next_idx")
    val nextId: String
)

data class Board(
    @SerializedName("bid")
    val boardId: String,
    @SerializedName("brdname")
    val boardName: String,
    @SerializedName("class")
    val boardClass: String,
    @SerializedName("flag")
    val boardAttributes: Int,
    @SerializedName("last_post_time")
    val lastPostTime: Int,
    @SerializedName("moderators")
    val moderators: List<String>,
    @SerializedName("nuser")
    val onlineUser: Int,
    @SerializedName("read")
    val read: Boolean,
    @SerializedName("reason")
    val reason: String,
    @SerializedName("stat_attr")
    val stateAttributes: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("total")
    val totalArticles: Int,
    @SerializedName("type")
    val type: String
)
