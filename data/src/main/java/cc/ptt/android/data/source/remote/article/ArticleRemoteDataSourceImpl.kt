package cc.ptt.android.data.source.remote.article

import cc.ptt.android.data.apiservices.article.ArticleApi
import cc.ptt.android.data.model.remote.article.ArticleComment
import cc.ptt.android.data.model.remote.article.ArticleCommentsList
import cc.ptt.android.data.model.remote.article.ArticleDetail
import cc.ptt.android.data.model.remote.article.ArticleRank
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

class ArticleRemoteDataSourceImpl constructor (
    private val articleApi: ArticleApi
) : ArticleRemoteDataSource {

    override fun getArticleDetail(
        boardId: String,
        articleId: String
    ): Flow<ArticleDetail> {
        return articleApi.getArticleDetail(boardId, articleId)
    }

    override fun getArticleComments(
        boardId: String,
        articleId: String,
        desc: Boolean
    ): Flow<ArticleCommentsList> {
        return articleApi.getArticleComments(boardId, articleId, desc)
    }

    override fun postArticleRank(
        rank: Int,
        boardId: String,
        articleId: String
    ): Flow<ArticleRank> {
        val param: String = Gson().toJson(
            ArticleRank(rank)
        )
        val body = param.toRequestBody("text/plain; charset=utf-8".toMediaType())
        return articleApi.postArticleRank(boardId, articleId, body)
    }

    override fun createArticleComment(bid: String, aid: String, type: Int, content: String): Flow<ArticleComment> {
        val param: String = JsonObject().apply {
            addProperty("type", type)
            addProperty("content", content)
        }.toString()
        val body = param.toRequestBody("text/plain; charset=utf-8".toMediaType())
        return articleApi.createArticleComment(bid, aid, body)
    }
}
