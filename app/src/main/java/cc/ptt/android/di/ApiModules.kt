package cc.ptt.android.di

import cc.ptt.android.common.api.RetrofitServiceProvider
import cc.ptt.android.data.api.article.ArticleApi
import cc.ptt.android.data.api.board.BoardApi
import cc.ptt.android.data.api.user.UserApi
import org.koin.dsl.module

val apiModules = module {
    factory { RetrofitServiceProvider(get(), get()) }
    single <BoardApi> { get<RetrofitServiceProvider>().create(BoardApi::class.java) }
    single <UserApi> { get<RetrofitServiceProvider>().create(UserApi::class.java) }
    single <ArticleApi> { get<RetrofitServiceProvider>().create(ArticleApi::class.java) }
}

const val RetrofitClient = "RetrofitClient"
