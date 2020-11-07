package tw.y_studio.ptt

import tw.y_studio.ptt.api.PopularBoardListAPI
import tw.y_studio.ptt.api.popular.IPopularRemoteDataSource
import tw.y_studio.ptt.api.popular.PopularRemoteDataSourceImpl

object Injection {
    object API {
        val popularBoardListAPI by lazy {
            PopularBoardListAPI()
        }
    }

    object RemoteDataSource {
        val popularRemoteDataSource: IPopularRemoteDataSource by lazy {
            PopularRemoteDataSourceImpl(API.popularBoardListAPI)
        }
    }
}
