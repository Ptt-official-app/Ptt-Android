package cc.ptt.android.data.source.remote.board

import cc.ptt.android.data.model.remote.board.article.ArticleList
import cc.ptt.android.data.model.remote.board.hotboard.HotBoard

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
