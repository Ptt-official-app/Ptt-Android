package tw.y_studio.ptt.source.remote.favorite

import tw.y_studio.ptt.api.board.BoardApiService
import tw.y_studio.ptt.api.model.board.hot_board.HotBoard

class IFavoriteRemoteDataSourceImpl(
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
