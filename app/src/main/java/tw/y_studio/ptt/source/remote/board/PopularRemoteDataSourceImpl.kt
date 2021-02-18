package tw.y_studio.ptt.source.remote.board

import tw.y_studio.ptt.api.board.BoardApiService
import tw.y_studio.ptt.api.model.hot_board.HotBoard

class PopularRemoteDataSourceImpl(
    private val boardApiService: BoardApiService
) : IPopularRemoteDataSource {
    override suspend fun getPopularBoards(): HotBoard {
        return boardApiService.getPopularBoard()
    }
}
