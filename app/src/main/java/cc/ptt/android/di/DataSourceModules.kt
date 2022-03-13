package cc.ptt.android.di

import cc.ptt.android.data.source.remote.article.ArticleRemoteDataSourceImpl
import cc.ptt.android.data.source.remote.article.IArticleRemoteDataSource
import cc.ptt.android.data.source.remote.board.BoardRemoteDataSourceImpl
import cc.ptt.android.data.source.remote.board.IBoardRemoteDataSource
import cc.ptt.android.data.source.remote.favorite.IFavoriteRemoteDataSource
import cc.ptt.android.data.source.remote.favorite.IFavoriteRemoteDataSourceImpl
import cc.ptt.android.data.source.remote.search.ISearchBoardRemoteDataSource
import cc.ptt.android.data.source.remote.search.SearchBoardRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class DataSourceModules {

    @Binds
    abstract fun provideIBoardRemoteDataSource(
        boardRemoteDataSourceImpl: BoardRemoteDataSourceImpl
    ): IBoardRemoteDataSource

    @Binds
    abstract fun provideISearchBoardRemoteDataSource(
        searchBoardRemoteDataSourceImpl: SearchBoardRemoteDataSourceImpl
    ): ISearchBoardRemoteDataSource

    @Binds
    abstract fun provideIArticleRemoteDataSource(
        articleRemoteDataSourceImpl: ArticleRemoteDataSourceImpl
    ): IArticleRemoteDataSource

    @Binds
    abstract fun provideIFavoriteRemoteDataSource(
        favoriteRemoteDataSourceImpl: IFavoriteRemoteDataSourceImpl
    ): IFavoriteRemoteDataSource
}
