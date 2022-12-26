package cc.ptt.android.data.di

import cc.ptt.android.common.network.api.RetrofitServiceProvider
import cc.ptt.android.common.network.api.TokenInterceptor
import cc.ptt.android.common.network.api.apihelper.ApiHelper
import cc.ptt.android.common.security.AESKeyStoreHelper
import cc.ptt.android.common.security.AESKeyStoreHelperImpl
import cc.ptt.android.data.ApiHelperImpl
import cc.ptt.android.data.TokenInterceptorImpl
import cc.ptt.android.data.apiservices.article.ArticleApi
import cc.ptt.android.data.apiservices.board.BoardApi
import cc.ptt.android.data.apiservices.user.UserApi
import org.koin.dsl.module

val apiModules = module {
    factory <AESKeyStoreHelper> { AESKeyStoreHelperImpl(get()) }
    factory <ApiHelper> { ApiHelperImpl(get()) }
    single <TokenInterceptor> { TokenInterceptorImpl(get()) }
    factory { RetrofitServiceProvider(get(), get()) }
    single <BoardApi> { get<RetrofitServiceProvider>().create(BoardApi::class.java) }
    single <UserApi> { get<RetrofitServiceProvider>().create(UserApi::class.java) }
    single <ArticleApi> { get<RetrofitServiceProvider>().create(ArticleApi::class.java) }
}
