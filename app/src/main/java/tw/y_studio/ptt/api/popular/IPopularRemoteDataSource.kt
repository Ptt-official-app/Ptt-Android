package tw.y_studio.ptt.api.popular

interface IPopularRemoteDataSource {

    fun getPopularBoardData(page: Int, count: Int): MutableList<Map<String, Any>>
}
