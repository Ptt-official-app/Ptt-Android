package cc.ptt.android.di

import cc.ptt.android.data.source.remote.article.ArticleRemoteDataSource
import cc.ptt.android.data.source.remote.article.ArticleRemoteDataSourceImpl
import cc.ptt.android.data.source.remote.board.BoardRemoteDataSource
import cc.ptt.android.data.source.remote.board.BoardRemoteDataSourceImpl
import cc.ptt.android.data.source.remote.favorite.FavoriteRemoteDataSource
import cc.ptt.android.data.source.remote.favorite.FavoriteRemoteDataSourceImpl
import cc.ptt.android.data.source.remote.login.LoginRemoteDataSource
import cc.ptt.android.data.source.remote.login.LoginRemoteDataSourceImpl
import cc.ptt.android.data.source.remote.search.SearchBoardRemoteDataSource
import cc.ptt.android.data.source.remote.search.SearchBoardRemoteDataSourceImpl
import org.koin.dsl.module

val remoteDataSourceModules = module {
    factory <BoardRemoteDataSource> { BoardRemoteDataSourceImpl(get()) }
    factory <SearchBoardRemoteDataSource> { SearchBoardRemoteDataSourceImpl(get()) }
    factory <ArticleRemoteDataSource> { ArticleRemoteDataSourceImpl(get()) }
    factory <FavoriteRemoteDataSource> { FavoriteRemoteDataSourceImpl(get()) }
    factory <LoginRemoteDataSource> { LoginRemoteDataSourceImpl(get()) }
}
