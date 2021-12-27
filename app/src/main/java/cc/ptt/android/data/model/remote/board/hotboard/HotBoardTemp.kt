package cc.ptt.android.data.model.remote.board.hotboard

data class HotBoardTemp(
    val number: Int,
    val title: String,
    val subtitle: String,
    val boardType: Int,
    val online: Int,
    val onlineColor: String
)
