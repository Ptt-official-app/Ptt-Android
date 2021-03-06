package tw.y_studio.ptt.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import tw.y_studio.ptt.fragment.favorite_boards.FavoriteBoardsViewModel
import tw.y_studio.ptt.fragment.login.LoginPageViewModel
import tw.y_studio.ptt.fragment.search_boards.SearchBoardsModel
import tw.y_studio.ptt.ui.article.list.ArticleListViewModel
import tw.y_studio.ptt.ui.article.read.ArticleReadViewModel
import tw.y_studio.ptt.ui.hot_board.HotBoardsViewModel

val viewModelModules = module {
    viewModel { HotBoardsViewModel(get(), get(named("IO"))) }
    viewModel { ArticleListViewModel(get(), get(), get(named("IO"))) }
    viewModel { ArticleReadViewModel(get(), get(), get(named("IO"))) }
    viewModel { LoginPageViewModel(get()) }
    viewModel { SearchBoardsModel(get(), get(named("IO"))) }
    viewModel { FavoriteBoardsViewModel(get(), get(named("IO")), get()) }
}
