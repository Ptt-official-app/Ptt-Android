package tw.y_studio.ptt.ui.hot_board

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import tw.y_studio.ptt.adapter.HotBoardsListAdapter
import tw.y_studio.ptt.api.model.board.hot_board.HotBoardsItem
import tw.y_studio.ptt.databinding.HotBoardsFragmentLayoutBinding
import tw.y_studio.ptt.fragment.search_boards.SearchBoardsFragment
import tw.y_studio.ptt.ui.BaseFragment
import tw.y_studio.ptt.ui.ClickFix
import tw.y_studio.ptt.ui.CustomLinearLayoutManager
import tw.y_studio.ptt.ui.article.list.ArticleListFragment
import tw.y_studio.ptt.utils.observeNotNull

class HotBoardsFragment : BaseFragment() {
    private var _binding: HotBoardsFragmentLayoutBinding? = null
    private val binding get() = _binding
    private val mClickFix = ClickFix()

    private val viewModel: HotBoardsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return HotBoardsFragmentLayoutBinding.inflate(inflater, container, false).apply {
            _binding = this
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {

            hotBoardsFragmentSearch.setOnClickListener(
                View.OnClickListener {
                    loadFragmentNoAnim(
                        SearchBoardsFragment.newInstance(), currentFragment
                    )
                }
            )

            hotBoardsFragmentRecyclerView.apply {
                val layoutManager = CustomLinearLayoutManager(context)
                layoutManager.orientation = RecyclerView.VERTICAL
                setHasFixedSize(true)
                this.layoutManager = layoutManager
                adapter = HotBoardsListAdapter(
                    viewModel.data,
                    object : HotBoardsListAdapter.OnItemClickListener {
                        override fun onItemClick(item: HotBoardsItem) {
                            if (mClickFix.isFastDoubleClick) return
                            loadFragment(
                                ArticleListFragment.newInstance(
                                    Bundle().apply {
                                        putString("title", item.boardName)
                                        putString("subtitle", item.subtitle)
                                        putString("board_id", item.boardId)
                                    }
                                ),
                                currentFragment
                            )
                        }
                    }
                )
            }
            hotBoardsFragmentRefreshLayout.apply {
                setColorSchemeResources(
                    android.R.color.holo_red_light,
                    android.R.color.holo_blue_light,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light
                )
                setOnRefreshListener {
                    viewModel.loadData()
                }
            }
        }
        viewModel.apply {
            observeNotNull(loadingState) {
                binding?.hotBoardsFragmentRefreshLayout?.isRefreshing = it
                binding?.hotBoardsFragmentRecyclerView?.adapter?.notifyDataSetChanged()
            }
            observeNotNull(errorMessage) {
                Log.e("errorMessage", it)
            }
        }
    }

    override fun onAnimOver() {
        viewModel.loadData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun scrollToTop() {
        Log.d("scrollToTop", "scrollToTop: hotBoardsFragmentRecyclerView")
        binding?.hotBoardsFragmentRecyclerView?.scrollToPosition(0)
    }

    companion object {
        fun newInstance(): HotBoardsFragment {
            val args = Bundle()
            val fragment = HotBoardsFragment()
            fragment.arguments = args
            return fragment
        }

        @JvmStatic
        fun newInstance(args: Bundle?): HotBoardsFragment {
            val fragment = HotBoardsFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
