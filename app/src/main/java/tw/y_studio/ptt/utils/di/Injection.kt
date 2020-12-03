package tw.y_studio.ptt.utils.di

import tw.y_studio.ptt.network.api.PopularBoardListAPI
import tw.y_studio.ptt.network.api.PostAPI
import tw.y_studio.ptt.network.api.PostListAPI
import tw.y_studio.ptt.network.api.SearchBoardAPI
import tw.y_studio.ptt.source.remote.popular.IPopularRemoteDataSource
import tw.y_studio.ptt.source.remote.popular.PopularRemoteDataSourceImpl
import tw.y_studio.ptt.source.remote.post.IPostListRemoteDataSource
import tw.y_studio.ptt.source.remote.post.PostListRemoteDataSourceImpl
import tw.y_studio.ptt.source.remote.post.PostRemoteDataSourceImpl
import tw.y_studio.ptt.source.remote.search.ISearchBoardRemoteDataSource
import tw.y_studio.ptt.source.remote.search.SearchBoardRemoteDataSourceImpl

object Injection {
    object API {
        val popularBoardListAPI by lazy {
            PopularBoardListAPI()
        }
        val searchBoardAPI by lazy { SearchBoardAPI() }
        val postListAPI by lazy { PostListAPI() }
        val postAPI by lazy { PostAPI() }
    }

    object RemoteDataSource {
        val popularRemoteDataSource: IPopularRemoteDataSource by lazy {
            PopularRemoteDataSourceImpl(API.popularBoardListAPI)
        }
        val searchBoardRemoteDataSource: ISearchBoardRemoteDataSource by lazy {
            SearchBoardRemoteDataSourceImpl(API.searchBoardAPI)
        }
        val postListRemoteDataSource: IPostListRemoteDataSource by lazy {
            PostListRemoteDataSourceImpl(API.postListAPI)
        }
        val postRemoteDataSourceImpl by lazy {
            PostRemoteDataSourceImpl(API.postAPI)
        }
    }
}
