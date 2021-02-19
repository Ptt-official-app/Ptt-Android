package tw.y_studio.ptt.api.model.board.hot_board

data class HotBoardTemp(
    val number: Int,
    val title: String,
    val subtitle: String,
    val boardType: Int,
    val online: Int,
    val onlineColor: String
)
