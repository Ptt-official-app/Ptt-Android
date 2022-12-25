package cc.ptt.android.data.source.remote.favorite

import cc.ptt.android.data.model.remote.board.hotboard.BoardList
import kotlinx.coroutines.flow.Flow

interface FavoriteRemoteDataSource {
    suspend fun getFavoriteBoards(
        userid: String = "",
        level_idx: String = "",
        startIndex: String = "",
        limit: Int = 200,
        aces: Boolean = true
    ): Flow<BoardList>
}
