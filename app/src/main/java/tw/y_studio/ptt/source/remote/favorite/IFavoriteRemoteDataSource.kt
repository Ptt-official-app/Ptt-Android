package tw.y_studio.ptt.source.remote.favorite

import tw.y_studio.ptt.api.model.board.hot_board.HotBoard

interface IFavoriteRemoteDataSource {
    suspend fun getFavoriteBoards(
        userid: String = "",
        level_idx: String = "",
        startIndex: String = "",
        limit: Int = 200,
        aces: Boolean = true
    ): HotBoard
}
