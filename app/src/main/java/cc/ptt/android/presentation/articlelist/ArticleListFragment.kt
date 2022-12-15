package cc.ptt.android.presentation.articlelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import cc.ptt.android.R
import cc.ptt.android.common.utils.Log
import cc.ptt.android.data.model.remote.board.article.Article
import cc.ptt.android.databinding.ArticleListFragmentLayoutBinding
import cc.ptt.android.presentation.articleread.ArticleReadFragment
import cc.ptt.android.presentation.articlesearch.ArticleListSearchFragment
import cc.ptt.android.presentation.base.BaseFragment
import cc.ptt.android.presentation.common.ClickFix
import cc.ptt.android.presentation.common.CustomLinearLayoutManager
import cc.ptt.android.presentation.postarticle.PostArticleFragment
import cc.ptt.android.utils.observeNotNull
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticleListFragment : BaseFragment() {

    private var _binding: ArticleListFragmentLayoutBinding? = null
    private val binding get() = _binding!!

    private var boardName = ""
    private var boardSubName = ""
    private var boardId = ""
    private val mClickFix = ClickFix()

    private val articleListViewModel: ArticleListViewModel by viewModels()

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
                                        putString("boardName", boardName)
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
