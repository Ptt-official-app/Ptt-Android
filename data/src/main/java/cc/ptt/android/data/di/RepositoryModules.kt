package cc.ptt.android.data.di

import cc.ptt.android.data.repository.article.ArticleRepository
import cc.ptt.android.data.repository.article.ArticleRepositoryImpl
import cc.ptt.android.data.repository.board.BoardRepository
import cc.ptt.android.data.repository.board.BoardRepositoryImpl
import cc.ptt.android.data.repository.login.LoginRepository
import cc.ptt.android.data.repository.login.LoginRepositoryImpl
import cc.ptt.android.data.repository.populararticles.PopularArticlesRepository
import cc.ptt.android.data.repository.populararticles.PopularArticlesRepositoryImpl
import cc.ptt.android.data.repository.search.SearchBoardRepository
import cc.ptt.android.data.repository.search.SearchBoardRepositoryImpl
import org.koin.dsl.module

val repositoryModules = module {
    factory <PopularArticlesRepository> { PopularArticlesRepositoryImpl(get()) }
    factory <ArticleRepository> { ArticleRepositoryImpl(get()) }
    factory <BoardRepository> { BoardRepositoryImpl(get()) }
    factory <LoginRepository> { LoginRepositoryImpl(get(), get()) }
    factory <SearchBoardRepository> { SearchBoardRepositoryImpl(get()) }
}
