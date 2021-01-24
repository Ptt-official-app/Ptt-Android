package tw.y_studio.ptt.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import tw.y_studio.ptt.ui.hot_board.HotBoardsViewModel

val viewModelModules = module {
    viewModel { HotBoardsViewModel(get(), get(named("IO"))) }
}
