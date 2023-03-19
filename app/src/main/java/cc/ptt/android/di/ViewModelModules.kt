package cc.ptt.android.di

import cc.ptt.android.articlelist.ArticleListViewModel
import cc.ptt.android.articleread.ArticleReadViewModel
import cc.ptt.android.home.favoriteboards.FavoriteBoardsViewModel
import cc.ptt.android.home.hotarticle.HotArticleListViewModel
import cc.ptt.android.home.hotboard.HotBoardsViewModel
import cc.ptt.android.home.setting.SettingViewModel
import cc.ptt.android.login.LoginPageViewModel
import cc.ptt.android.searchboards.SearchBoardsModel
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@FlowPreview
val viewModelModules = module {
    viewModel { ArticleListViewModel(get(), get()) }
    viewModel { ArticleReadViewModel(get(), get(), get(), get(), get()) }
    viewModel { FavoriteBoardsViewModel(get(), get()) }
    viewModel { HotArticleListViewModel(get()) }
    viewModel { HotBoardsViewModel(get()) }
    viewModel { SettingViewModel(get(), get(), get()) }
    viewModel { LoginPageViewModel(get(), get()) }
    viewModel { SearchBoardsModel(get()) }
}
