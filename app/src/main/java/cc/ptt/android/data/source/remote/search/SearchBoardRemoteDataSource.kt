package cc.ptt.android.data.source.remote.search

import cc.ptt.android.data.model.remote.board.hotboard.BoardList
import kotlinx.coroutines.flow.Flow

interface SearchBoardRemoteDataSource {
    fun searchBoardByKeyword(
        keyword: String,
        startIndex: String = "",
        limit: Int = 200,
        aces: Boolean = true
    ): Flow<BoardList>
}
