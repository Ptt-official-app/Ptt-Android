package cc.ptt.android.data.di

import cc.ptt.android.data.source.remote.article.ArticleRemoteDataSource
import cc.ptt.android.data.source.remote.article.ArticleRemoteDataSourceImpl
import cc.ptt.android.data.source.remote.board.BoardRemoteDataSource
import cc.ptt.android.data.source.remote.board.BoardRemoteDataSourceImpl
import cc.ptt.android.data.source.remote.search.SearchBoardRemoteDataSource
import cc.ptt.android.data.source.remote.search.SearchBoardRemoteDataSourceImpl
import cc.ptt.android.data.source.remote.user.UserRemoteDataSource
import cc.ptt.android.data.source.remote.user.UserRemoteDataSourceImpl
import org.koin.dsl.module

val remoteDataSourceModules = module {
    factory <BoardRemoteDataSource> { BoardRemoteDataSourceImpl(get()) }
    factory <SearchBoardRemoteDataSource> { SearchBoardRemoteDataSourceImpl(get()) }
    factory <ArticleRemoteDataSource> { ArticleRemoteDataSourceImpl(get()) }
    factory <UserRemoteDataSource> { UserRemoteDataSourceImpl(get()) }
}
