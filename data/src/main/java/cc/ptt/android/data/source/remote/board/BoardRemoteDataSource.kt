package cc.ptt.android.data.source.remote.board

import cc.ptt.android.data.model.remote.board.article.ArticleList
import cc.ptt.android.data.model.remote.board.hotboard.BoardList
import kotlinx.coroutines.flow.Flow

interface BoardRemoteDataSource {

    fun getPopularBoards(): Flow<BoardList>

    fun getBoardArticles(
        boardId: String,
        title: String = "",
        startIndex: String = "",
        limit: Int = 200,
        desc: Boolean = true
    ): Flow<ArticleList>

    fun getFavoriteBoards(
        userid: String = "",
        level_idx: String = "",
        startIndex: String = "",
        limit: Int = 200,
        aces: Boolean = true
    ): Flow<BoardList>
}
