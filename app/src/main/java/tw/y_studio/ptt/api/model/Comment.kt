package tw.y_studio.ptt.api.model

data class Comment(
    val userid: String,
    val tag: String,
    val content: String,
    val ip: String,
    val date: String
)
