package tw.y_studio.ptt.Fragment;

import static tw.y_studio.ptt.Utils.DebugUtils.useApi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import okhttp3.OkHttpClient;
import tw.y_studio.ptt.Adapter.SearchBoardsAdapter;
import tw.y_studio.ptt.DataBase.FavoriteDBHelper;
import tw.y_studio.ptt.R;
import tw.y_studio.ptt.UI.BaseFragment;
import tw.y_studio.ptt.UI.ClickFix;
import tw.y_studio.ptt.UI.CustomLinearLayoutManager;
import tw.y_studio.ptt.UI.UiFix;
import tw.y_studio.ptt.UI.article.list.ArticleListFragment;
import tw.y_studio.ptt.Utils.DebugUtils;
import tw.y_studio.ptt.Utils.StringUtils;
import tw.y_studio.ptt.api.SearchBoardAPI;
import tw.y_studio.ptt.di.Injection;
import tw.y_studio.ptt.source.remote.search.ISearchBoardRemoteDataSource;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SearchBoardsFragment extends BaseFragment {

    public static SearchBoardsFragment newInstance() {
        Bundle args = new Bundle();
        SearchBoardsFragment fragment = new SearchBoardsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static SearchBoardsFragment newInstance(Bundle args) {
        SearchBoardsFragment fragment = new SearchBoardsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private SearchBoardsAdapter mdapter;

    private List<Map<String, Object>> data = new ArrayList<>();

    private EditText searchBar;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;

    private ClickFix mClickFix = new ClickFix();

    private ImageButton clearButton;
    private int changeStep = 0;

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_boards_fragment_layout, container, false);

        setMainView(view);

        Bundle bundle = getArguments(); // 取得Bundle

        mRecyclerView = findViewById(R.id.search_boards_fragment_recyclerView);
        searchBar = findViewById(R.id.search_boards_fragment_editText_search);
        clearButton = findViewById(R.id.search_boards_item_imageView_like);
        mSwipeRefreshLayout = findViewById(R.id.search_boards_fragment_refresh_layout);
        // mSwipeRefreshLayout.setColorSchemeColors(Color.BLUE);

        clearButton.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (mClickFix.isFastDoubleClick(300)) return;
                        if (searchBar.getText().toString().length() == 0) {
                            try {
                                InputMethodManager inputMethodManager =
                                        (InputMethodManager)
                                                getCurrentActivity()
                                                        .getSystemService(
                                                                Context.INPUT_METHOD_SERVICE);
                                inputMethodManager.hideSoftInputFromWindow(
                                        searchBar.getWindowToken(), 0);
                            } catch (Exception e) {
                            }
                            getCurrentActivity().onBackPressed();
                        } else {
                            searchBar.getText().clear();
                        }
                    }
                });

        mdapter = new SearchBoardsAdapter(getCurrentActivity(), data);

        final CustomLinearLayoutManager layoutManager = new CustomLinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mdapter);

        UiFix.setSwipeRefreshLayoutBackground(mSwipeRefreshLayout, getCurrentActivity());

        mSwipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_red_light,
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light);

        mSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {

                    @Override
                    public void onRefresh() {
                        loadData();
                    }
                });

        searchBar.addTextChangedListener(
                new TextWatcher() {

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (haveApi && useApi) {
                            if (s.toString().length() == 0) {
                                data.clear();
                                mdapter.notifyDataSetChanged();
                                data.addAll(data_temp);
                                mdapter.notifyDataSetChanged();
                            } else {
                                if (!GattingData) {
                                    getDataFromApi(s.toString());
                                } else {
                                    waitSearchText = s.toString();
                                }
                            }
                            // getDataFromApi();
                        } else {
                            if (!GattingData) {
                                if (s.toString().length() == 0) {
                                    data.clear();
                                    mdapter.notifyDataSetChanged();
                                    data.addAll(data_temp);
                                    mdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }

                    @Override
                    public void beforeTextChanged(
                            CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}
                });

        mdapter.setOnItemClickListener(
                new SearchBoardsAdapter.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {
                        if (mClickFix.isFastDoubleClick()) return;

                        try {
                            InputMethodManager inputMethodManager =
                                    (InputMethodManager)
                                            getCurrentActivity()
                                                    .getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(
                                    searchBar.getWindowToken(), 0);
                        } catch (Exception e) {
                        }
                        Bundle bundle = new Bundle();
                        bundle.putString(
                                "title",
                                StringUtils.notNullString(data.get(position).get("title")));
                        bundle.putString(
                                "subtitle",
                                StringUtils.notNullString(data.get(position).get("subtitle")));

                        loadFragment(ArticleListFragment.newInstance(bundle), getCurrentFragment());
                    }
                });

        mdapter.setLikeOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (GattingData || mClickFix.isFastDoubleClick(300)) return;

                        int position = (int) v.getTag();
                        if ((Boolean) data.get(position).get("like")) {
                            deleteBoard(
                                    StringUtils.notNullString(data.get(position).get("title")),
                                    position);
                        } else {
                            insertBoard(
                                    StringUtils.notNullString(data.get(position).get("title")),
                                    StringUtils.notNullString(data.get(position).get("subtitle")),
                                    StringUtils.notNullString(data.get(position).get("class")),
                                    myBoardIndex,
                                    position);
                        }
                        changeStep++;
                        // Toast.makeText(getThisActivity(),StringUtils.notNullString(data.get(position).get("title")),Toast.LENGTH_SHORT).show();
                    }
                });

        return view;
    }

    private void insertBoard(
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

    private boolean restroy = false;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            restroy = true;
        }
    }

    private void sendChangeMessage() {
        Log.d("sender", "Broadcasting message");
        Intent intent = new Intent("ptt-favorite-change");
        // You can also include some extra data.
        intent.putExtra("message", "change");
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
    }

    protected void onAnimOver() {
        if (!restroy) {
            searchBar.requestFocus();
            InputMethodManager inputMethodManager =
                    (InputMethodManager)
                            getCurrentActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(searchBar, InputMethodManager.SHOW_FORCED);
            restroy = true;
        }

        loadData();
    }

    private Handler mThreadHandler;
    private HandlerThread mThread;
    private Runnable r1;

    private Handler mThreadHandler3;
    private HandlerThread mThread3;
    private Runnable r3;

    private List<Map<String, Object>> data_temp = new ArrayList<>();
    private OkHttpClient client;

    private Set<String> myBoard = new HashSet<>();
    private int myBoardIndex = 0;

    private SearchBoardAPI searchBoardAPI;
    private ISearchBoardRemoteDataSource searchBoardRemoteDataSource =
            Injection.RemoteDataSource.INSTANCE.getSearchBoardRemoteDataSource();

    private void getDataFromApi(String keyboard) {
        nowSearchText = keyboard;
        if (keyboard.isEmpty()) {
            mSwipeRefreshLayout.setRefreshing(false);
            return;
        }

        r1 =
                new Runnable() {

                    public void run() {
                        runOnUI(
                                () -> {
                                    mSwipeRefreshLayout.setRefreshing(true);
                                });

                        GattingData = true;
                        data_temp.clear();
                        myBoard.clear();

                        FavoriteDBHelper mDBHelper =
                                new FavoriteDBHelper(getCurrentActivity(), "Favorite.db", null, 1);
                        List<Map<String, Object>> data_temp2 = new ArrayList<>();
                        DebugUtils.Log("onAL", "get data from web start");
                        try {
                            myBoard.addAll(mDBHelper.getAllSet());
                            myBoardIndex = mDBHelper.getMaxIndex();

                            data_temp.addAll(
                                    searchBoardRemoteDataSource.searchBoardByKeyword(
                                            keyboard.replace(" ", "")));

                            for (Map<String, Object> item : data_temp) {
                                if (myBoard.contains(item.get("title").toString())) {
                                    item.put("like", true);
                                } else {
                                    item.put("like", false);
                                }
                            }

                            runOnUI(
                                    () -> {
                                        data.clear();
                                        data.addAll(data_temp);
                                        mdapter.notifyDataSetChanged();
                                        // data_temp.clear();
                                        mSwipeRefreshLayout.setRefreshing(false);
                                        if (!waitSearchText.isEmpty()) {
                                            getDataFromApi(waitSearchText);
                                        }
                                        waitSearchText = "";
                                    });

                            DebugUtils.Log("onAL", "get data from web over");
                        } catch (final Exception e) {
                            DebugUtils.Log("onAL", "Error : " + e.toString());
                            runOnUI(
                                    () -> {
                                        Toast.makeText(
                                                        getCurrentActivity(),
                                                        "Error : " + e.toString(),
                                                        Toast.LENGTH_SHORT)
                                                .show();
                                        mSwipeRefreshLayout.setRefreshing(false);
                                        if (!waitSearchText.isEmpty()) {
                                            getDataFromApi(waitSearchText);
                                        }
                                        waitSearchText = "";
                                    });
                        } finally {
                            mDBHelper.close();
                        }

                        GattingData = false;
                    }
                };

        mThread = new HandlerThread("name");
        mThread.start();
        mThreadHandler = new Handler(mThread.getLooper());
        mThreadHandler.post(r1);
    }

    private String nowSearchText = "";
    private String waitSearchText = "";
    private boolean haveApi = true;
    private boolean GattingData = false;

    private void loadData() {
        if (GattingData) return;
        data.clear();
        mdapter.notifyDataSetChanged();
        getDataFromApi(nowSearchText);
    }

    private void initView() throws Exception {}

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            InputMethodManager inputMethodManager =
                    (InputMethodManager)
                            getCurrentActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getMainView().getWindowToken(), 0);
        } catch (Exception e) {
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (changeStep > 0) {
            sendChangeMessage();
        }
        if (data != null) data.clear();

        if (data_temp != null) data_temp.clear();
        // 移除工作
        if (mThreadHandler != null) {
            mThreadHandler.removeCallbacks(r1);
        }
        // (關閉Thread)
        if (mThread != null) {
            mThread.quit();
        }

        if (mThreadHandler3 != null) {
            mThreadHandler3.removeCallbacks(r3);
        }
        // (關閉Thread)
        if (mThread3 != null) {
            mThread3.quit();
        }
    }
}
