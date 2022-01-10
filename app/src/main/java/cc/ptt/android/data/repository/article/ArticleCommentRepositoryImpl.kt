package cc.ptt.android.data.repository.article

import cc.ptt.android.data.api.article.ArticleApiService
import cc.ptt.android.data.model.remote.article.ArticleComment
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

class ArticleCommentRepositoryImpl constructor(
    private val articleApiService: ArticleApiService,
    private val dispatcher: CoroutineDispatcher,
) : ArticleCommentRepository {
    override suspend fun createArticleComment(bid: String, aid: String, type: Int, content: String): ArticleComment = withContext(dispatcher) {
        val param: String = JsonObject().apply {
            addProperty("type", type)
            addProperty("content", content)
        }.toString()
        val body = param.toRequestBody("text/plain; charset=utf-8".toMediaType())
        return@withContext articleApiService.createArticleComment(bid, aid, body)
    }
}
