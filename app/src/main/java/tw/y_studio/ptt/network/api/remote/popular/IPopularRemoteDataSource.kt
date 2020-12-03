package tw.y_studio.ptt.source.remote.popular

interface IPopularRemoteDataSource {

    fun getPopularBoardData(page: Int, count: Int): MutableList<Map<String, Any>>
}
