package cc.ptt.android.domain.usecase

import cc.ptt.android.data.repository.populararticles.PopularArticlesRepository
import cc.ptt.android.domain.base.UseCaseBase
import cc.ptt.android.domain.model.ui.hotarticle.HotArticleUI
import cc.ptt.android.domain.model.ui.hotarticle.HotArticleUIType
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

@FlowPreview
class GetPopularArticlesUIUseCase constructor(
    private val popularArticlesRepository: PopularArticlesRepository
) : UseCaseBase() {

    fun getPopularArticles(startIndex: String? = null, getNext: Boolean): Flow<Results> {
        if (getNext && startIndex.isNullOrEmpty()) {
            throw Exception("Can not get next.")
        }
        val limit = 100
        val desc = false
        val startIndex = startIndex.orEmpty()

        return popularArticlesRepository.getPopularArticles(startIndex, limit, desc).flatMapMerge {
            val data: MutableList<HotArticleUI> = arrayListOf()
            if (!getNext) {
                data.add(HotArticleUI(HotArticleUIType.TITLE, "ALL", 0L, "", "", "", "", "", "", false, "", null))
            }
            for (item in it.list) {
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
            flowOf(Results(data, it.nextIdx))
        }
    }

    data class Results(
        val data: MutableList<HotArticleUI>,
        val nextIdx: String
    )
}
