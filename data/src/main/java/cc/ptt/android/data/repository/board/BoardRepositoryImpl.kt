package cc.ptt.android.data.repository.board

import cc.ptt.android.data.model.remote.board.article.ArticleList
import cc.ptt.android.data.model.remote.board.hotboard.BoardList
import cc.ptt.android.data.source.remote.board.BoardRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class BoardRepositoryImpl constructor(
    private val boardRemoteDataSource: BoardRemoteDataSource
) : BoardRepository {
    override fun getPopularBoards(): Flow<BoardList> {
        return boardRemoteDataSource.getPopularBoards().flowOn(Dispatchers.IO)
    }

    override fun getBoardArticles(
        boardId: String,
        title: String,
        startIndex: String,
        limit: Int,
        desc: Boolean
    ): Flow<ArticleList> {
        return boardRemoteDataSource.getBoardArticles(boardId, title, startIndex, limit, desc).flowOn(Dispatchers.IO)
    }
}
