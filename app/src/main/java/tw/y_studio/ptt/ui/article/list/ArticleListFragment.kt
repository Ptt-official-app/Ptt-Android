package tw.y_studio.ptt.ui.article.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel
import tw.y_studio.ptt.R
import tw.y_studio.ptt.api.model.board.article.Article
import tw.y_studio.ptt.databinding.ArticleListFragmentLayoutBinding
import tw.y_studio.ptt.fragment.ArticleListSearchFragment
import tw.y_studio.ptt.fragment.PostArticleFragment
import tw.y_studio.ptt.ui.BaseFragment
import tw.y_studio.ptt.ui.ClickFix
import tw.y_studio.ptt.ui.CustomLinearLayoutManager
import tw.y_studio.ptt.ui.article.read.ArticleReadFragment
import tw.y_studio.ptt.utils.Log
import tw.y_studio.ptt.utils.observeNotNull

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
        boardName = bundle?.getString("title", getString(R.string.board_list_title_empty)) ?: ""
        boardSubName = bundle?.getString("subtitle", getString(R.string.board_list_subtitle_empty))
            ?: ""
        boardId = bundle?.getString("board_id", getString(R.string.board_list_bid_empty)) ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
                            loadFragment(
                                ArticleReadFragment.newInstance(
                                    Bundle().apply {
                                        putParcelable("article", article)
                                    }
                                ),
                                currentFragment
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
                            loadFragment(
                                PostArticleFragment.newInstance(), currentFragment
                            )
                            return@OnNavigationItemSelectedListener false
                        }
                        R.id.article_list_navigation_item_search -> loadFragment(
                            ArticleListSearchFragment.newInstance(),
                            currentFragment
                        )
                        R.id.article_list_navigation_item_info -> {
                        }
                        else -> {
                        }
                    }
                    false
                }
            )
            articleReadItemHeaderImageViewBack.setOnClickListener {
                currentActivity.onBackPressed()
            }
            articleListFragmentTextViewSubtitle.text = boardSubName
        }
        articleListViewModel.run {
            observeNotNull(getLoadingStateLiveData()) { isLoading ->
                binding.articleListFragmentRefreshLayout.isRefreshing = isLoading
                binding.articleListFragmentRecyclerView.adapter?.notifyDataSetChanged()
            }

            observeNotNull(getErrorLiveData()) { e ->
                Log("ArticleListFragment", "Error : $e")
                Toast.makeText(currentActivity, "Error : $e", Toast.LENGTH_SHORT).show()
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
        fun newInstance(): ArticleListFragment {
            val args = Bundle()
            val fragment = ArticleListFragment()
            fragment.arguments = args
            return fragment
        }

        @JvmStatic
        fun newInstance(args: Bundle?): ArticleListFragment {
            val fragment = ArticleListFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
