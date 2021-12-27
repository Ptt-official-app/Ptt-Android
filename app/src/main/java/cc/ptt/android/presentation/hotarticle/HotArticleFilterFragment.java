package cc.ptt.android.presentation.hotarticle;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import cc.ptt.android.R;
import cc.ptt.android.data.common.StringUtils;
import cc.ptt.android.presentation.base.BaseFragment;
import cc.ptt.android.presentation.common.CustomLinearLayoutManager;
import cc.ptt.android.presentation.common.RecyclerItemClickListener;
import cc.ptt.android.presentation.common.stickyheader.StickyHeaderItemDecorator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HotArticleFilterFragment extends BaseFragment {

    public static HotArticleFilterFragment newInstance() {
        Bundle args = new Bundle();
        HotArticleFilterFragment fragment = new HotArticleFilterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static HotArticleFilterFragment newInstance(Bundle args) {
        HotArticleFilterFragment fragment = new HotArticleFilterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private HotArticleFilterAdapter mAdapter;

    private List<Map<String, Object>> data = new ArrayList<>();

    private String title = "";
    private List<String> board_list = new ArrayList<String>();

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hot_article_list_fragment_layout, container, false);

        setMainView(view);

        mRecyclerView = findViewById(R.id.article_list_fragment_recycler_view);
        mSwipeRefreshLayout = findViewById(R.id.article_list_fragment_refresh_layout);

        Bundle bundle = getArguments(); // 取得Bundle

        title = bundle.getString("title", "ALL");

        board_list.addAll(bundle.getStringArrayList("BoardList"));

        mAdapter = new HotArticleFilterAdapter(getCurrentActivity(), data);

        final CustomLinearLayoutManager layoutManager = new CustomLinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        StickyHeaderItemDecorator decorator = new StickyHeaderItemDecorator(mAdapter);
        decorator.attachToRecyclerView(mRecyclerView);

        mSwipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_red_light,
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light);
        mSwipeRefreshLayout.setEnabled(false);

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
                                // loadNextData();
                            }
                    }
                });

        RecyclerItemClickListener recyclerItemClickListener =
                new RecyclerItemClickListener(
                        mRecyclerView,
                        new RecyclerItemClickListener.OnItemClickListener() {

                            @Override
                            public void onItemClick(View view, int position) {
                                // System.out.println("onItemClick " + adapter.getItem(position));
                                if (position > 0) {
                                    if (!StringUtils.notNullString(data.get(position).get("title"))
                                            .equalsIgnoreCase(title)) {
                                        Intent intent = new Intent("puty-hot-HotArticle-change");
                                        // You can also include some extra data.
                                        String board =
                                                StringUtils.notNullString(
                                                        data.get(position).get("title"));
                                        if (board.equalsIgnoreCase("ALL")) {
                                            intent.putExtra("message", "");
                                        } else {
                                            intent.putExtra(
                                                    "message",
                                                    StringUtils.notNullString(
                                                            data.get(position).get("title")));
                                        }

                                        LocalBroadcastManager.getInstance(getContext())
                                                .sendBroadcast(intent);
                                    }

                                    getCurrentActivity().onBackPressed();
                                } else {
                                    getCurrentActivity().onBackPressed();
                                }
                            }

                            @Override
                            public void onItemLongClick(View view, int position) {
                                // System.out.println("onItemLongClick " + position);
                            }
                        });

        recyclerItemClickListener.setDecorator(decorator);
        mRecyclerView.addOnItemTouchListener(recyclerItemClickListener);

        mAdapter.setMoreClickListen(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        getCurrentActivity().onBackPressed();
                    }
                });

        return view;
    }

    protected void onAnimOver() {
        loadData();
    }

    private boolean GattingData = false;

    private void loadData() {
        if (GattingData) return;

        data.clear();

        for (int i = -1; i < board_list.size(); i++) {
            Map<String, Object> mm = new HashMap<>();
            if (i == -1) {
                mm.put("title", title);
                mm.put("type", "title");
            } else {
                mm.put("title", board_list.get(i));
                if (board_list.get(i).equalsIgnoreCase(title)) {
                    mm.put("select", true);
                } else {
                    mm.put("select", false);
                }
            }
            data.add(mm);
            if (i == -1) {
                mAdapter.notifyDataSetChanged();
            }
        }
        mAdapter.notifyItemRangeInserted(1, 1 + board_list.size());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (data != null) data.clear();
    }
}
