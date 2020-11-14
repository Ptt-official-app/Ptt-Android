package tw.y_studio.ptt.UI.article.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.article_list_fragment_layout.*
import tw.y_studio.ptt.Fragment.ArticleListSearchFragment
import tw.y_studio.ptt.Fragment.ArticleReadFragment
import tw.y_studio.ptt.Fragment.PostArticleFragment
import tw.y_studio.ptt.R
import tw.y_studio.ptt.UI.BaseFragment
import tw.y_studio.ptt.UI.ClickFix
import tw.y_studio.ptt.UI.CustomLinearLayoutManager
import tw.y_studio.ptt.Utils.Log
import tw.y_studio.ptt.Utils.observeNotNull
import tw.y_studio.ptt.api.PostListAPIHelper
import tw.y_studio.ptt.model.PartialPost

class ArticleListFragment : BaseFragment() {

    private var boardName = ""
    private var boardSubName = ""
    private val mClickFix = ClickFix()

    private lateinit var articleListViewModel: ArticleListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments // 取得Bundle
        boardName = bundle?.getString("title", getString(R.string.board_list_title_empty)) ?: ""
        boardSubName = bundle?.getString("subtitle", getString(R.string.board_list_subtitle_empty)) ?: ""
        articleListViewModel = ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ArticleListViewModel(PostListAPIHelper(boardName)) as T
                }
            }
        ).get(ArticleListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.article_list_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        view.apply {
            article_list_fragment_textView_title.text = boardName
            article_list_fragment_recyclerView.apply {
                setHasFixedSize(true)
                val layoutManager = CustomLinearLayoutManager(context).apply {
                    orientation = RecyclerView.VERTICAL
                }
                setLayoutManager(layoutManager)
                adapter = ArticleListAdapter(
                    articleListViewModel.data,
                    object : ArticleListAdapter.OnItemClickListener {
                        override fun onItemClick(partialPost: PartialPost) {
                            if (mClickFix.isFastDoubleClick) return
                            loadFragment(
                                ArticleReadFragment.newInstance(
                                    Bundle().apply {
                                        putString("title", partialPost.title)
                                        putString("auth", partialPost.auth)
                                        putString("date", partialPost.date)
                                        putString("class", partialPost.category)
                                        putString("board", boardName)
                                        putString("url", partialPost.url)
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
                            articleListViewModel.loadNextData()
                        }
                    }
                })
            }
            article_list_fragment_refresh_layout.apply {
                setColorSchemeResources(
                    android.R.color.holo_red_light,
                    android.R.color.holo_blue_light,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light
                )
                setOnRefreshListener { articleListViewModel.loadData() }
            }
            article_list_fragment_bottom_navigation.setOnNavigationItemSelectedListener(
                BottomNavigationView.OnNavigationItemSelectedListener { item ->
                    when (item.itemId) {
                        R.id.article_list_navigation_item_refresh -> {
                            articleListViewModel.loadData()
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
            article_read_item_header_imageView_back.setOnClickListener {
                currentActivity.onBackPressed()
            }
            article_list_fragment_textView_subtitle.text = boardSubName
        }
        articleListViewModel.run {
            observeNotNull(getLoadingStateLiveData()) { isLoading ->
                article_list_fragment_refresh_layout.isRefreshing = isLoading
                article_list_fragment_recyclerView.adapter?.notifyDataSetChanged()
            }

            observeNotNull(getErrorLiveData()) { e ->
                Log("ArticleListFragment", "Error : $e")
                Toast.makeText(currentActivity, "Error : $e", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onAnimOver() {
        articleListViewModel.loadData()
        article_list_fragment_recyclerView.adapter?.notifyDataSetChanged()
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
