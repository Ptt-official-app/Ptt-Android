package cc.ptt.android.data.source.remote.search

import cc.ptt.android.data.api.board.BoardApiService
import cc.ptt.android.data.model.remote.board.hotboard.HotBoard

class SearchBoardRemoteDataSourceImpl(
    private val boardApiService: BoardApiService
) : ISearchBoardRemoteDataSource {

    override suspend fun searchBoardByKeyword(
        keyword: String,
        startIndex: String,
        limit: Int,
        aces: Boolean
    ): HotBoard {
        return boardApiService.searchBoards(
            keyword = keyword,
            start_idx = startIndex,
            limit = limit,
            asc = aces
        )
    }
}
