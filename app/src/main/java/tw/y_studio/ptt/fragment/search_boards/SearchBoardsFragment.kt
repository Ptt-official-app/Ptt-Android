package tw.y_studio.ptt.fragment.search_boards

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import tw.y_studio.ptt.adapter.SearchBoardsAdapter
import tw.y_studio.ptt.api.model.board.search_board.SearchBoardsItem
import tw.y_studio.ptt.databinding.SearchBoardsFragmentLayoutBinding
import tw.y_studio.ptt.ui.BaseFragment
import tw.y_studio.ptt.ui.ClickFix
import tw.y_studio.ptt.ui.CustomLinearLayoutManager
import tw.y_studio.ptt.ui.article.list.ArticleListFragment
import tw.y_studio.ptt.utils.KeyboardUtils
import tw.y_studio.ptt.utils.observeNotNull

class SearchBoardsFragment : BaseFragment() {
    private var _binding: SearchBoardsFragmentLayoutBinding? = null
    private val binding get() = _binding
    private val mClickFix = ClickFix()

    private val viewModel: SearchBoardsModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return SearchBoardsFragmentLayoutBinding.inflate(inflater, container, false).apply {
            _binding = this
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            searchBoardsItemImageViewLike.setOnClickListener {
                if (mClickFix.isFastDoubleClick(300)) return@setOnClickListener
                if (searchBoardsFragmentEditTextSearch.text.isEmpty()) {
                    currentActivity.onBackPressed()
                } else {
                    searchBoardsFragmentEditTextSearch.text.clear()
                }
            }

            searchBoardsFragmentRefreshLayout.apply {
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

            searchBoardsFragmentRecyclerView.apply {
                val layoutManager = CustomLinearLayoutManager(context)
                layoutManager.orientation = RecyclerView.VERTICAL
                setHasFixedSize(true)
                this.layoutManager = layoutManager

                adapter = SearchBoardsAdapter(
                    viewModel.data,
                    object : SearchBoardsAdapter.OnItemClickListener {
                        override fun onItemClick(item: SearchBoardsItem) {
                            if (mClickFix.isFastDoubleClick) return

                            loadFragment(
                                ArticleListFragment.newInstance(
                                    Bundle().apply {
                                        putString("title", item.title as? String)
                                        putString("subtitle", item.subtitle as? String)
                                        putString("board_id", item.boardId as? String)
                                    }
                                ),
                                currentFragment
                            )
                        }
                    }
                )
                (adapter as SearchBoardsAdapter).setLikeOnClickListener(
                    View.OnClickListener {
                        if (viewModel.loadingState.value == false && mClickFix.isFastDoubleClick) {
                            val position: Int = it.tag as Int
                            viewModel.changeBoardLikeSate(position)
                        }
                    }
                )
            }

            searchBoardsFragmentEditTextSearch.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    viewModel.searchBoard(s.toString())
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })
        }

        viewModel.apply {
            observeNotNull(loadingState) {
                binding?.searchBoardsFragmentRefreshLayout?.isRefreshing = it
                binding?.searchBoardsFragmentRecyclerView?.adapter?.notifyDataSetChanged()
            }
            observeNotNull(errorMessage) {
                Log.e("errorMessage", it)
            }
        }
    }

    private fun sendChangeMessage() {
        Log.d("sender", "Broadcasting message")
        val intent: Intent = Intent("ptt-favorite-change")
        // You can also include some extra data.
        intent.putExtra("message", "change")
        context?.let { LocalBroadcastManager.getInstance(it).sendBroadcast(intent) }
    }

    override fun onAnimOver() {
        viewModel.loadData()
        binding?.searchBoardsFragmentEditTextSearch?.let {
            it.requestFocus()
            KeyboardUtils.showSoftInput(requireActivity())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        KeyboardUtils.hideSoftInput(requireActivity())
        _binding = null
        sendChangeMessage()
    }

    companion object {
        @JvmStatic
        fun newInstance(): SearchBoardsFragment {
            val args = Bundle()
            val fragment = SearchBoardsFragment()
            fragment.arguments = args
            return fragment
        }

        @JvmStatic
        fun newInstance(args: Bundle?): SearchBoardsFragment {
            val fragment = SearchBoardsFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
