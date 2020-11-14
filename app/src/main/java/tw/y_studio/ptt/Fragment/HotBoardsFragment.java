package tw.y_studio.ptt.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import tw.y_studio.ptt.Adapter.HotBoardsListAdapter;
import tw.y_studio.ptt.R;
import tw.y_studio.ptt.UI.BaseFragment;
import tw.y_studio.ptt.UI.ClickFix;
import tw.y_studio.ptt.UI.CustomLinearLayoutManager;
import tw.y_studio.ptt.UI.article.list.ArticleListFragment;
import tw.y_studio.ptt.Utils.DebugUtils;
import tw.y_studio.ptt.Utils.StringUtils;
import tw.y_studio.ptt.di.Injection;
import tw.y_studio.ptt.source.remote.popular.IPopularRemoteDataSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HotBoardsFragment extends BaseFragment {

    public static HotBoardsFragment newInstance() {
        Bundle args = new Bundle();
        HotBoardsFragment fragment = new HotBoardsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static HotBoardsFragment newInstance(Bundle args) {
        HotBoardsFragment fragment = new HotBoardsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private HotBoardsListAdapter mHotBoardsListAdapter;

    private List<Map<String, Object>> data = new ArrayList<>();
    private ClickFix mClickFix = new ClickFix();

    private LinearLayout search_bar;

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hot_boards_fragment_layout, container, false);

        setMainView(view);

        Bundle bundle = getArguments();

        search_bar = getMainView().findViewById(R.id.hot_boards_fragment_search);
        mRecyclerView = getMainView().findViewById(R.id.hot_boards_fragment_recyclerView);
        mSwipeRefreshLayout = getMainView().findViewById(R.id.hot_boards_fragment_refresh_layout);

        search_bar.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        loadFragmentNoAnim(
                                SearchBoardsFragment.newInstance(), getCurrentFragment());
                    }
                });

        mHotBoardsListAdapter = new HotBoardsListAdapter(getCurrentActivity(), data);

        final CustomLinearLayoutManager layoutManager = new CustomLinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mHotBoardsListAdapter);

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

        mHotBoardsListAdapter.setOnItemClickListener(
                new HotBoardsListAdapter.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {
                        if (mClickFix.isFastDoubleClick()) return;
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

        return view;
    }

    protected void onAnimOver() {
        loadData();
    }

    private Handler mThreadHandler;
    private HandlerThread mThread;
    private Runnable r1;

    private List<Map<String, Object>> data_temp = new ArrayList<>();

    private IPopularRemoteDataSource popularRemoteDataSource =
            Injection.RemoteDataSource.INSTANCE.getPopularRemoteDataSource();

    public void scrollToTop() {
        try {
            if (mRecyclerView != null) {
                mRecyclerView.scrollToPosition(0);
            }
        } catch (Exception e) {
        }
    }

    private void getDataFromApi() {
        r1 =
                new Runnable() {

                    public void run() {
                        runOnUI(
                                () -> {
                                    mSwipeRefreshLayout.setRefreshing(true);
                                });

                        GattingData = true;
                        data_temp.clear();

                        try {
                            data_temp.addAll(popularRemoteDataSource.getPopularBoardData(1, 128));
                            runOnUI(
                                    () -> {
                                        data.addAll(data_temp);
                                        mHotBoardsListAdapter.notifyDataSetChanged();
                                        data_temp.clear();
                                        mSwipeRefreshLayout.setRefreshing(false);
                                    });

                            DebugUtils.Log("HotBoardsFragment", "get data from web success");
                        } catch (final Exception e) {
                            DebugUtils.Log("HotBoardsFragment", "Error : " + e.toString());
                            runOnUI(
                                    () -> {
                                        Toast.makeText(
                                                        getActivity(),
                                                        "Error : " + e.toString(),
                                                        Toast.LENGTH_SHORT)
                                                .show();
                                        mSwipeRefreshLayout.setRefreshing(false);
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

    private boolean GattingData = false;

    private void loadData() {
        if (GattingData) return;
        GattingData = true;
        data.clear();
        mHotBoardsListAdapter.notifyDataSetChanged();
        getDataFromApi();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (data != null) {
            data.clear();
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
