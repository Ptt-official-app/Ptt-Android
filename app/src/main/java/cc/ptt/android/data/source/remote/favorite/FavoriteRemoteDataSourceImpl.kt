package cc.ptt.android.data.source.remote.favorite

import cc.ptt.android.data.api.board.BoardApi
import cc.ptt.android.data.model.remote.board.hotboard.BoardList
import kotlinx.coroutines.flow.Flow

class FavoriteRemoteDataSourceImpl constructor(
    private val boardApi: BoardApi
) : FavoriteRemoteDataSource {
    override suspend fun getFavoriteBoards(
        userid: String,
        level_idx: String,
        startIndex: String,
        limit: Int,
        aces: Boolean
    ): Flow<BoardList> {
        return boardApi.favoriteBoards(userid, level_idx, startIndex, limit, aces)
    }
}
