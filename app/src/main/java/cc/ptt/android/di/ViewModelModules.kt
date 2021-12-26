package cc.ptt.android.di

import cc.ptt.android.presentation.articlelist.ArticleListViewModel
import cc.ptt.android.presentation.articleread.ArticleReadViewModel
import cc.ptt.android.presentation.favoriteboards.FavoriteBoardsViewModel
import cc.ptt.android.presentation.hotarticle.HotArticleListViewModel
import cc.ptt.android.presentation.hotboard.HotBoardsViewModel
import cc.ptt.android.presentation.login.LoginPageViewModel
import cc.ptt.android.presentation.searchboards.SearchBoardsModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val viewModelModules = module {
    viewModel { HotBoardsViewModel(get(), get(named("IO"))) }
    viewModel { ArticleListViewModel(get(), get(), get(named("IO"))) }
    viewModel { ArticleReadViewModel(get(), get(), get(named("IO"))) }
    viewModel { LoginPageViewModel(get()) }
    viewModel { SearchBoardsModel(get(), get(named("IO"))) }
    viewModel { FavoriteBoardsViewModel(get(), get(named("IO")), get()) }
    viewModel { HotArticleListViewModel(get(), get(named("UI")), get(named("IO"))) }
}
