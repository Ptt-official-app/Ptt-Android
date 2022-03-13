package cc.ptt.android.data.repository.populararticles

import cc.ptt.android.data.api.article.ArticleApiService
import cc.ptt.android.data.model.remote.article.hotarticle.HotArticleList
import cc.ptt.android.di.IODispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PopularArticlesRepositoryImpl @Inject constructor(
    private val articleApiService: ArticleApiService,
    @IODispatchers private val dispatcher: CoroutineDispatcher,
) : PopularArticlesRepository {
    override suspend fun getPopularArticles(startIndex: String, limit: Int, desc: Boolean): HotArticleList = withContext(dispatcher) {
        return@withContext articleApiService.getPopularArticles(startIndex = startIndex, limit = limit, desc = desc)
    }
}
