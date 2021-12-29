package cc.ptt.android.data.repository.article

import cc.ptt.android.data.model.remote.article.ArticleComment

interface ArticleCommentRepository {
    suspend fun createArticleComment(bid: String, aid: String, type: Int, content: String): ArticleComment
}
