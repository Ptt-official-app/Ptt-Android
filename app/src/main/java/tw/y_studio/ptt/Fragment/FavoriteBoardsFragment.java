package tw.y_studio.ptt.Fragment;

import static tw.y_studio.ptt.Utils.DebugUtils.useApi;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import tw.y_studio.ptt.Adapter.FavoriteBoardsListAdapter;
import tw.y_studio.ptt.DataBase.FavoriteDBHelper;
import tw.y_studio.ptt.R;
import tw.y_studio.ptt.UI.BaseFragment;
import tw.y_studio.ptt.UI.ClickFix;
import tw.y_studio.ptt.UI.CustomLinearLayoutManager;
import tw.y_studio.ptt.UI.DragItemMove.ItemMoveCallback;
import tw.y_studio.ptt.UI.DragItemMove.StartDragListener;
import tw.y_studio.ptt.UI.article.list.ArticleListFragment;
import tw.y_studio.ptt.Utils.DebugUtils;
import tw.y_studio.ptt.Utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FavoriteBoardsFragment extends BaseFragment {

    public static FavoriteBoardsFragment newInstance() {
        Bundle args = new Bundle();
        FavoriteBoardsFragment fragment = new FavoriteBoardsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static FavoriteBoardsFragment newInstance(Bundle args) {
        FavoriteBoardsFragment fragment = new FavoriteBoardsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FavoriteBoardsListAdapter mAdapter;

    private List<Map<String, Object>> data = new ArrayList<>();

    private StartDragListener mStartDragListener;
    private ItemTouchHelper touchHelper;
    private ImageButton edit;
    private boolean editMode = false;
    private ClickFix mClickFix = new ClickFix();

    private LinearLayout search_bar;

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favorite_boards_fragment_layout, container, false);

        setMainView(view);

        search_bar = findViewById(R.id.hot_boards_fragment_search);
        edit = findViewById(R.id.hot_boards_fragment_edit);
        mRecyclerView = findViewById(R.id.hot_boards_fragment_recyclerView);
        mSwipeRefreshLayout = findViewById(R.id.hot_boards_fragment_refresh_layout);

        Bundle bundle = getArguments(); // 取得Bundle

        search_bar.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (isEditMode()) {
                            Toast mm =
                                    Toast.makeText(
                                            getContext(),
                                            R.string.attion_close_edit_mode,
                                            Toast.LENGTH_SHORT);
                            mm.setGravity(Gravity.CENTER, 0, 0);
                            mm.show();
                            return;
                        }
                        try {
                            loadFragmentNoAnim(
                                    SearchBoardsFragment.newInstance(), getCurrentFragment());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

        edit.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (GattingData) return;

                        editMode = !editMode;
                        mAdapter.setEditMode(editMode);
                        mAdapter.notifyDataSetChanged();
                        if (editMode) {
                            edit.setColorFilter(
                                    getCurrentActivity()
                                            .getResources()
                                            .getColor(R.color.tangerine));
                        } else {
                            edit.setColorFilter(
                                    getCurrentActivity()
                                            .getResources()
                                            .getColor(R.color.slateGrey));
                        }
                        if (!editMode) {
                            UpdateBoardSort();
                        }
                    }
                });

        mStartDragListener =
                new StartDragListener() {

                    @Override
                    public void requestDrag(RecyclerView.ViewHolder viewHolder) {
                        touchHelper.startDrag(viewHolder);
                    }
                };

        mAdapter = new FavoriteBoardsListAdapter(getCurrentActivity(), data, mStartDragListener);
        // mAdapter.setEditMode(true);
        ItemTouchHelper.Callback callback = new ItemMoveCallback(mAdapter);
        touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mRecyclerView);

        final CustomLinearLayoutManager layoutManager = new CustomLinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        // mSwipeRefreshLayout.setColorSchemeColors(Color.BLUE);
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

        mAdapter.setOnItemClickListener(
                new FavoriteBoardsListAdapter.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {
                        if (mClickFix.isFastDoubleClick()) return;
                        if (!editMode) {
                            Bundle bundle = new Bundle();
                            bundle.putString(
                                    "title",
                                    StringUtils.notNullString(data.get(position).get("title")));
                            bundle.putString(
                                    "subtitle",
                                    StringUtils.notNullString(data.get(position).get("subtitle")));

                            loadFragment(
                                    ArticleListFragment.newInstance(bundle), getCurrentFragment());
                        }
                    }
                });
        mAdapter.setDislikeOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (mClickFix.isFastDoubleClick()) return;

                        String board = (String) v.getTag();

                        deleteBoard(board, getBoardIndex(board));
                    }
                });

        LocalBroadcastManager.getInstance(getContext())
                .registerReceiver(mMessageReceiver, new IntentFilter("ptt-favorite-change"));

        return view;
    }

    protected void onAnimOver() {
        loadData();
    }

    private void dealDataChange() {
        if (GattingData) return;
        if (haveApi && useApi) {
            dealDataChangeFromApi();
        } else {
            dealDataChangeFromDataBase();
        }
    }

    private void dealDataChangeFromApi() {}

    private int oreDFloor2 = 0, oreDFloorVector2 = 0;

    private void dealDataChangeFromDataBase() {
        r1 =
                new Runnable() {

                    public void run() {
                        runOnUI(
                                () -> {
                                    mSwipeRefreshLayout.setRefreshing(true);
                                    try {
                                        LinearLayoutManager layoutManager =
                                                (LinearLayoutManager)
                                                        mRecyclerView.getLayoutManager();
                                        View topView = layoutManager.getChildAt(0);
                                        if (topView != null) {
                                            oreDFloor2 = topView.getTop();
                                            oreDFloorVector2 = layoutManager.getPosition(topView);
                                        }
                                    } catch (Exception e) {
                                    }
                                    data.clear();
                                    mAdapter.notifyDataSetChanged();
                                });

                        GattingData = true;
                        data_temp.clear();

                        FavoriteDBHelper mDBHelper =
                                new FavoriteDBHelper(getCurrentActivity(), "Favorite.db", null, 1);

                        try {
                            data_temp.addAll(mDBHelper.getAll());
                            runOnUI(
                                    () -> {
                                        data.addAll(data_temp);
                                        mAdapter.notifyDataSetChanged();
                                        data_temp.clear();
                                        mSwipeRefreshLayout.setRefreshing(false);
                                        ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                                                .scrollToPositionWithOffset(
                                                        oreDFloorVector2, oreDFloor2);
                                    });

                            DebugUtils.Log("onHotBoards", "get data from web over");
                        } catch (final Exception e) {
                            DebugUtils.Log("onHotBoards", "Error " + e.toString());
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

        mThread = new HandlerThread("name");
        mThread.start();
        mThreadHandler = new Handler(mThread.getLooper());
        mThreadHandler.post(r1);
    }

    private BroadcastReceiver mMessageReceiver =
            new BroadcastReceiver() {

                @Override
                public void onReceive(Context context, Intent intent) {
                    // Get extra data included in the Intent
                    String message = intent.getStringExtra("message");
                    if (message.equals("change")) {
                        dealDataChange();
                    }
                    Log.d("receiver", "Got message: " + message);
                }
            };

    public int getBoardIndex(String board) {
        for (int i = 0; i < data.size(); i++) {
            Map<String, Object> mm = data.get(i);
            if (mm.get("title").toString().equals(board)) {
                return i;
            }
        }
        return -1;
    }

    public boolean isEditMode() {
        return editMode;
    }

    private Handler mThreadHandler;
    private HandlerThread mThread;
    private Runnable r1;

    public void scrollToTop() {
        try {
            if (mRecyclerView != null) {
                mRecyclerView.scrollToPosition(0);
            }
        } catch (Exception e) {
        }
    }

    private Handler mThreadHandler3;
    private HandlerThread mThread3;
    private Runnable r3;

    private void UpdateBoardSort() {
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
                            List<ContentValues> items = new ArrayList<>();

                            for (int i = 0; i < data.size(); i++) {
                                Map<String, Object> mm = data.get(i);

                                items.add(
                                        FavoriteDBHelper.newContentValues(
                                                StringUtils.notNullImageString(mm.get("title")),
                                                StringUtils.notNullImageString(mm.get("subtitle")),
                                                StringUtils.notNullImageString(mm.get("class")),
                                                i));
                            }

                            mDBHelper.deleAll();
                            mDBHelper.insertBoards(items);
                            runOnUI(
                                    () -> {
                                        mSwipeRefreshLayout.setRefreshing(false);
                                    });
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
                                        data.remove(position);
                                        mAdapter.notifyItemRemoved(position);
                                        // myBoardIndex--;
                                        mSwipeRefreshLayout.setRefreshing(false);
                                    });

                            DebugUtils.Log("onAL", board + " delete over");
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

    private List<Map<String, Object>> data_temp = new ArrayList<>();

    private void getDataFromDataBase() {
        r1 =
                new Runnable() {

                    public void run() {
                        runOnUI(
                                () -> {
                                    mSwipeRefreshLayout.setRefreshing(true);
                                });

                        GattingData = true;
                        data_temp.clear();

                        FavoriteDBHelper mDBHelper =
                                new FavoriteDBHelper(getCurrentActivity(), "Favorite.db", null, 1);

                        try {
                            data_temp.addAll(mDBHelper.getAll());
                            runOnUI(
                                    () -> {
                                        data.addAll(data_temp);
                                        mAdapter.notifyDataSetChanged();
                                        data_temp.clear();
                                        mSwipeRefreshLayout.setRefreshing(false);
                                    });

                            DebugUtils.Log("onHotBoards", "get data from web over");
                        } catch (final Exception e) {
                            DebugUtils.Log("onHotBoards", "Error " + e.toString());
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

        mThread = new HandlerThread("name");
        mThread.start();
        mThreadHandler = new Handler(mThread.getLooper());
        mThreadHandler.post(r1);
    }

    private void getDataFromApi() {}

    private boolean haveApi = false;
    private boolean GattingData = false;

    private void loadData() {
        if (GattingData) return;

        data.clear();
        mAdapter.notifyDataSetChanged();
        if (haveApi && useApi) {
            getDataFromApi();
        } else {
            getDataFromDataBase();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mMessageReceiver);

        if (data != null) data.clear();
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
