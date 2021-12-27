package cc.ptt.android.data.source.remote.article

import cc.ptt.android.data.api.PostRankMark
import cc.ptt.android.data.model.remote.Post
import cc.ptt.android.data.model.remote.PostRank
import cc.ptt.android.data.model.remote.article.ArticleCommentsList
import cc.ptt.android.data.model.remote.article.ArticleDetail
import cc.ptt.android.data.model.remote.article.ArticleRank

interface IArticleRemoteDataSource {
    suspend fun getArticleDetail(boardId: String, articleId: String): ArticleDetail

    suspend fun getArticleComments(
        boardId: String,
        articleId: String,
        desc: Boolean = false
    ): ArticleCommentsList

    suspend fun postArticleRank(
        rank: Int,
        boardId: String,
        articleId: String
    ): ArticleRank

    fun getPost(board: String, fileName: String): Post

    fun setPostRank(
        boardName: String,
        aid: String,
        pttId: String,
        rank: PostRankMark
    )

    fun getPostRank(board: String, aid: String): PostRank

    fun disposeAll()
}
