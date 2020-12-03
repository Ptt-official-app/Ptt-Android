package tw.y_studio.ptt.network.model

data class PartialPost(
    val title: String = "",
    val date: String = "",
    val category: String = "",
    val comments: Int = 0,
    val like: Int = 0,
    val auth: String = "",
    var read: Boolean = false,
    val deleted: Boolean = false,
    val url: String = ""
)
