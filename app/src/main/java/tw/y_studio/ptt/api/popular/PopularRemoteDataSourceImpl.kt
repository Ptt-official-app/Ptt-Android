package tw.y_studio.ptt.api.popular

import tw.y_studio.ptt.api.PopularBoardListAPI

class PopularRemoteDataSourceImpl(
    private val popularBoardListAPI: PopularBoardListAPI
) : IPopularRemoteDataSource {

    @Throws(Exception::class)
    override fun getPopularBoardData(page: Int, count: Int): MutableList<Map<String, Any>> {
        return popularBoardListAPI.refresh(page, count)
    }
}
