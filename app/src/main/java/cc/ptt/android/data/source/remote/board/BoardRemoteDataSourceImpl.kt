package cc.ptt.android.data.source.remote.board

import cc.ptt.android.data.api.board.BoardApi
import cc.ptt.android.data.model.remote.board.article.ArticleList
import cc.ptt.android.data.model.remote.board.hotboard.BoardList
import kotlinx.coroutines.flow.Flow

class BoardRemoteDataSourceImpl constructor (
    private val boardApi: BoardApi
) : BoardRemoteDataSource {

    override fun getPopularBoards(): Flow<BoardList> {
        return boardApi.getPopularBoard()
    }

    override fun getBoardArticles(
        boardId: String,
        title: String,
        startIndex: String,
        limit: Int,
        desc: Boolean
    ): Flow<ArticleList> {
        return boardApi.getArticles(
            boardId,
            title,
            startIndex,
            limit,
            desc
        )
    }
}
