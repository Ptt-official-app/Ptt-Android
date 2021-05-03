package tw.y_studio.ptt.di

import org.koin.dsl.module
import tw.y_studio.ptt.source.remote.article.ArticleRemoteDataSourceImpl
import tw.y_studio.ptt.source.remote.article.IArticleRemoteDataSource
import tw.y_studio.ptt.source.remote.board.BoardRemoteDataSourceImpl
import tw.y_studio.ptt.source.remote.board.IBoardRemoteDataSource
import tw.y_studio.ptt.source.remote.search.ISearchBoardRemoteDataSource
import tw.y_studio.ptt.source.remote.search.SearchBoardRemoteDataSourceImpl
import tw.y_studio.ptt.source.remote.user.IUserRemoteDataSource
import tw.y_studio.ptt.source.remote.user.UserRemoteDataSourceImpl

val dataSourceModules = module {
    factory<IBoardRemoteDataSource> { BoardRemoteDataSourceImpl(get()) }
    factory<ISearchBoardRemoteDataSource> { SearchBoardRemoteDataSourceImpl(get()) }
    factory<IArticleRemoteDataSource> { ArticleRemoteDataSourceImpl(get(), get(), get(IO)) }
    factory<IUserRemoteDataSource> { UserRemoteDataSourceImpl(get(), get(IO)) }
}
