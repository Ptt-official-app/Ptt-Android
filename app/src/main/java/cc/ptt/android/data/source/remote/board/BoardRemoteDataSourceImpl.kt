package cc.ptt.android.data.source.remote.board

import cc.ptt.android.data.api.board.BoardApiService
import cc.ptt.android.data.model.remote.board.article.ArticleList
import cc.ptt.android.data.model.remote.board.hotboard.HotBoard
import javax.inject.Inject

class BoardRemoteDataSourceImpl @Inject constructor (
    private val boardApiService: BoardApiService
) : IBoardRemoteDataSource {
    override suspend fun getPopularBoards(): HotBoard {
        return boardApiService.getPopularBoard()
    }

    override suspend fun getBoardArticles(
        boardId: String,
        title: String,
        startIndex: String,
        limit: Int,
        desc: Boolean
    ): ArticleList {
        return boardApiService.getArticles(
            boardId,
            title,
            startIndex,
            limit,
            desc
        )
    }
}
