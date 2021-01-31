package tw.y_studio.ptt.source.remote.board

import tw.y_studio.ptt.api.model.hot_board.HotBoard
import tw.y_studio.ptt.api.model.hot_board.HotBoardTemp

interface IPopularRemoteDataSource {

    fun getPopularBoardData(page: Int, count: Int): MutableList<HotBoardTemp>

    suspend fun getPopularBoards(): HotBoard

    fun disposeAll()
}
