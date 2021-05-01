package tw.y_studio.ptt.fragment.search_boards

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import tw.y_studio.ptt.adapter.SearchBoardsAdapter
import tw.y_studio.ptt.databinding.SearchBoardsFragmentLayoutBinding
import tw.y_studio.ptt.ui.BaseFragment
import tw.y_studio.ptt.ui.ClickFix
import tw.y_studio.ptt.ui.CustomLinearLayoutManager
import tw.y_studio.ptt.ui.article.list.ArticleListFragment
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
                    try {
                        val inputMethodManager = currentActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        inputMethodManager.hideSoftInputFromWindow(searchBoardsFragmentEditTextSearch?.windowToken, 0)
                    } catch (e: Exception) {
                    }
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
                        override fun onItemClick(item: Map<String, Any>) {
                            if (mClickFix.isFastDoubleClick) return

                            loadFragment(
                                ArticleListFragment.newInstance(
                                    Bundle().apply {
                                        putString("title", item.get("title") as? String)
                                        putString("subtitle", item.get("subtitle") as? String)
                                        putString("board_id", item.get("boardId") as? String)
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

    private fun closeSoftInput() {
        try {
            val inputMethodManager = currentActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(_binding?.root?.windowToken, 0)
        } catch (e: Exception) {
        }
    }

    override fun onAnimOver() {
        viewModel.loadData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        closeSoftInput()
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

/*   private void insertBoard(
            final String board,
            final String title,
            final String category,
            final int index,
            final int position) {
        r3 =
                new Runnable() {

                    public void run() {
                        runOnUI(
                                () -> {
                                    mSwipeRefreshLayout.setRefreshing(true);
                                });

                        GattingData = true;

                        FavoriteDBHelper mDBHelper =
                                new FavoriteDBHelper(getCurrentActivity(), "Favorite.db", null, 1);
                        try {
                            mDBHelper.insertBoard(board, title, category, index + 1);
                            runOnUI(
                                    () -> {
                                        data.get(position).put("like", true);
                                        mdapter.notifyItemChanged(position);
                                        myBoardIndex++;
                                        mSwipeRefreshLayout.setRefreshing(false);
                                    });

                            DebugUtils.Log("onAL", "insert over");
                        } catch (final Exception e) {
                            runOnUI(
                                    () -> {
                                        Toast.makeText(
                                                        getCurrentActivity(),
                                                        "Error : " + e.toString(),
                                                        Toast.LENGTH_SHORT)
                                                .show();
                                        mSwipeRefreshLayout.setRefreshing(false);
                                    });
                        } finally {
                            mDBHelper.close();
                        }

                        GattingData = false;
                    }
                };

        mThread3 = new HandlerThread("name");
        mThread3.start();
        mThreadHandler3 = new Handler(mThread3.getLooper());
        mThreadHandler3.post(r3);
    }

    private void deleteBoard(final String board, final int position) {
        r3 =
                new Runnable() {

                    public void run() {
                        runOnUI(
                                () -> {
                                    mSwipeRefreshLayout.setRefreshing(true);
                                });

                        GattingData = true;

                        FavoriteDBHelper mDBHelper =
                                new FavoriteDBHelper(getCurrentActivity(), "Favorite.db", null, 1);
                        try {
                            mDBHelper.delebyBoard(board);

                            runOnUI(
                                    () -> {
                                        data.get(position).put("like", false);
                                        mdapter.notifyItemChanged(position);
                                        myBoardIndex--;
                                        mSwipeRefreshLayout.setRefreshing(false);
                                    });

                            DebugUtils.Log("onAL", board + " delete over");
                        } catch (final Exception e) {
                            runOnUI(
                                    () -> {
                                        Toast.makeText(
                                                        getCurrentActivity(),
                                                        "Error : " + e.getLocalizedMessage(),
                                                        Toast.LENGTH_SHORT)
                                                .show();
                                        mSwipeRefreshLayout.setRefreshing(false);
                                    });
                        } finally {
                            mDBHelper.close();
                        }

                        GattingData = false;
                    }
                };

        mThread3 = new HandlerThread("name");
        mThread3.start();
        mThreadHandler3 = new Handler(mThread3.getLooper());
        mThreadHandler3.post(r3);
    }
*/
