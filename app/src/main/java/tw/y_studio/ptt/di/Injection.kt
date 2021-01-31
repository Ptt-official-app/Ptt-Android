package tw.y_studio.ptt.di

import tw.y_studio.ptt.api.SearchBoardAPI
import tw.y_studio.ptt.source.remote.search.ISearchBoardRemoteDataSource
import tw.y_studio.ptt.source.remote.search.SearchBoardRemoteDataSourceImpl

object Injection {
    object API {
        val searchBoardAPI by lazy { SearchBoardAPI() }
    }

    object RemoteDataSource {
        val searchBoardRemoteDataSource: ISearchBoardRemoteDataSource by lazy {
            SearchBoardRemoteDataSourceImpl(API.searchBoardAPI)
        }
    }
}
