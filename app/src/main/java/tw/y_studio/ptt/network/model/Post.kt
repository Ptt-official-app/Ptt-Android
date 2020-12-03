package tw.y_studio.ptt.network.model

data class Post(
    val title: String,
    val classString: String,
    val date: String,
    val auth: String,
    val authNickName: String,
    val content: String,
    val comments: List<Comment>
)
