package cc.ptt.android.di

import cc.ptt.android.domain.usecase.GetPopularArticlesUIUseCase
import cc.ptt.android.domain.usecase.articlecomment.CreateArticleCommentUseCase
import cc.ptt.android.domain.usecase.login.LoginUseCase
import kotlinx.coroutines.FlowPreview
import org.koin.dsl.module

@FlowPreview
val useCaseModules = module {
    factory <CreateArticleCommentUseCase> { CreateArticleCommentUseCase(get()) }
    factory <LoginUseCase> { LoginUseCase(get(), get()) }
    factory <GetPopularArticlesUIUseCase> { GetPopularArticlesUIUseCase(get()) }
}
