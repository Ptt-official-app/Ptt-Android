package cc.ptt.android.domain.usecase.board

import cc.ptt.android.data.model.remote.board.article.ArticleList
import cc.ptt.android.data.model.remote.board.hotboard.BoardList
import cc.ptt.android.data.repository.board.BoardRepository
import kotlinx.coroutines.flow.Flow

class BoardUseCaseImpl constructor(
    private val boardRepository: BoardRepository
) : BoardUseCase {

    override fun getPopularBoards(): Flow<BoardList> {
        return boardRepository.getPopularBoards()
    }

    override fun getBoardArticles(
        boardId: String,
        title: String,
        startIndex: String,
        limit: Int,
        desc: Boolean
    ): Flow<ArticleList> {
        return boardRepository.getBoardArticles(boardId, title, startIndex, limit, desc)
    }
}
