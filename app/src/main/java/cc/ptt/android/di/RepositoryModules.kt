package cc.ptt.android.di

import cc.ptt.android.data.repository.article.ArticleCommentRepository
import cc.ptt.android.data.repository.article.ArticleCommentRepositoryImpl
import cc.ptt.android.data.repository.login.LoginRepository
import cc.ptt.android.data.repository.login.LoginRepositoryImpl
import cc.ptt.android.data.repository.populararticles.PopularArticlesRepository
import cc.ptt.android.data.repository.populararticles.PopularArticlesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModules {

    @Binds
    abstract fun providePopularArticlesRepository(
        popularArticlesRepositoryImpl: PopularArticlesRepositoryImpl
    ): PopularArticlesRepository

    @Binds
    abstract fun provideArticleCommentRepository(
        articleCommentRepositoryImpl: ArticleCommentRepositoryImpl
    ): ArticleCommentRepository

    @Binds
    @Singleton
    abstract fun provideLoginRepository(
        loginRepositoryImpl: LoginRepositoryImpl
    ): LoginRepository
}
