package cc.ptt.android.data.source.remote.article

import cc.ptt.android.data.api.PostAPI
import cc.ptt.android.data.api.PostRankMark
import cc.ptt.android.data.api.article.ArticleApiService
import cc.ptt.android.data.model.remote.Post
import cc.ptt.android.data.model.remote.PostRank
import cc.ptt.android.data.model.remote.article.ArticleCommentsList
import cc.ptt.android.data.model.remote.article.ArticleDetail
import cc.ptt.android.data.model.remote.article.ArticleRank
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

class ArticleRemoteDataSourceImpl(
    private val postAPI: PostAPI,
    private val articleApiService: ArticleApiService,
    private val dispatcher: CoroutineDispatcher,
) : IArticleRemoteDataSource {

    override suspend fun getArticleDetail(
        boardId: String,
        articleId: String
    ): ArticleDetail = withContext(dispatcher) {
        return@withContext articleApiService.getArticleDetail(boardId, articleId)
    }

    override suspend fun getArticleComments(
        boardId: String,
        articleId: String,
        desc: Boolean
    ): ArticleCommentsList = withContext(dispatcher) {
        return@withContext articleApiService.getArticleComments(boardId, articleId, desc)
    }

    override suspend fun postArticleRank(
        rank: Int,
        boardId: String,
        articleId: String
    ) = withContext(dispatcher) {
        val param: String = Gson().toJson(
            ArticleRank(rank)
        )
        val body = param.toRequestBody("text/plain; charset=utf-8".toMediaType())

        return@withContext articleApiService.postArticleRank(boardId, articleId, body)
    }

    override fun getPost(board: String, fileName: String): Post {
        return postAPI.getPost(board, fileName)
    }

    override fun setPostRank(
        boardName: String,
        aid: String,
        pttId: String,
        rank: PostRankMark
    ) {
        postAPI.setPostRank(boardName, aid, pttId, rank)
    }

    override fun getPostRank(board: String, aid: String): PostRank {
        return postAPI.getPostRank(board, aid)
    }

    override fun disposeAll() {
        postAPI.close()
    }
}
