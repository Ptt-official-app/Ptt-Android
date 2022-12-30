package cc.ptt.android.data.source.remote.search

import cc.ptt.android.data.apiservices.board.BoardApi
import cc.ptt.android.data.model.remote.board.hotboard.BoardList
import kotlinx.coroutines.flow.Flow

class SearchBoardRemoteDataSourceImpl constructor (
    private val boardApi: BoardApi
) : SearchBoardRemoteDataSource {

    override fun searchBoardByKeyword(
        keyword: String,
        startIndex: String,
        limit: Int,
        aces: Boolean
    ): Flow<BoardList> {
        return boardApi.searchBoards(
            keyword = keyword,
            start_idx = startIndex,
            limit = limit,
            asc = aces
        )
    }
}
