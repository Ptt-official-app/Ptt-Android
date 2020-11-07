package tw.y_studio.ptt.source.remote.search

import tw.y_studio.ptt.api.SearchBoardAPI

class SearchBoardRemoteDataSourceImpl(
    private val searchBoardAPI: SearchBoardAPI
) : ISearchBoardRemoteDataSource {
    override fun searchBoardByKeyboard(keyboard: String): MutableList<Map<String, Any>> {
        return searchBoardAPI.searchBoard(keyboard)
    }
}
