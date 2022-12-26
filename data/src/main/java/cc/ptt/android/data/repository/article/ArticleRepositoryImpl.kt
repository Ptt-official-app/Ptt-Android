package cc.ptt.android.data.repository.article

import cc.ptt.android.data.model.remote.article.ArticleComment
import cc.ptt.android.data.model.remote.article.ArticleCommentsList
import cc.ptt.android.data.model.remote.article.ArticleDetail
import cc.ptt.android.data.model.remote.article.ArticleRank
import cc.ptt.android.data.source.remote.article.ArticleRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class ArticleRepositoryImpl constructor(
    private val articleRemoteDataSource: ArticleRemoteDataSource
) : ArticleRepository {

    override fun getArticleDetail(boardId: String, articleId: String): Flow<ArticleDetail> {
        return articleRemoteDataSource.getArticleDetail(boardId, articleId).flowOn(Dispatchers.IO)
    }

    override fun getArticleComments(
        boardId: String,
        articleId: String,
        desc: Boolean
    ): Flow<ArticleCommentsList> {
        return articleRemoteDataSource.getArticleComments(boardId, articleId, desc).flowOn(Dispatchers.IO)
    }

    override fun createArticleComment(bid: String, aid: String, type: Int, content: String): Flow<ArticleComment> {
        return articleRemoteDataSource.createArticleComment(bid, aid, type, content).flowOn(Dispatchers.IO)
    }

    override fun postArticleRank(rank: Int, boardId: String, articleId: String): Flow<ArticleRank> {
        return articleRemoteDataSource.postArticleRank(rank, boardId, articleId).flowOn(Dispatchers.IO)
    }
}
