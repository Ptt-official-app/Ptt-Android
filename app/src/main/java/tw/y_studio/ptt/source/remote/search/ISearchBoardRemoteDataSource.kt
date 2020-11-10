package tw.y_studio.ptt.source.remote.search

interface ISearchBoardRemoteDataSource {

    fun searchBoardByKeyword(keyword: String): MutableList<Map<String, Any>>
}
