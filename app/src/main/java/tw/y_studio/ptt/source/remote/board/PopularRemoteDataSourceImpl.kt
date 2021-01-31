package tw.y_studio.ptt.source.remote.board

import tw.y_studio.ptt.api.PopularBoardListAPI
import tw.y_studio.ptt.api.board.BoardApiService
import tw.y_studio.ptt.api.model.hot_board.HotBoard
import tw.y_studio.ptt.api.model.hot_board.HotBoardTemp

class PopularRemoteDataSourceImpl(
    private val boardApiService: BoardApiService,
    private val popularBoardListAPI: PopularBoardListAPI
) : IPopularRemoteDataSource {

    @Throws(Exception::class)
    override fun getPopularBoardData(page: Int, count: Int): MutableList<HotBoardTemp> {
        return popularBoardListAPI.refresh(page, count)
    }

    override suspend fun getPopularBoards(): HotBoard {
        return boardApiService.getPopularBoard()
    }

    override fun disposeAll() {
        popularBoardListAPI.close()
    }
}
