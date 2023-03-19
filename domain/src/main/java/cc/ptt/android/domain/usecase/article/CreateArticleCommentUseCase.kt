package cc.ptt.android.domain.usecase.article

import cc.ptt.android.data.model.remote.article.ArticleComment
import cc.ptt.android.data.model.remote.article.ArticleCommentType
import cc.ptt.android.data.repository.article.ArticleRepository
import cc.ptt.android.domain.base.UseCaseBase
import kotlinx.coroutines.flow.Flow

class CreateArticleCommentUseCase constructor(
    private val articleRepository: ArticleRepository
) : UseCaseBase() {

    fun createArticleComment(
        bid: String,
        aid: String,
        type: ArticleCommentType,
        content: String
    ): Flow<ArticleComment> {
        return articleRepository.createArticleComment(bid, aid, type.value, content)
    }
}
