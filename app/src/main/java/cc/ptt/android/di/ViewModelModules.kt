package cc.ptt.android.di

import cc.ptt.android.presentation.articlelist.ArticleListViewModel
import cc.ptt.android.presentation.articleread.ArticleReadViewModel
import cc.ptt.android.presentation.home.favoriteboards.FavoriteBoardsViewModel
import cc.ptt.android.presentation.home.hotarticle.HotArticleListViewModel
import cc.ptt.android.presentation.home.hotboard.HotBoardsViewModel
import cc.ptt.android.presentation.home.setting.SettingViewModel
import cc.ptt.android.presentation.login.LoginPageViewModel
import cc.ptt.android.presentation.searchboards.SearchBoardsModel
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

@FlowPreview
val viewModelModules = module {
    viewModel { ArticleListViewModel(get()) }
    viewModel { ArticleReadViewModel(get(), get(), get()) }
    viewModel { FavoriteBoardsViewModel(get(), get(named(MainPreferences))) }
    viewModel { HotArticleListViewModel(get()) }
    viewModel { HotBoardsViewModel(get()) }
    viewModel { SettingViewModel(get(), get()) }
    viewModel { LoginPageViewModel(get()) }
    viewModel { SearchBoardsModel(get()) }
}
