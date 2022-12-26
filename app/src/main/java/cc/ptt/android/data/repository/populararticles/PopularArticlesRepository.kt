package cc.ptt.android.data.repository.populararticles

import cc.ptt.android.data.model.remote.article.hotarticle.HotArticleList
import kotlinx.coroutines.flow.Flow

interface PopularArticlesRepository {
    fun getPopularArticles(
        startIndex: String,
        limit: Int,
        desc: Boolean
    ): Flow<HotArticleList>
}
