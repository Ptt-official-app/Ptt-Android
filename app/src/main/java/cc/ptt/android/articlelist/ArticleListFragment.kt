package cc.ptt.android.articlelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import cc.ptt.android.Navigation
import cc.ptt.android.R
import cc.ptt.android.base.BaseFragment
import cc.ptt.android.common.ClickFix
import cc.ptt.android.common.CustomLinearLayoutManager
import cc.ptt.android.data.model.remote.board.article.Article
import cc.ptt.android.databinding.ArticleListFragmentLayoutBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ArticleListFragment : BaseFragment() {

    private var _binding: ArticleListFragmentLayoutBinding? = null
    private val binding get() = _binding!!
    private lateinit var articleListAdapter: ArticleListAdapter

    private var boardName = ""
    private var boardSubName = ""
    private var boardId = ""
    private val mClickFix = ClickFix()

    private val viewModel: ArticleListViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments // 取得Bundle
        boardName = bundle?.getString(KEY_TITLE, getString(R.string.board_list_title_empty)).orEmpty()
        boardSubName = bundle?.getString(KEY_SUBTITLE, getString(R.string.board_list_subtitle_empty)).orEmpty()
        boardId = bundle?.getString(KEY_BOARD_ID, getString(R.string.board_list_bid_empty)).orEmpty()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ArticleListFragmentLayoutBinding.inflate(inflater, container, false).apply {
            _binding = this
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        initObserver()
    }

    private fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.data.collectLatest {
                articleListAdapter.updateArticleList(it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.actionState.collect {
                when (it) {
                    is ArticleListViewModel.ActionState.ShowErrorMsg -> {
                        Toast.makeText(requireContext(), it.msg, Toast.LENGTH_SHORT).show()
                    }
                    is ArticleListViewModel.ActionState.SwitchToArticleReadPage -> {
                        Navigation.switchToArticleReadPage(requireActivity(), it.article, boardName)
                    }
                    ArticleListViewModel.ActionState.SwitchToArticleListSearchPage -> {
                        Navigation.switchToArticleListSearchPage(requireActivity())
                    }
                    ArticleListViewModel.ActionState.SwitchToPostArticlePage -> {
                        Navigation.switchToPostArticlePage(requireActivity())
                    }
                }
            }
        }

        viewModel.loadingStateLiveData.observe(viewLifecycleOwner) {
            binding.articleListFragmentRefreshLayout.isRefreshing = it
        }
    }

    private fun initView() {
        binding.apply {
            articleListFragmentTextViewTitle.text = boardName
            articleListFragmentTextViewSubtitle.text = boardSubName

            articleListFragmentRecyclerView.apply {
                setHasFixedSize(true)
                val layoutManager = CustomLinearLayoutManager(context).apply {
                    orientation = RecyclerView.VERTICAL
                }
                setLayoutManager(layoutManager)
                articleListAdapter = ArticleListAdapter(
                    mutableListOf(),
                    object : ArticleListAdapter.OnItemClickListener {
                        override fun onItemClick(article: Article) {
                            if (mClickFix.isFastDoubleClick) return
                            viewModel.switchToArticleReadPage(article)
                        }
                    }
                )
                adapter = articleListAdapter

                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                        val totalItemCount = layoutManager.itemCount
                        if (lastVisibleItem >= totalItemCount - 30) {
                            viewModel.loadNextData(boardId)
                        }
                    }
                })
            }

            articleListFragmentRefreshLayout.apply {
                setColorSchemeResources(
                    android.R.color.holo_red_light,
                    android.R.color.holo_blue_light,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light
                )
                setOnRefreshListener {
                    viewModel.loadData(boardId)
                }
            }

            articleListFragmentBottomNavigation.setOnItemSelectedListener {
                return@setOnItemSelectedListener when (it.itemId) {
                    R.id.article_list_navigation_item_refresh -> {
                        viewModel.loadData(boardId)
                        false
                    }
                    R.id.article_list_navigation_item_post -> {
                        viewModel.switchToPostArticlePage()
                        false
                    }
                    R.id.article_list_navigation_item_search -> {
                        viewModel.switchToArticleListSearchPage()
                        false
                    }
                    R.id.article_list_navigation_item_info -> {
                        viewModel.switchToBoardInfoPage()
                        false
                    }
                    else -> true
                }
            }

            articleReadItemHeaderImageViewBack.setOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    override fun onAnimFinished() {
        viewModel.loadData(boardId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val KEY_TITLE = "title"
        const val KEY_SUBTITLE = "subtitle"
        const val KEY_BOARD_ID = "board_id"
    }
}
