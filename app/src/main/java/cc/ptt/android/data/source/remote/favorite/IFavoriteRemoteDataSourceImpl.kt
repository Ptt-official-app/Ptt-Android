package cc.ptt.android.data.source.remote.favorite

import cc.ptt.android.data.api.board.BoardApiService
import cc.ptt.android.data.model.remote.board.hotboard.HotBoard
import javax.inject.Inject

class IFavoriteRemoteDataSourceImpl @Inject constructor(
    private val boardApiService: BoardApiService
) : IFavoriteRemoteDataSource {
    override suspend fun getFavoriteBoards(
        userid: String,
        level_idx: String,
        startIndex: String,
        limit: Int,
        aces: Boolean
    ): HotBoard {
        return boardApiService.favoriteBoards(userid, level_idx, startIndex, limit, aces)
    }
}
