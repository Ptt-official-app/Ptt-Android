package tw.y_studio.ptt.api.popular

import tw.y_studio.ptt.api.PopularBoardListAPI

class PopularRemoteDataSourceImpl : IPopularRemoteDataSource {

    // TODO: 2020/11/5 之後會改成 Construct param，
    //  但現在沒有良好的依賴注入的方式的話先用 lazy init 比較好
    private val popularBoardListAPI by lazy {
        PopularBoardListAPI()
    }

    @Throws(Exception::class)
    override fun getPopularBoardData(page: Int, count: Int): MutableList<Map<String, Any>> {
        return popularBoardListAPI.refresh(page, count)
    }
}
