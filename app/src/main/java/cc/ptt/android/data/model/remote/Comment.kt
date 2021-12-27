package cc.ptt.android.data.model.remote

data class Comment(
    val userid: String,
    val tag: String,
    val content: String,
    val ip: String,
    val date: String
)
