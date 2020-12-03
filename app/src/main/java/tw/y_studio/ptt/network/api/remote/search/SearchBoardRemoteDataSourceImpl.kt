package tw.y_studio.ptt.source.remote.search

import tw.y_studio.ptt.network.api.SearchBoardAPI

class SearchBoardRemoteDataSourceImpl(
    private val searchBoardAPI: SearchBoardAPI
) : ISearchBoardRemoteDataSource {

    @Throws(Exception::class)
    override fun searchBoardByKeyword(keyword: String): MutableList<Map<String, Any>> {
        return searchBoardAPI.searchBoard(keyword)
    }
}
