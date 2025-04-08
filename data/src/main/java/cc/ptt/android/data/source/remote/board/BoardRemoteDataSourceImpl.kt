package cc.ptt.android.data.source.remote.board

import cc.ptt.android.data.apiservices.board.BoardApi
import cc.ptt.android.data.model.remote.board.article.ArticleList
import cc.ptt.android.data.model.remote.board.hotboard.BoardList
import kotlinx.coroutines.flow.Flow

class BoardRemoteDataSourceImpl constructor(
    private val boardApi: BoardApi,
) : BoardRemoteDataSource {
    override fun getPopularBoards(): Flow<BoardList> = boardApi.getPopularBoard()

    override fun getBoardArticles(
        boardId: String,
        title: String,
        startIndex: String,
        limit: Int,
        desc: Boolean,
    ): Flow<ArticleList> =
        boardApi.getArticles(
            boardId,
            title,
            startIndex,
            limit,
            desc,
        )

    override fun getFavoriteBoards(
        userid: String,
        level_idx: String,
        startIndex: String,
        limit: Int,
        aces: Boolean,
    ): Flow<BoardList> = boardApi.favoriteBoards(userid, level_idx, startIndex, limit, aces)
}
