package tw.y_studio.ptt.source.remote.board

import tw.y_studio.ptt.api.model.hot_board.HotBoard

interface IPopularRemoteDataSource {
    suspend fun getPopularBoards(): HotBoard
}
