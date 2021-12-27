package cc.ptt.android.data.repository.populararticles

import cc.ptt.android.data.model.remote.article.hotarticle.HotArticleList

interface PopularArticlesRepository {
    suspend fun getPopularArticles(
        startIndex: String,
        limit: Int,
        desc: Boolean
    ): HotArticleList
}
