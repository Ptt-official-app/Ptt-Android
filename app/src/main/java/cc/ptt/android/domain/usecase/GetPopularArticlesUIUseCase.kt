package cc.ptt.android.domain.usecase

import cc.ptt.android.data.model.ui.hotarticle.HotArticleUI
import cc.ptt.android.data.model.ui.hotarticle.HotArticleUIType
import cc.ptt.android.data.repository.populararticles.PopularArticlesRepository
import cc.ptt.android.di.IODispatchers
import cc.ptt.android.domain.base.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GetPopularArticlesUIUseCase @Inject constructor(
    private val popularArticlesRepository: PopularArticlesRepository,
    @IODispatchers dispatcher: CoroutineDispatcher
) : UseCase<GetPopularArticlesUIUseCase.Params, GetPopularArticlesUIUseCase.Results>(dispatcher) {

    data class Params(
        val startIndex: String? = null,
        val getNext: Boolean
    )

    data class Results(
        val data: MutableList<HotArticleUI>,
        val nextIdx: String
    )

    override suspend fun execute(parameters: Params): Result<Results> {
        return try {
            if (parameters.getNext && parameters.startIndex.isNullOrEmpty()) {
                throw Exception("Can not get next.")
            }
            val limit = 100
            val desc = false
            val startIndex = parameters.startIndex ?: ""
            val result = popularArticlesRepository.getPopularArticles(startIndex, limit, desc)
            val data: MutableList<HotArticleUI> = arrayListOf()
            if (!parameters.getNext) {
                data.add(HotArticleUI(HotArticleUIType.TITLE, "ALL", 0L, "", "", "", "", "", "", false, "", null))
            }
            for (item in result.list) {
                data.add(
                    HotArticleUI(
                        HotArticleUIType.NORMAL,
                        item.title,
                        item.createTime.toLong(),
                        item.bid,
                        item.`class`,
                        item.owner,
                        item.rank.toString(),
                        item.recommend.toString(),
                        item.url,
                        item.read,
                        "",
                        item
                    )
                )
            }
            Result.success(Results(data, result.nextIdx))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
