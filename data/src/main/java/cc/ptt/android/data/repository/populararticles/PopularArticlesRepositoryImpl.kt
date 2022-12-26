package cc.ptt.android.data.repository.populararticles

import cc.ptt.android.data.apiservices.article.ArticleApi
import cc.ptt.android.data.model.remote.article.hotarticle.HotArticleList
import kotlinx.coroutines.flow.Flow

class PopularArticlesRepositoryImpl constructor(
    private val articleApi: ArticleApi
) : PopularArticlesRepository {

    override fun getPopularArticles(startIndex: String, limit: Int, desc: Boolean): Flow<HotArticleList> {
        return articleApi.getPopularArticles(startIndex = startIndex, limit = limit, desc = desc)
    }
}
