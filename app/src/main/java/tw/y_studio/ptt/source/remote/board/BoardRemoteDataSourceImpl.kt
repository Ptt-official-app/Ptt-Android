package tw.y_studio.ptt.source.remote.board

import tw.y_studio.ptt.api.board.BoardApiService
import tw.y_studio.ptt.api.model.board.article.ArticleList
import tw.y_studio.ptt.api.model.board.hot_board.HotBoard

class BoardRemoteDataSourceImpl(
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
