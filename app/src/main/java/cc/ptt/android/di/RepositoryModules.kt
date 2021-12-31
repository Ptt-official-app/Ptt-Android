package cc.ptt.android.di

import cc.ptt.android.data.repository.article.ArticleCommentRepository
import cc.ptt.android.data.repository.article.ArticleCommentRepositoryImpl
import cc.ptt.android.data.repository.login.LoginRepository
import cc.ptt.android.data.repository.login.LoginRepositoryImpl
import cc.ptt.android.data.repository.populararticles.PopularArticlesRepository
import cc.ptt.android.data.repository.populararticles.PopularArticlesRepositoryImpl
import org.koin.dsl.module

val repositoryModules = module {
    factory<PopularArticlesRepository> { PopularArticlesRepositoryImpl(get(), get(IO)) }
    factory<ArticleCommentRepository> { ArticleCommentRepositoryImpl(get(), get(IO)) }
    single<LoginRepository> { LoginRepositoryImpl(get(), get(), get(IO)) }
}
