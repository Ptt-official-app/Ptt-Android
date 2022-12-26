package cc.ptt.android.home.favoriteboards

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import cc.ptt.android.R
import cc.ptt.android.articlelist.ArticleListFragment
import cc.ptt.android.base.BaseFragment
import cc.ptt.android.common.ClickFix
import cc.ptt.android.common.CustomLinearLayoutManager
import cc.ptt.android.common.dragitemmove.ItemMoveCallback
import cc.ptt.android.common.dragitemmove.StartDragListener
import cc.ptt.android.databinding.FavoriteBoardsFragmentLayoutBinding
import cc.ptt.android.searchboards.SearchBoardsFragment
import cc.ptt.android.utils.observeNotNull
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteBoardsFragment : BaseFragment() {
    private var _binding: FavoriteBoardsFragmentLayoutBinding? = null
    private val binding get() = _binding
    private val mClickFix = ClickFix()
    private var editMode = false
    private var mStartDragListener: StartDragListener? = null
    private var touchHelper: ItemTouchHelper? = null

    private val viewModel: FavoriteBoardsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FavoriteBoardsFragmentLayoutBinding.inflate(inflater, container, false).apply {
            _binding = this
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {

            hotBoardsFragmentSearch.setOnClickListener(
                View.OnClickListener {
                    if (isEditMode()) {
                        val mm: Toast = Toast.makeText(
                            context,
                            R.string.attion_close_edit_mode,
                            Toast.LENGTH_SHORT
                        )
                        mm.setGravity(Gravity.CENTER, 0, 0)
                        mm.show()
                        return@OnClickListener
                    }
                    try {
                        loadFragmentNoAnim(
                            SearchBoardsFragment.newInstance(), currentFragment
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            )

            hotBoardsFragmentEdit.setOnClickListener(
                View.OnClickListener {
                    if (viewModel.loadingState.value == true) return@OnClickListener
                    editMode = !editMode
                    (binding?.hotBoardsFragmentRecyclerView?.adapter as FavoriteBoardsListAdapter).let {
                        it.setEditMode(editMode)
                        it.notifyDataSetChanged()
                    }

                    if (editMode) {
                        hotBoardsFragmentEdit.setColorFilter(
                            currentActivity
                                .resources
                                .getColor(R.color.tangerine)
                        )
                    } else {
                        hotBoardsFragmentEdit.setColorFilter(
                            currentActivity
                                .resources
                                .getColor(R.color.slateGrey)
                        )
                    }
                    if (!editMode) {
                        // UpdateBoardSort()
                    }
                }
            )

            mStartDragListener = object : StartDragListener {
                override fun requestDrag(viewHolder: RecyclerView.ViewHolder?) {
                    touchHelper?.startDrag(viewHolder!!)
                }
            }

            hotBoardsFragmentRecyclerView.apply {
                adapter = FavoriteBoardsListAdapter(
                    viewModel.data,
                    this@FavoriteBoardsFragment.mStartDragListener!!
                )

                val callback: ItemTouchHelper.Callback = ItemMoveCallback((adapter as FavoriteBoardsListAdapter))
                touchHelper = ItemTouchHelper(callback)
                touchHelper?.attachToRecyclerView(this)

                val layoutManager = CustomLinearLayoutManager(context)
                layoutManager.orientation = RecyclerView.VERTICAL
                setHasFixedSize(true)
                setLayoutManager(layoutManager)
                (adapter as FavoriteBoardsListAdapter).setOnItemClickListener(
                    object : FavoriteBoardsListAdapter.OnItemClickListener {
                        override fun onItemClick(view: View?, position: Int) {
                            if (mClickFix.isFastDoubleClick) return
                            if (!editMode) {
                                viewModel.data[position].let {
                                    loadFragment(
                                        ArticleListFragment.newInstance(
                                            Bundle().apply {
                                                putString("title", it.boardName)
                                                putString("subtitle", it.subtitle)
                                                putString("board_id", it.boardId)
                                            }
                                        ),
                                        currentFragment
                                    )
                                }
                            }
                        }
                    })

                (adapter as FavoriteBoardsListAdapter).setDislikeOnClickListener(
                    View.OnClickListener { v ->
                        if (mClickFix.isFastDoubleClick) return@OnClickListener
                        (v.tag as Int).let {
                            viewModel.data[it].let { data ->
                                viewModel.deleteBoard(data)
                            }
                        }
                    }
                )

                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                        val totalItemCount = layoutManager.itemCount
                        if (lastVisibleItem >= totalItemCount - 30) {
                            viewModel.loadNextData()
                        }
                    }
                })
            }

            hotBoardsFragmentRefreshLayout.apply {
                setColorSchemeResources(
                    android.R.color.holo_red_light,
                    android.R.color.holo_blue_light,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light
                )
                setOnRefreshListener(
                    OnRefreshListener {
                        viewModel.loadData()
                    }
                )
            }

            LocalBroadcastManager.getInstance(requireContext())
                .registerReceiver(mMessageReceiver, IntentFilter("ptt-favorite-change"))
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

    private val mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // Get extra data included in the Intent
            val message = intent.getStringExtra("message")
            if (message == "change") {
                viewModel.loadData()
            }
            Log.d("receiver", "Got message: $message")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(mMessageReceiver)
    }

    override fun onAnimOver() {
        viewModel.loadData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun isEditMode(): Boolean {
        return editMode
    }

    fun scrollToTop() {
        binding?.hotBoardsFragmentRecyclerView?.scrollToPosition(0)
    }

    companion object {
        @JvmStatic
        fun newInstance(): FavoriteBoardsFragment {
            val args = Bundle()
            val fragment = FavoriteBoardsFragment()
            fragment.arguments = args
            return fragment
        }

        @JvmStatic
        fun newInstance(args: Bundle?): FavoriteBoardsFragment {
            val fragment = FavoriteBoardsFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
