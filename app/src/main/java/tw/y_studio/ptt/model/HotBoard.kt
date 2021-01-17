package tw.y_studio.ptt.model

data class HotBoard(
    val number: Int,
    val title: String,
    val subtitle: String,
    val boardType: Int,
    val online: Int,
    val onlineColor: String
)
