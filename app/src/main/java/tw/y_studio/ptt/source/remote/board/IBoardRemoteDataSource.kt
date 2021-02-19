package tw.y_studio.ptt.source.remote.board

import tw.y_studio.ptt.api.model.board.article.ArticleList
import tw.y_studio.ptt.api.model.board.hot_board.HotBoard

interface IBoardRemoteDataSource {
    suspend fun getPopularBoards(): HotBoard

    suspend fun getBoardArticles(
        boardId: String,
        title: String = "",
        startIndex: String = "",
        limit: Int = 200,
        desc: Boolean = true
    ): ArticleList
}
