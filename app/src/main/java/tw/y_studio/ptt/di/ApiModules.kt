package tw.y_studio.ptt.di

import org.koin.dsl.module
import tw.y_studio.ptt.api.PopularBoardListAPI
import tw.y_studio.ptt.api.PostAPI
import tw.y_studio.ptt.api.SearchBoardAPI

val apiModules = module {
    factory { PopularBoardListAPI() }
    factory { SearchBoardAPI() }
    factory { PostAPI() }
}
