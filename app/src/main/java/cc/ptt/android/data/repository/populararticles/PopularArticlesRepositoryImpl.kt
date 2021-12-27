package cc.ptt.android.data.repository.populararticles

import cc.ptt.android.data.api.article.ArticleApiService
import cc.ptt.android.data.model.remote.article.hotarticle.HotArticleList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class PopularArticlesRepositoryImpl constructor(
    private val articleApiService: ArticleApiService,
    private val dispatcher: CoroutineDispatcher,
) : PopularArticlesRepository {
    override suspend fun getPopularArticles(startIndex: String, limit: Int, desc: Boolean): HotArticleList = withContext(dispatcher) {
        return@withContext articleApiService.getPopularArticles(startIndex = startIndex, limit = limit, desc = desc)
    }
}
