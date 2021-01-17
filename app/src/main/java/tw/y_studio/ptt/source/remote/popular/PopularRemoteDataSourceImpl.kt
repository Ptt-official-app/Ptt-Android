package tw.y_studio.ptt.source.remote.popular

import tw.y_studio.ptt.api.PopularBoardListAPI
import tw.y_studio.ptt.model.HotBoard

class PopularRemoteDataSourceImpl(
    private val popularBoardListAPI: PopularBoardListAPI
) : IPopularRemoteDataSource {

    @Throws(Exception::class)
    override fun getPopularBoardData(page: Int, count: Int): MutableList<HotBoard> {
        return popularBoardListAPI.refresh(page, count)
    }

    override fun disposeAll() {
        popularBoardListAPI.close()
    }
}
