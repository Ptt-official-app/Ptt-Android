package cc.ptt.android.data.model.remote.board.searchboard

data class SearchBoardsItem(
    val boardId: String = "",
    val number: Int = 0,
    val title: String = "",
    val subtitle: String = "",
    val boardType: Int = 0,
    val like: Boolean = false,
    val moderators: String = "",
    val _class: String = "",
    val online: Int = 0,
    val onlineColor: Int = 7
)
