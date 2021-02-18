package tw.y_studio.ptt.api.model

data class PartialPost(
    val title: String = "",
    val date: String = "",
    val category: String = "",
    val comments: Int = 0,
    val goup: Int = 0,
    val down: Int = 0,
    val auth: String = "",
    val board: String = "",
    val aid: String = "",
    var read: Boolean = false,
    val deleted: Boolean = false,
    val url: String = ""
)
