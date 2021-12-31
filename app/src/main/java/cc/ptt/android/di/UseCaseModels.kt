package cc.ptt.android.di

import cc.ptt.android.domain.usecase.GetPopularArticlesUIUseCase
import cc.ptt.android.domain.usecase.articlecomment.CreateArticleCommentUseCase
import org.koin.core.qualifier.named
import org.koin.dsl.module

val useCaseModels = module {
    factory { GetPopularArticlesUIUseCase(get(), get(named("IO"))) }
    factory { CreateArticleCommentUseCase(get(), get(named("IO"))) }
}
