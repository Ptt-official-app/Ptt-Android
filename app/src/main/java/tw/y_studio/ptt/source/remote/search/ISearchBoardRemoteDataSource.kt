package tw.y_studio.ptt.source.remote.search

import tw.y_studio.ptt.api.model.board.hot_board.HotBoard

interface ISearchBoardRemoteDataSource {
    suspend fun searchBoardByKeyword(
        keyword: String,
        startIndex: String = "",
        limit: Int = 200,
        aces: Boolean = true
    ): HotBoard
}
