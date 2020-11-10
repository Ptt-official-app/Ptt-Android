package tw.y_studio.ptt.di

import tw.y_studio.ptt.api.PopularBoardListAPI
import tw.y_studio.ptt.api.SearchBoardAPI
import tw.y_studio.ptt.source.remote.popular.IPopularRemoteDataSource
import tw.y_studio.ptt.source.remote.popular.PopularRemoteDataSourceImpl
import tw.y_studio.ptt.source.remote.search.ISearchBoardRemoteDataSource
import tw.y_studio.ptt.source.remote.search.SearchBoardRemoteDataSourceImpl

object Injection {
    object API {
        val popularBoardListAPI by lazy {
            PopularBoardListAPI()
        }
        val searchBoardAPI by lazy { SearchBoardAPI() }
    }

    object RemoteDataSource {
        val popularRemoteDataSource: IPopularRemoteDataSource by lazy {
            PopularRemoteDataSourceImpl(API.popularBoardListAPI)
        }
        val searchBoardRemoteDataSource: ISearchBoardRemoteDataSource by lazy {
            SearchBoardRemoteDataSourceImpl(API.searchBoardAPI)
        }
    }
}
