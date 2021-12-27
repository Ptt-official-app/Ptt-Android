package cc.ptt.android.data.source.remote.search

import cc.ptt.android.data.model.remote.board.hotboard.HotBoard

interface ISearchBoardRemoteDataSource {
    suspend fun searchBoardByKeyword(
        keyword: String,
        startIndex: String = "",
        limit: Int = 200,
        aces: Boolean = true
    ): HotBoard
}
