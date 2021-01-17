package tw.y_studio.ptt.source.remote.popular

import tw.y_studio.ptt.model.HotBoard

interface IPopularRemoteDataSource {

    fun getPopularBoardData(page: Int, count: Int): MutableList<HotBoard>

    fun disposeAll()
}
