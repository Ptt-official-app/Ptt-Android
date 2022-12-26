package cc.ptt.android.data.repository.search

import cc.ptt.android.data.model.remote.board.hotboard.BoardList
import kotlinx.coroutines.flow.Flow

interface SearchBoardRepository {
    fun searchBoardByKeyword(
        keyword: String,
        startIndex: String = "",
        limit: Int = 200,
        aces: Boolean = true
    ): Flow<BoardList>
}
