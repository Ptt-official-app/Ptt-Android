package cc.ptt.android.domain.usecase.board

import cc.ptt.android.data.model.remote.board.article.ArticleList
import cc.ptt.android.data.model.remote.board.hotboard.BoardList
import cc.ptt.android.data.repository.board.BoardRepository
import cc.ptt.android.data.repository.user.UserRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge

class BoardUseCaseImpl constructor(
    private val boardRepository: BoardRepository,
    private val userRepository: UserRepository
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

    @OptIn(FlowPreview::class)
    override fun getFavoriteBoards(
        level_idx: String,
        startIndex: String,
        limit: Int,
        aces: Boolean
    ): Flow<BoardList> {
        return userRepository.userId().flatMapMerge {
            boardRepository.getFavoriteBoards(it, level_idx, startIndex, limit, aces)
        }
    }
}
