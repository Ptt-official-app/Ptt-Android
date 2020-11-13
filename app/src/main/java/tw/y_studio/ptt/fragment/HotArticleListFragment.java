package tw.y_studio.ptt.fragment;

import static tw.y_studio.ptt.utils.DebugUtils.useApi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Document;

import tw.y_studio.ptt.BuildConfig;
import tw.y_studio.ptt.R;
import tw.y_studio.ptt.adapter.HotArticleListAdapter;
import tw.y_studio.ptt.ptt.AidConverter;
import tw.y_studio.ptt.ui.BaseFragment;
import tw.y_studio.ptt.ui.ClickFix;
import tw.y_studio.ptt.ui.CustomLinearLayoutManager;
import tw.y_studio.ptt.ui.RecyclerItemClickListener;
import tw.y_studio.ptt.ui.StaticValue;
import tw.y_studio.ptt.ui.stickyheader.StickyHeaderItemDecorator;
import tw.y_studio.ptt.utils.DebugUtils;
import tw.y_studio.ptt.utils.OkHttpUtils;
import tw.y_studio.ptt.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HotArticleListFragment extends BaseFragment {

    public static HotArticleListFragment newInstance() {
        Bundle args = new Bundle();
        HotArticleListFragment fragment = new HotArticleListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static HotArticleListFragment newInstance(Bundle args) {
        HotArticleListFragment fragment = new HotArticleListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private HotArticleListAdapter mAdapter;

    private List<Map<String, Object>> data = new ArrayList<>();

    private ClickFix mClickFix = new ClickFix();

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

        mAdapter = new HotArticleListAdapter(getCurrentActivity(), data);

        final CustomLinearLayoutManager layoutManager = new CustomLinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        StickyHeaderItemDecorator decorator = new StickyHeaderItemDecorator(mAdapter);
        decorator.attachToRecyclerView(mRecyclerView);

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
                                if (mClickFix.isFastDoubleClick()) return;
                                if (position > 0) {
                                    data.get(position).put("readed", true);
                                    mAdapter.setHighLightUrl(
                                            StringUtils.notNullString(
                                                    data.get(position).get("url")));
                                    mAdapter.notifyDataSetChanged();
                                    // WebUtils.turnOnUrl(getContext(),StringUtils.notNullString(data.get(position).get("url")));

                                    Bundle bundle = new Bundle();
                                    bundle.putString(
                                            "title",
                                            StringUtils.notNullString(
                                                    data.get(position).get("title")));
                                    bundle.putString(
                                            "auth",
                                            StringUtils.notNullString(
                                                    data.get(position).get("auth")));
                                    bundle.putString(
                                            "date",
                                            StringUtils.notNullString(
                                                    data.get(position).get("date")));
                                    bundle.putString(
                                            "class",
                                            StringUtils.notNullString(
                                                    data.get(position).get("class")));
                                    bundle.putString(
                                            "board",
                                            StringUtils.notNullString(
                                                    data.get(position).get("board")));
                                    bundle.putString(
                                            "url",
                                            StringUtils.notNullString(
                                                    data.get(position).get("url")));

                                    loadFragment(
                                            ArticleReadFragment.newInstance(bundle),
                                            getCurrentFragment());
                                } else {
                                    Bundle bundle = new Bundle();
                                    bundle.putString(
                                            "title",
                                            StringUtils.notNullString(data.get(0).get("title")));
                                    bundle.putStringArrayList(
                                            "BoardList", (ArrayList<String>) board_list);
                                    // Log.d("onHot","size = "+board_list.size());

                                    loadFragmentNoAnim(
                                            HotArticleFilterFragment.newInstance(bundle),
                                            getCurrentFragment());
                                }
                            }

                            @Override
                            public void onItemLongClick(View view, int position) {
                                // System.out.println("onItemLongClick " + position);
                            }
                        });

        recyclerItemClickListener.setDecorator(decorator);
        mRecyclerView.addOnItemTouchListener(recyclerItemClickListener);

        LocalBroadcastManager.getInstance(getContext())
                .registerReceiver(mMessageReceiver, new IntentFilter("puty-hot-article-change"));

        mAdapter.setMoreClickListen(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (mClickFix.isFastDoubleClick()) return;
                        Bundle bundle = new Bundle();
                        bundle.putString(
                                "title", StringUtils.notNullString(data.get(0).get("title")));
                        bundle.putStringArrayList("BoardList", (ArrayList<String>) board_list);
                        DebugUtils.Log("onHot", "size = " + board_list.size());
                        loadFragmentNoAnim(
                                HotArticleFilterFragment.newInstance(bundle), getCurrentFragment());
                    }
                });

        return view;
    }

    protected void onAnimOver() {
        loadData();
    }

    public void scrollToTop() {
        try {
            if (mRecyclerView != null) {
                mRecyclerView.scrollToPosition(0);
            }
        } catch (Exception e) {
        }
    }

    private Handler mThreadHandler;
    private HandlerThread mThread;
    private Runnable r1;

    private String FieldName = "";

    private OkHttpClient client_puty;
    private final Pattern p2id = Pattern.compile("[-a-zA-Z0-9-_]{2,15}");
    private List<Map<String, Object>> data_temp = new ArrayList<>();
    private List<String> board_list = new ArrayList<String>();

    private void getDataFromPttWeb() {
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
                        String url = BuildConfig.demo_hot_article_url;
                        if (FieldName != null && FieldName.length() > 0) {
                            Map<String, Object> datt = new HashMap<>();
                            datt.put("title", FieldName);
                            datt.put("type", "title");
                            data_temp.add(datt);
                        } else {
                            Map<String, Object> datt = new HashMap<>();
                            datt.put("title", "ALL");
                            datt.put("type", "title");
                            data_temp.add(datt);
                        }

                        Document doc;
                        try {
                            if (StaticValue.userDebugMode) {
                                if (url == null) throw new Exception("url is null");

                                if (client_puty == null)
                                    client_puty = new OkHttpUtils().getTrustAllClient();

                                Request request = new Request.Builder().url(url).build();
                                Call mcall = client_puty.newCall(request);

                                Response response = mcall.execute();
                                final int code = response.code(); // can be any value
                                if (!response.isSuccessful() && code != 200) {
                                    // error
                                } else {
                                    ResponseBody mRb = response.body();
                                    String cont = mRb.string();

                                    JSONObject mm = new JSONObject(cont);

                                    JSONArray m2 = mm.getJSONArray("r");
                                    int i = 0;
                                    while (true) {
                                        try {
                                            if (m2.isNull(i)) break;
                                            JSONObject m3 = m2.getJSONObject(i);
                                            if (!board_list.contains(m3.getString("brd"))) {
                                                board_list.add(m3.getString("brd"));
                                            }
                                            if (FieldName != null && FieldName.length() > 0) {
                                                if (!m3.getString("brd")
                                                        .equalsIgnoreCase(FieldName)) {
                                                    i++;
                                                    continue;
                                                }
                                            }
                                            // if(m3==null) break;
                                            Map<String, Object> datt = new HashMap<>();

                                            datt.put("like", "" + m3.getInt("pushcnt"));
                                            datt.put("commit", "" + m3.getInt("pushcnt"));

                                            datt.put("type", "article");
                                            datt.put("readed", false);

                                            datt.put(
                                                    "url",
                                                    AidConverter.aidToUrl(
                                                            m3.getString("brd"),
                                                            m3.getString("pid")));
                                            String title = m3.getString("t");
                                            String classs = "";
                                            final Pattern p23 =
                                                    Pattern.compile("\\[([\\s\\S]{1,4})\\]");
                                            Matcher m23 = p23.matcher(title);

                                            if (m23.find()) {
                                                classs = m23.group(1);
                                                if (classs.length() <= 6) {
                                                    int start = title.indexOf("[" + classs + "]");
                                                    int end = start + classs.length() + 2;
                                                    if (start == 0) {
                                                        title =
                                                                StringUtils.clearStart(
                                                                        title.substring(end));
                                                    } else {
                                                        try {
                                                            title =
                                                                    title.substring(0, start)
                                                                            + StringUtils
                                                                                    .clearStart(
                                                                                            title
                                                                                                    .substring(
                                                                                                            end));
                                                        } catch (Exception E) {
                                                        }
                                                    }
                                                } else {
                                                    classs = "無分類";
                                                }
                                            }
                                            datt.put("title", title);
                                            datt.put("class", classs);
                                            datt.put("board", "" + m3.getString("brd"));
                                            // datt.put("auth",""+m3.getString("pid"));
                                            String auth = m3.getString("a1");

                                            Matcher m26 = p2id.matcher(auth);
                                            if (m26.find()) {
                                                auth = m26.group();
                                            }

                                            datt.put("auth", "" + auth);
                                            datt.put("image", "" + m3.getString("i"));
                                            String time = m3.getString("ts");

                                            SimpleDateFormat sdf =
                                                    new SimpleDateFormat(
                                                            "EEE MMM d HH:mm:ss yyyy",
                                                            Locale.ENGLISH);
                                            Date date = sdf.parse(time);
                                            // Log.d("onPuty","i = "+i+" / time ="+date.getTime());
                                            datt.put("date", "" + date.getTime());
                                            data_temp.add(datt);
                                        } catch (Exception e) {
                                            // Log.d("onPuty","error2 = "+e.toString());
                                            DebugUtils.Log("onAL", "inner = " + e.toString());
                                            break;
                                        }

                                        i++;
                                        // DebugUtils.Log("onAL","i = "+i);
                                    }
                                }
                            }

                            runOnUI(
                                    () -> {
                                        data.addAll(data_temp);
                                        mAdapter.notifyDataSetChanged();
                                        data_temp.clear();
                                        mSwipeRefreshLayout.setRefreshing(false);
                                    });

                            DebugUtils.Log("onAL", "get data from web over");
                        } catch (final Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            DebugUtils.Log("onHALF", "Error : " + e.toString());
                            runOnUI(
                                    () -> {
                                        Toast.makeText(
                                                        getCurrentActivity(),
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

    private BroadcastReceiver mMessageReceiver =
            new BroadcastReceiver() {

                @Override
                public void onReceive(Context context, Intent intent) {
                    // Get extra data included in the Intent
                    String message = intent.getStringExtra("message");
                    dealDataChange(message);
                    DebugUtils.Log("receiver", "Got message: " + message);
                }
            };

    private void dealDataChange(String board) {
        FieldName = board;
        loadData();
    }

    private void getDataFromApi() {}

    private boolean haveApi = false;
    private boolean GattingData = false;

    private void loadData() {
        if (GattingData) return;

        data.clear();

        board_list.clear();
        board_list.add("ALL");

        mAdapter.notifyDataSetChanged();
        if (haveApi && useApi) {
            getDataFromApi();
        } else {
            getDataFromPttWeb();
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
    }
}
