package tw.y_studio.ptt.source.remote.article

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import tw.y_studio.ptt.api.PostAPI
import tw.y_studio.ptt.api.PostRankMark
import tw.y_studio.ptt.api.article.ArticleApiService
import tw.y_studio.ptt.api.model.Post
import tw.y_studio.ptt.api.model.PostRank
import tw.y_studio.ptt.api.model.article.ArticleCommentsList
import tw.y_studio.ptt.api.model.article.ArticleDetail

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
