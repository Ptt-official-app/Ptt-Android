package cc.ptt.android.articlelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import cc.ptt.android.Navigation
import cc.ptt.android.R
import cc.ptt.android.base.BaseFragment
import cc.ptt.android.common.ClickFix
import cc.ptt.android.common.CustomLinearLayoutManager
import cc.ptt.android.data.model.remote.board.article.Article
import cc.ptt.android.databinding.ArticleListFragmentLayoutBinding
import cc.ptt.android.utils.observeNotNull
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel

class ArticleListFragment : BaseFragment() {

    private var _binding: ArticleListFragmentLayoutBinding? = null
    private val binding get() = _binding!!

    private var boardName = ""
    private var boardSubName = ""
    private var boardId = ""
    private val mClickFix = ClickFix()

    private val articleListViewModel: ArticleListViewModel by viewModel()

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
        binding.apply {
            articleListFragmentTextViewTitle.text = boardName
            articleListFragmentRecyclerView.apply {
                setHasFixedSize(true)
                val layoutManager = CustomLinearLayoutManager(context).apply {
                    orientation = RecyclerView.VERTICAL
                }
                setLayoutManager(layoutManager)
                adapter = ArticleListAdapter(
                    articleListViewModel.data,
                    object : ArticleListAdapter.OnItemClickListener {
                        override fun onItemClick(article: Article) {
                            if (mClickFix.isFastDoubleClick) return
                            Navigation.switchToArticleReadPage(
                                requireActivity(),
                                article,
                                boardName
                            )
                        }
                    }
                )
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                        val totalItemCount = layoutManager.itemCount
                        if (lastVisibleItem >= totalItemCount - 30) {
                            articleListViewModel.loadNextData(boardId, boardName)
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
                setOnRefreshListener { articleListViewModel.loadData(boardId, boardName) }
            }
            articleListFragmentBottomNavigation.setOnNavigationItemSelectedListener(
                BottomNavigationView.OnNavigationItemSelectedListener { item ->
                    when (item.itemId) {
                        R.id.article_list_navigation_item_refresh -> {
                            articleListViewModel.loadData(boardId, boardName)
                            return@OnNavigationItemSelectedListener false
                        }
                        R.id.article_list_navigation_item_post -> {
                            Navigation.switchToPostArticlePage(requireActivity())
                            return@OnNavigationItemSelectedListener false
                        }
                        R.id.article_list_navigation_item_search -> {
                            Navigation.switchToArticleListSearchPage(requireActivity())
                        }
                        R.id.article_list_navigation_item_info -> {
                        }
                        else -> {
                        }
                    }
                    false
                }
            )
            articleReadItemHeaderImageViewBack.setOnClickListener {
                requireActivity().onBackPressed()
            }
            articleListFragmentTextViewSubtitle.text = boardSubName
        }
        articleListViewModel.run {
            observeNotNull(getLoadingStateLiveData()) { isLoading ->
                binding.articleListFragmentRefreshLayout.isRefreshing = isLoading
                binding.articleListFragmentRecyclerView.adapter?.notifyDataSetChanged()
            }

            observeNotNull(getErrorLiveData()) { e ->
                Toast.makeText(requireContext(), "Error : $e", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onAnimOver() {
        articleListViewModel.loadData(boardId, boardName)
        binding.articleListFragmentRecyclerView.adapter?.notifyDataSetChanged()
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
