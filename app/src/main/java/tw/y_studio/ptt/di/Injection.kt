package tw.y_studio.ptt.di

import tw.y_studio.ptt.api.*
import tw.y_studio.ptt.source.remote.popular.IPopularRemoteDataSource
import tw.y_studio.ptt.source.remote.popular.PopularRemoteDataSourceImpl
import tw.y_studio.ptt.source.remote.post.IPostListRemoteDataSource
import tw.y_studio.ptt.source.remote.post.IPostRemoteDataSource
import tw.y_studio.ptt.source.remote.post.PostListRemoteDataSourceImpl
import tw.y_studio.ptt.source.remote.post.PostRemoteDataSourceImpl
import tw.y_studio.ptt.source.remote.rank.IPostRankRemoteDataSource
import tw.y_studio.ptt.source.remote.rank.PostRankRemoteDataSourceImpl
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
        val postRankAPI by lazy { PostRankAPI() }
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
        val postRemoteDataSource: IPostRemoteDataSource by lazy {
            PostRemoteDataSourceImpl(API.postAPI)
        }
        val postRankRemoteDataSource: IPostRankRemoteDataSource by lazy {
            PostRankRemoteDataSourceImpl(API.postRankAPI)
        }
    }
}
