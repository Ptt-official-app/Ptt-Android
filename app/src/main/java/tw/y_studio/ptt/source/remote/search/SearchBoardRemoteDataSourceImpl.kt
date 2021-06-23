package tw.y_studio.ptt.source.remote.search

import tw.y_studio.ptt.api.board.BoardApiService
import tw.y_studio.ptt.api.model.board.hot_board.HotBoard

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
