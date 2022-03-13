package cc.ptt.android.domain.usecase.articlecomment

import cc.ptt.android.data.model.remote.article.ArticleComment
import cc.ptt.android.data.model.remote.article.ArticleCommentType
import cc.ptt.android.data.repository.article.ArticleCommentRepository
import cc.ptt.android.di.IODispatchers
import cc.ptt.android.domain.base.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import java.lang.Exception
import javax.inject.Inject

class CreateArticleCommentUseCase @Inject constructor(
    private val articleCommentRepository: ArticleCommentRepository,
    @IODispatchers dispatcher: CoroutineDispatcher
) : UseCase<CreateArticleCommentUseCase.Params, CreateArticleCommentUseCase.Results>(dispatcher) {

    data class Params(
        val bid: String,
        val aid: String,
        val type: ArticleCommentType,
        val content: String
    )

    data class Results(
        val comment: ArticleComment
    )

    override suspend fun execute(parameters: Params): Result<Results> {
        return try {
            Result.success(Results(articleCommentRepository.createArticleComment(parameters.bid, parameters.aid, parameters.type.value, parameters.content)))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
