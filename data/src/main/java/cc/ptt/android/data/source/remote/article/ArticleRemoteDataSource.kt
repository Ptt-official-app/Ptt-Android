package cc.ptt.android.data.source.remote.article

import cc.ptt.android.data.model.remote.article.ArticleComment
import cc.ptt.android.data.model.remote.article.ArticleCommentsList
import cc.ptt.android.data.model.remote.article.ArticleDetail
import cc.ptt.android.data.model.remote.article.ArticleRank
import kotlinx.coroutines.flow.Flow

interface ArticleRemoteDataSource {

    fun getArticleDetail(boardId: String, articleId: String): Flow<ArticleDetail>

    fun getArticleComments(
        boardId: String,
        articleId: String,
        desc: Boolean = false
    ): Flow<ArticleCommentsList>

    fun postArticleRank(
        rank: Int,
        boardId: String,
        articleId: String
    ): Flow<ArticleRank>

    fun createArticleComment(bid: String, aid: String, type: Int, content: String): Flow<ArticleComment>
}
