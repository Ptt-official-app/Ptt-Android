package tw.y_studio.ptt.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import tw.y_studio.ptt.Adapter.ArticleListAdapter;
import tw.y_studio.ptt.R;
import tw.y_studio.ptt.UI.BaseFragment;
import tw.y_studio.ptt.UI.ClickFix;
import tw.y_studio.ptt.UI.CustomLinearLayoutManager;
import tw.y_studio.ptt.Utils.DebugUtils;
import tw.y_studio.ptt.Utils.StringUtils;
import tw.y_studio.ptt.api.PostListAPIHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ArticleListFragment extends BaseFragment {

    public enum Type {
        Normal,
        Search,
    }

    public static ArticleListFragment newInstance() {
        Bundle args = new Bundle();
        ArticleListFragment fragment = new ArticleListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ArticleListFragment newInstance(Bundle args) {
        ArticleListFragment fragment = new ArticleListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ArticleListAdapter mArticleListAdapter;

    private List<Map<String, Object>> data = new ArrayList<>();

    private Type PageType = Type.Normal;

    private String BoardName = "";
    private String BoardSubName = "";
    private TextView mTextView_BoardName;
    private TextView mTextView_BoardSubName;
    private ImageButton Go2Back;

    private BottomNavigationView navigation;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;

    private ClickFix mClickFix = new ClickFix();

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.article_list_fragment_layout, container, false);

        setMainView(view);

        mTextView_BoardName = findViewById(R.id.article_list_fragment_textView_title);
        mTextView_BoardSubName = findViewById(R.id.article_list_fragment_textView_subtitle);
        Go2Back = findViewById(R.id.article_read_item_header_imageView_back);
        mRecyclerView = findViewById(R.id.article_list_fragment_recyclerView);
        navigation = findViewById(R.id.article_list_fragment_bottom_navigation);
        mSwipeRefreshLayout = findViewById(R.id.article_list_fragment_refresh_layout);

        Go2Back.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        getCurrentActivity().onBackPressed();
                    }
                });

        mOnNavigationItemSelectedListener =
                new BottomNavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.article_list_navigation_item_refresh:
                                loadData();
                                return false;
                            case R.id.article_list_navigation_item_post:
                                loadFragment(
                                        PostArticleFragment.newInstance(), getCurrentFragment());
                                return false;
                            case R.id.article_list_navigation_item_search:
                                loadFragment(
                                        ArticleListSearchFragment.newInstance(),
                                        getCurrentFragment());
                            case R.id.article_list_navigation_item_info:
                            default:
                        }
                        return false;
                    }
                };

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mArticleListAdapter = new ArticleListAdapter(getCurrentActivity(), data);

        final CustomLinearLayoutManager layoutManager = new CustomLinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mArticleListAdapter);

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

        mRecyclerView.addOnScrollListener(
                new RecyclerView.OnScrollListener() {

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                        int totalItemCount = layoutManager.getItemCount();
                        if (!GattingData)
                            if (lastVisibleItem >= totalItemCount - 30) {
                                loadNextData();
                            }
                    }
                });

        mArticleListAdapter.setOnItemClickListener(
                new ArticleListAdapter.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {
                        if (mClickFix.isFastDoubleClick()) return;
                        data.get(position).put("readed", true);
                        mArticleListAdapter.setHighLightUrl(
                                StringUtils.notNullString(data.get(position).get("url")));
                        mArticleListAdapter.notifyDataSetChanged();
                        // WebUtils.turnOnUrl(_mActivity,StringUtils.notNullString(data.get(position).get("url")));

                        Bundle bundle = new Bundle();
                        bundle.putString(
                                "title",
                                StringUtils.notNullString(data.get(position).get("title")));
                        bundle.putString(
                                "auth", StringUtils.notNullString(data.get(position).get("auth")));
                        bundle.putString(
                                "date", StringUtils.notNullString(data.get(position).get("date")));
                        bundle.putString(
                                "class",
                                StringUtils.notNullString(data.get(position).get("class")));
                        bundle.putString("board", BoardName);
                        bundle.putString(
                                "url", StringUtils.notNullString(data.get(position).get("url")));

                        loadFragment(ArticleReadFragment.newInstance(bundle), getCurrentFragment());
                    }
                });

        Bundle bundle = getArguments(); // 取得Bundle
        BoardName = bundle.getString("title", getString(R.string.board_list_title_empty));
        BoardSubName = bundle.getString("subtitle", getString(R.string.board_list_subtitle_empty));
        PageType = (Type) bundle.get("PageType");

        if (PageType == null) {
            PageType = Type.Normal;
        }

        mTextView_BoardName.setText(BoardName);
        mTextView_BoardSubName.setText(BoardSubName);

        return view;
    }

    protected void onAnimOver() {
        loadData();
    }

    private Handler mThreadHandler;
    private HandlerThread mThread;
    private Runnable r1;

    private List<Map<String, Object>> data_temp = new ArrayList<>();
    private PostListAPIHelper postListAPI;
    private int NowApiNum = 1;

    private void getDataFromApi() {
        if (postListAPI == null) {
            postListAPI = new PostListAPIHelper(getContext(), BoardName);
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
                        DebugUtils.Log("onAL", "get data from web start");
                        try {
                            for (int i = 0; i < 3; i++) {
                                try {
                                    data_temp.addAll(postListAPI.get(NowApiNum).getData());
                                } catch (Exception e) {
                                    if (NowApiNum > 1) {
                                        throw e;
                                    }
                                }
                                NowApiNum++;
                            }

                            runOnUI(
                                    () -> {
                                        data.addAll(data_temp);
                                        mArticleListAdapter.notifyDataSetChanged();
                                        data_temp.clear();
                                        mSwipeRefreshLayout.setRefreshing(false);
                                    });
                            DebugUtils.Log("ArticleListFragment", "get data from web success");
                        } catch (final Exception e) {
                            e.printStackTrace();
                            DebugUtils.Log("ArticleListFragment", "Error : " + e.toString());
                            runOnUI(
                                    () -> {
                                        try {
                                            Toast.makeText(
                                                            getCurrentActivity(),
                                                            "Error : " + e.toString(),
                                                            Toast.LENGTH_SHORT)
                                                    .show();
                                            mSwipeRefreshLayout.setRefreshing(false);
                                        } catch (Exception e2) {
                                        }
                                    });
                        }
                        GattingData = false;
                    }
                };

        mThread = new HandlerThread("name");
        mThread.start();
        mThreadHandler = new Handler(mThread.getLooper());
        mThreadHandler.post(r1);
    }

    private void loadNextData() {
        if (GattingData) return;
        getDataFromApi();
    }

    private boolean GattingData = false;

    private void loadData() {
        if (GattingData) return;

        data.clear();
        mArticleListAdapter.notifyDataSetChanged();

        NowApiNum = 1;
        getDataFromApi();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (data != null) data.clear();
        if (postListAPI != null) {
            postListAPI.close();
        }
        // 移除工作
        if (mThreadHandler != null) {
            mThreadHandler.removeCallbacks(r1);
        }
        // (關閉Thread)
        if (mThread != null) {
            mThread.quit();
        }
    }
}
