package tw.y_studio.ptt.source.remote.article

import tw.y_studio.ptt.api.PostRankMark
import tw.y_studio.ptt.api.model.Post
import tw.y_studio.ptt.api.model.PostRank
import tw.y_studio.ptt.api.model.article.ArticleCommentsList
import tw.y_studio.ptt.api.model.article.ArticleDetail

interface IArticleRemoteDataSource {
    suspend fun getArticleDetail(boardId: String, articleId: String): ArticleDetail

    suspend fun getArticleComments(
        boardId: String,
        articleId: String,
        desc: Boolean = false
    ): ArticleCommentsList

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
