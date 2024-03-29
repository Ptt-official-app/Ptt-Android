package cc.ptt.android.data.model.remote

data class Post(
    val title: String,
    val classString: String,
    val date: String,
    val auth: String,
    val authNickName: String,
    val content: String,
    val comments: List<cc.ptt.android.data.model.remote.Comment>
)
