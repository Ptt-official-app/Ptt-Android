package cc.ptt.android.domain.usecase.board

import cc.ptt.android.data.model.remote.board.article.ArticleList
import cc.ptt.android.data.model.remote.board.hotboard.BoardList
import kotlinx.coroutines.flow.Flow

interface BoardUseCase {
    fun getPopularBoards(): Flow<BoardList>
    fun getBoardArticles(
        boardId: String,
        title: String = "",
        startIndex: String = "",
        limit: Int = 200,
        desc: Boolean = true
    ): Flow<ArticleList>

    fun getFavoriteBoards(
        level_idx: String,
        startIndex: String,
        limit: Int,
        aces: Boolean
    ): Flow<BoardList>
}
