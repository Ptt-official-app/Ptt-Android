package cc.ptt.android.data.repository.search

import cc.ptt.android.data.model.remote.board.hotboard.BoardList
import cc.ptt.android.data.source.remote.search.SearchBoardRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class SearchBoardRepositoryImpl constructor(
    private val searchBoardRemoteDataSource: SearchBoardRemoteDataSource
) : SearchBoardRepository {
    override fun searchBoardByKeyword(
        keyword: String,
        startIndex: String,
        limit: Int,
        aces: Boolean
    ): Flow<BoardList> {
        return searchBoardRemoteDataSource.searchBoardByKeyword(keyword, startIndex, limit, aces).flowOn(Dispatchers.IO)
    }
}
