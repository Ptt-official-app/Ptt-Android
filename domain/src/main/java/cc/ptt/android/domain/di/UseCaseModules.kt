package cc.ptt.android.domain.di

import cc.ptt.android.domain.usecase.GetPopularArticlesUIUseCase
import cc.ptt.android.domain.usecase.article.CreateArticleCommentUseCase
import cc.ptt.android.domain.usecase.article.GetArticleUseCase
import cc.ptt.android.domain.usecase.board.BoardUseCase
import cc.ptt.android.domain.usecase.board.BoardUseCaseImpl
import cc.ptt.android.domain.usecase.login.LoginUseCase
import kotlinx.coroutines.FlowPreview
import org.koin.dsl.module

@FlowPreview
val useCaseModules = module {
    factory <CreateArticleCommentUseCase> { CreateArticleCommentUseCase(get()) }
    factory <GetArticleUseCase> { GetArticleUseCase(get(), get()) }
    single <LoginUseCase> { LoginUseCase(get(), get()) }
    factory <GetPopularArticlesUIUseCase> { GetPopularArticlesUIUseCase(get()) }
    factory <BoardUseCase> { BoardUseCaseImpl(get()) }
}
