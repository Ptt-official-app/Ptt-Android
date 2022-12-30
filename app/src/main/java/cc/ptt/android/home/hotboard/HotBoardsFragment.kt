package cc.ptt.android.home.hotboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cc.ptt.android.articlelist.ArticleListFragment
import cc.ptt.android.base.BaseFragment
import cc.ptt.android.common.ClickFix
import cc.ptt.android.common.CustomLinearLayoutManager
import cc.ptt.android.data.model.remote.board.hotboard.HotBoardsItem
import cc.ptt.android.databinding.HotBoardsFragmentLayoutBinding
import cc.ptt.android.searchboards.SearchBoardsFragment
import cc.ptt.android.utils.observeNotNull
import org.koin.androidx.viewmodel.ext.android.viewModel

class HotBoardsFragment : BaseFragment() {
    private var _binding: HotBoardsFragmentLayoutBinding? = null
    private val binding get() = _binding
    private val mClickFix = ClickFix()

    private val viewModel: HotBoardsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = HotBoardsFragmentLayoutBinding.inflate(inflater, container, false).apply {
        _binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {

            hotBoardsFragmentSearch.setOnClickListener {
                loadFragmentNoAnim(
                    SearchBoardsFragment.newInstance(), currentFragment
                )
            }

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
                logger.d("HotBoardsFragment", "errorMessage $it")
            }
        }
    }

    override fun onAnimOver() {
        super.onAnimOver()
        viewModel.loadData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun scrollToTop() {
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
