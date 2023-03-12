package cc.ptt.android.home.hotarticle

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import cc.ptt.android.R
import cc.ptt.android.base.BaseFragment
import cc.ptt.android.common.CustomLinearLayoutManager
import cc.ptt.android.common.RecyclerItemClickListener
import cc.ptt.android.common.StringUtils.notNullString
import cc.ptt.android.common.stickyheader.StickyHeaderItemDecorator
import java.util.ArrayList
import java.util.HashMap

class HotArticleFilterFragment : BaseFragment() {
    private var mRecyclerView: RecyclerView? = null
    private var mSwipeRefreshLayout: SwipeRefreshLayout? = null
    private var mAdapter: HotArticleFilterAdapter? = null
    private val data: MutableList<Map<String, Any>>? = ArrayList()
    private var title = ""
    private val board_list: MutableList<String> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.hot_article_list_fragment_layout, container, false)
        mRecyclerView = findViewById<RecyclerView>(R.id.article_list_fragment_recycler_view)
        mSwipeRefreshLayout =
            findViewById<SwipeRefreshLayout>(R.id.article_list_fragment_refresh_layout)
        val bundle = arguments // 取得Bundle
        title = bundle!!.getString("title", "ALL")
        board_list.addAll(bundle.getStringArrayList("BoardList")!!)
        mAdapter = HotArticleFilterAdapter(requireContext(), data!!)
        val layoutManager = CustomLinearLayoutManager(context)
        layoutManager.orientation = RecyclerView.VERTICAL
        mRecyclerView!!.setHasFixedSize(true)
        mRecyclerView!!.layoutManager = layoutManager
        mRecyclerView!!.adapter = mAdapter
        val decorator = StickyHeaderItemDecorator(mAdapter!!)
        decorator.attachToRecyclerView(mRecyclerView)
        mSwipeRefreshLayout!!.setColorSchemeResources(
            android.R.color.holo_red_light,
            android.R.color.holo_blue_light,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light
        )
        mSwipeRefreshLayout!!.isEnabled = false
        mSwipeRefreshLayout!!.setOnRefreshListener { loadData() }
        mRecyclerView!!.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                    val totalItemCount = layoutManager.itemCount
                    if (!GattingData) if (lastVisibleItem >= totalItemCount - 30) {
                        // loadNextData();
                    }
                }
            })
        val recyclerItemClickListener = RecyclerItemClickListener(
            mRecyclerView!!,
            object : RecyclerItemClickListener.OnItemClickListener {
                override fun onItemClick(view: View?, position: Int) {
                    // System.out.println("onItemClick " + adapter.getItem(position));
                    if (position > 0) {
                        if (!notNullString(data[position]["title"])
                            .equals(title, ignoreCase = true)
                        ) {
                            val intent = Intent("puty-hot-HotArticle-change")
                            // You can also include some extra data.
                            val board = notNullString(
                                data[position]["title"]
                            )
                            if (board.equals("ALL", ignoreCase = true)) {
                                intent.putExtra("message", "")
                            } else {
                                intent.putExtra(
                                    "message",
                                    notNullString(
                                        data[position]["title"]
                                    )
                                )
                            }
                            context?.let {
                                LocalBroadcastManager.getInstance(it)
                                    .sendBroadcast(intent)
                            }
                        }
                        requireActivity().onBackPressed()
                    } else {
                        requireActivity().onBackPressed()
                    }
                }

                override fun onItemLongClick(view: View?, position: Int) {
                    // System.out.println("onItemLongClick " + position);
                }
            }
        )
        recyclerItemClickListener.setDecorator(decorator)
        mRecyclerView?.addOnItemTouchListener(recyclerItemClickListener)
        mAdapter?.setMoreClickListen { requireActivity().onBackPressed() }
        return view
    }

    override fun onAnimOver() {
        loadData()
    }

    private val GattingData = false
    private fun loadData() {
        if (GattingData) return
        data!!.clear()
        for (i in -1 until board_list.size) {
            val mm: MutableMap<String, Any> = HashMap()
            if (i == -1) {
                mm["title"] = title
                mm["type"] = "title"
            } else {
                mm["title"] = board_list[i]
                mm["select"] = board_list[i].equals(title, ignoreCase = true)
            }
            data.add(mm)
            if (i == -1) {
                mAdapter!!.notifyDataSetChanged()
            }
        }
        mAdapter!!.notifyItemRangeInserted(1, 1 + board_list.size)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        data?.clear()
    }

    companion object {
        fun newInstance(): HotArticleFilterFragment {
            val args = Bundle()
            val fragment = HotArticleFilterFragment()
            fragment.arguments = args
            return fragment
        }

        fun newInstance(args: Bundle?): HotArticleFilterFragment {
            val fragment = HotArticleFilterFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
