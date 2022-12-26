package cc.ptt.android.home.hotarticle

import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import cc.ptt.android.articleread.ArticleReadFragment
import cc.ptt.android.base.BaseFragment
import cc.ptt.android.common.ClickFix
import cc.ptt.android.common.CustomLinearLayoutManager
import cc.ptt.android.common.stickyheader.StickyHeaderItemDecorator
import cc.ptt.android.databinding.HotArticleListFragmentLayoutBinding
import cc.ptt.android.domain.model.ui.hotarticle.HotArticleUI
import cc.ptt.android.domain.model.ui.hotarticle.HotArticleUIType
import cc.ptt.android.utils.observeNotNull
import org.koin.androidx.viewmodel.ext.android.viewModel

class HotArticleListFragment : BaseFragment() {

    private var _binding: HotArticleListFragmentLayoutBinding? = null
    private val binding get() = _binding!!

    private val mRecyclerView: RecyclerView get() = binding.articleListFragmentRecyclerView
    private val mSwipeRefreshLayout: SwipeRefreshLayout get() = binding.articleListFragmentRefreshLayout
    private lateinit var mAdapter: HotArticleListAdapter
    private val mClickFix = ClickFix()

    private val viewModel: HotArticleListViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return HotArticleListFragmentLayoutBinding.inflate(inflater, container, false).apply {
            _binding = this
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    override fun onAnimOver() {
        super.onAnimOver()
        loadData(false)
    }

    private fun initUI() {
        mAdapter = HotArticleListAdapter(currentActivity, viewModel.data)

        val layoutManager = CustomLinearLayoutManager(context)
        layoutManager.orientation = RecyclerView.VERTICAL
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = layoutManager
        mRecyclerView.adapter = mAdapter

        val decorator = StickyHeaderItemDecorator(mAdapter)
        decorator.attachToRecyclerView(mRecyclerView)

        mSwipeRefreshLayout.setColorSchemeResources(
            R.color.holo_red_light,
            R.color.holo_blue_light,
            R.color.holo_green_light,
            R.color.holo_orange_light
        )

        mSwipeRefreshLayout.setOnRefreshListener { loadData(false) }

        mRecyclerView.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                    val totalItemCount = layoutManager.itemCount
                    if (lastVisibleItem >= totalItemCount - 30) {
                        // loadData(true)
                    }
                }
            })

        mAdapter.setOnItemClickListener(object : HotArticleListAdapter.OnItemClickListener {
            override fun onItemClick(view: View?, position: Int, data: HotArticleUI) {
                if (mClickFix.isFastDoubleClick) return
                val item = viewModel.data.getOrNull(position) ?: return

                when (item.type) {
                    HotArticleUIType.TITLE -> {
                        // TODO
                    }
                    HotArticleUIType.NORMAL -> {
                        item.readed = true
                        mAdapter.setHighLightUrl(item.url)
                        mAdapter.notifyDataSetChanged()
                        loadFragment(
                            ArticleReadFragment.newInstance(
                                Bundle().apply {
                                    putParcelable("article", item.toArticle())
                                    putString("boardName", item.board)
                                }
                            ),
                            currentFragment
                        )
                    }
                    else -> Unit
                }
            }
        })

        mAdapter.setMoreClickListen(
            View.OnClickListener {
                if (mClickFix.isFastDoubleClick) return@OnClickListener
                // TODO
            }
        )

        viewModel.run {
            observeNotNull(loadingState) {
                mSwipeRefreshLayout.isRefreshing = it
                mAdapter.notifyDataSetChanged()
                Log.e(TAG, "loadingState: $it ${viewModel.data.size}")
            }
            observeNotNull(errorMessage) {
                Log.e(TAG, "Error: $it")
            }
        }
    }

    private fun loadData(getNext: Boolean) {
        viewModel.loadData(getNext)
    }

    fun scrollToTop() {
        mRecyclerView.scrollToPosition(0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private val TAG = HotArticleListFragment.javaClass.simpleName

        fun newInstance(): HotArticleListFragment {
            return HotArticleListFragment().apply {
                arguments = Bundle()
            }
        }

        @JvmStatic
        fun newInstance(args: Bundle?): HotArticleListFragment {
            return HotArticleListFragment().apply {
                arguments = args
            }
        }
    }
}
