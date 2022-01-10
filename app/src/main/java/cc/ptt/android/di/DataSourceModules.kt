package cc.ptt.android.di

import cc.ptt.android.data.source.remote.article.ArticleRemoteDataSourceImpl
import cc.ptt.android.data.source.remote.article.IArticleRemoteDataSource
import cc.ptt.android.data.source.remote.board.BoardRemoteDataSourceImpl
import cc.ptt.android.data.source.remote.board.IBoardRemoteDataSource
import cc.ptt.android.data.source.remote.favorite.IFavoriteRemoteDataSource
import cc.ptt.android.data.source.remote.favorite.IFavoriteRemoteDataSourceImpl
import cc.ptt.android.data.source.remote.search.ISearchBoardRemoteDataSource
import cc.ptt.android.data.source.remote.search.SearchBoardRemoteDataSourceImpl
import org.koin.dsl.module

val dataSourceModules = module {
    factory<IBoardRemoteDataSource> { BoardRemoteDataSourceImpl(get()) }
    factory<ISearchBoardRemoteDataSource> { SearchBoardRemoteDataSourceImpl(get()) }
    factory<IArticleRemoteDataSource> { ArticleRemoteDataSourceImpl(get(), get(), get(IO)) }
    factory<IFavoriteRemoteDataSource> { IFavoriteRemoteDataSourceImpl(get()) }
}
