package tw.y_studio.ptt.Fragment;

import static android.content.Context.MODE_PRIVATE;
import static tw.y_studio.ptt.Utils.DebugUtils.useApi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import tw.y_studio.ptt.API.GetPostRankAPIHelper;
import tw.y_studio.ptt.API.PostAPIHelper;
import tw.y_studio.ptt.API.SetPostRankAPIHelper;
import tw.y_studio.ptt.Adapter.ArticleReadAdapter;
import tw.y_studio.ptt.Ptt.AidBean;
import tw.y_studio.ptt.Ptt.AidConverter;
import tw.y_studio.ptt.R;
import tw.y_studio.ptt.UI.BaseFragment;
import tw.y_studio.ptt.UI.CustomLinearLayoutManager;
import tw.y_studio.ptt.Utils.DebugUtils;
import tw.y_studio.ptt.Utils.StringUtils;
import tw.y_studio.ptt.Utils.UIUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArticleReadFragment extends BaseFragment {

    public static ArticleReadFragment newInstance() {
        Bundle args = new Bundle();
        ArticleReadFragment fragment = new ArticleReadFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ArticleReadFragment newInstance(Bundle args) {
        ArticleReadFragment fragment = new ArticleReadFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ArticleReadAdapter mAdapter;

    private List<Map<String, Object>> data = new ArrayList<>();

    private String articleTitle = "";
    private String articleBoard = "";
    private String articleAuth = " ";
    private String articleTime = "";
    private String articleClass = "";
    private String originalArticleTitle = "";
    private TextView article_read_item_textView_like;
    private EditText reply_edittext;
    private LinearLayout org_left;
    private LinearLayout org_right;
    private LinearLayout reply_left;
    private LinearLayout reply_right;

    private ImageButton reply_hide;
    private ImageButton likeBT;
    private ImageButton shareArticleBT;

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.article_read_fragment_layout, container, false);

        setMainView(view);

        reply_edittext = findViewById(R.id.article_read_item_editText_reply);
        org_left = findViewById(R.id.article_read_item_linearlayout_org_left);
        org_right = findViewById(R.id.article_read_item_linearlayout_org_right);
        reply_left = findViewById(R.id.article_read_item_linearlayout_reply_left);
        reply_right = findViewById(R.id.article_read_item_linearlayout_reply_right);
        reply_hide = findViewById(R.id.article_read_item_imageButton_hide_reply);
        article_read_item_textView_like = findViewById(R.id.article_read_item_textView_like);
        mRecyclerView = findViewById(R.id.article_read_fragment_recyclerView);
        likeBT = findViewById(R.id.article_read_item_imageButton_like);
        shareArticleBT = findViewById(R.id.article_read_item_imageButton_share);
        mSwipeRefreshLayout = findViewById(R.id.article_read_fragment_refresh_layout);

        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getContext().getTheme();
        theme.resolveAttribute(R.attr.article_header, typedValue, true);
        @ColorInt int color = typedValue.data;
        Window window = getCurrentActivity().getWindow();
        window.setStatusBarColor(color);

        likeBT.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        setRankMenu(v);
                    }
                });

        shareArticleBT.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        UIUtils.shareTo(
                                getContext(),
                                originalArticleTitle,
                                originalArticleTitle + "\n" + orgUrl,
                                "分享文章");
                    }
                });

        mAdapter = new ArticleReadAdapter(getCurrentActivity(), data);

        final CustomLinearLayoutManager layoutManager = new CustomLinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

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

        mAdapter.setOnItemClickListener(
                new ArticleReadAdapter.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {}
                });

        reply_hide.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        reply_edittext.clearFocus();
                        org_left.setVisibility(View.VISIBLE);
                        org_right.setVisibility(View.VISIBLE);
                        reply_left.setVisibility(View.GONE);
                        reply_right.setVisibility(View.GONE);
                        reply_edittext.setSingleLine(true);
                    }
                });

        reply_edittext.setOnFocusChangeListener(
                new View.OnFocusChangeListener() {

                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            org_left.setVisibility(View.GONE);
                            org_right.setVisibility(View.GONE);
                            reply_left.setVisibility(View.VISIBLE);
                            reply_right.setVisibility(View.VISIBLE);
                            reply_edittext.setSingleLine(false);
                            reply_edittext.setMaxLines(5);
                        } else {
                        }
                    }
                });

        Bundle bundle = getArguments(); // 取得Bundle
        orgUrl = bundle.getString("url", "");
        articleBoard = bundle.getString("board", "");
        articleTitle = bundle.getString("title", "");
        articleAuth = bundle.getString("auth", "");
        articleClass = bundle.getString("class", "");
        articleTime = bundle.getString("date", "");
        putDefaultHeader();
        article_read_item_textView_like.setText("0");

        return view;
    }

    protected void onAnimOver() {
        loadData();
    }

    private Handler mThreadHandler;
    private HandlerThread mThread;
    private Runnable r1;

    private String orgUrl = "";

    private int floorNum = 0;
    private List<Map<String, Object>> data_temp = new ArrayList<>();

    private int pushCount = 0;

    private boolean gettedUrl = false;

    private PostAPIHelper postAPI;
    private GetPostRankAPIHelper getPostRankAPI;

    private void getDataFromApi() {
        if (postAPI == null) {
            final Pattern p =
                    Pattern.compile(
                            "www.ptt.cc/bbs/([-a-zA-Z0-9_]{2,})/([M|G].[-a-zA-Z0-9._]{1,30}).htm");
            Matcher m = p.matcher(orgUrl);
            if (m.find()) {
                postAPI = new PostAPIHelper(getContext(), m.group(1), m.group(2));
            } else {
                DebugUtils.Log("onAR", "not match");
            }
        }

        if (getPostRankAPI == null) {
            final Pattern p =
                    Pattern.compile(
                            "www.ptt.cc/bbs/([-a-zA-Z0-9_]{2,})/([M|G].[-a-zA-Z0-9._]{1,30}).htm");
            Matcher m = p.matcher(orgUrl);
            if (m.find()) {
                AidBean aid = AidConverter.urlToAid(orgUrl);
                getPostRankAPI =
                        new GetPostRankAPIHelper(getContext(), aid.getBoardTitle(), aid.getAid());
            } else {
                DebugUtils.Log("onAR", "not match");
            }
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
                        DebugUtils.Log("onAR", "get data from web start");

                        try {
                            postAPI.get();
                            getPostRankAPI.get();
                            pushCount = getPostRankAPI.getLike();
                            floorNum = postAPI.getFloorNum();

                            originalArticleTitle = postAPI.getTitle();
                            if (true) {
                                Map<String, Object> item = new HashMap<>();
                                item.put("type", "header");
                                item.put("title", postAPI.getTitle());
                                item.put(
                                        "auth",
                                        postAPI.getAuth()
                                                + " ("
                                                + postAPI.getAuth_nickName()
                                                + ")");
                                item.put("date", postAPI.getDate());
                                item.put("class", postAPI.getClassString());
                                item.put("board", postAPI.getBoard());
                                data_temp.add(0, item);
                            }

                            String contents[] = postAPI.getContent().split("\r\n");
                            StringBuilder contentTemp = new StringBuilder();
                            for (int i = 0; i < contents.length; i++) {
                                String cmd = contents[i];
                                Matcher urlM = StringUtils.UrlPattern.matcher(cmd);
                                if (urlM.find()) {
                                    if (true) {
                                        Map<String, Object> item = new HashMap<>();
                                        item.put("type", "content");
                                        item.put("text", contentTemp.toString());
                                        data_temp.add(item);
                                    }
                                    contentTemp = new StringBuilder();
                                    if (true) {
                                        Map<String, Object> item = new HashMap<>();
                                        item.put("type", "content");
                                        item.put("text", cmd);
                                        data_temp.add(item);
                                    }
                                    List<String> imageUrl = StringUtils.getImgUrl(cmd);
                                    for (String urlString : imageUrl) {
                                        if (true) {
                                            Map<String, Object> item = new HashMap<>();
                                            item.put("type", "content_image");
                                            item.put("url", urlString);
                                            item.put("index", -1);
                                            data_temp.add(item);
                                        }
                                    }
                                } else {
                                    contentTemp.append(cmd);
                                    if (i < contents.length - 1) {
                                        contentTemp.append("\n");
                                    }
                                }
                            }
                            if (contentTemp.toString().length() > 0) {
                                Map<String, Object> item = new HashMap<>();
                                item.put("type", "content");
                                item.put("text", contentTemp.toString());
                                data_temp.add(item);
                                contentTemp = new StringBuilder();
                            }

                            if (true) {
                                Map<String, Object> item = new HashMap<>();
                                item.put("type", "center_bar");
                                item.put("like", pushCount + "");
                                item.put("floor", floorNum + "");
                                data_temp.add(item);
                            }

                            data_temp.addAll(postAPI.getPushData());

                            runOnUI(
                                    () -> {
                                        data.clear();
                                        // mAdapter.notifyDataSetChanged();
                                        data.addAll(data_temp);
                                        mAdapter.notifyDataSetChanged();
                                        data_temp.clear();
                                        mSwipeRefreshLayout.setRefreshing(false);
                                        article_read_item_textView_like.setText(pushCount + "");
                                    });
                            DebugUtils.Log("onAL", "get data from web over");
                        } catch (final Exception e) {
                            DebugUtils.Log("onAL", "Error : " + e.toString());
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

    private boolean haveApi = true;

    private void putDefaultHeader() {
        Map<String, Object> item = new HashMap<>();
        item.put("type", "header");
        item.put("title", articleTitle);
        item.put("auth", articleAuth);
        item.put("date", articleTime);
        item.put("class", articleClass);
        item.put("board", articleBoard);
        data.add(item);
    }

    private boolean GattingData = false;

    private void loadData() {
        if (GattingData) return;

        data.clear();
        putDefaultHeader();
        mAdapter.notifyDataSetChanged();
        getDataFromApi();
    }

    private void setRankMenu(View view) {
        if (!(haveApi && useApi)) {
            return;
        }
        String id =
                getCurrentActivity()
                        .getSharedPreferences("MainSetting", MODE_PRIVATE)
                        .getString("APIPTTID", "");
        if (id.isEmpty()) {
            loadFragment(LoginPageFragment.newInstance(), getCurrentFragment());
            return;
        }

        PopupMenu popupMenu = new PopupMenu(getCurrentActivity(), view);
        popupMenu.getMenuInflater().inflate(R.menu.post_article_rank_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(
                new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        SetPostRankAPIHelper.iRank rank = SetPostRankAPIHelper.iRank.non;
                        switch (item.getItemId()) {
                            case R.id.post_article_rank_like:
                                rank = SetPostRankAPIHelper.iRank.like;
                                break;
                            case R.id.post_article_rank_dislike:
                                rank = SetPostRankAPIHelper.iRank.dislike;
                                break;
                            case R.id.post_article_rank_non:
                                rank = SetPostRankAPIHelper.iRank.non;
                                break;
                        }
                        setRank(rank);
                        return true;
                    }
                });

        popupMenu.show();
    }

    private void rehreshRank() {
        if (getPostRankAPI == null) {
            final Pattern p =
                    Pattern.compile(
                            "www.ptt.cc/bbs/([-a-zA-Z0-9_]{2,})/([M|G].[-a-zA-Z0-9._]{1,30}).htm");
            Matcher m = p.matcher(orgUrl);
            if (m.find()) {
                AidBean aid = AidConverter.urlToAid(orgUrl);
                getPostRankAPI =
                        new GetPostRankAPIHelper(getContext(), aid.getBoardTitle(), aid.getAid());
            } else {
                DebugUtils.Log("onAR", "not match");
            }
        }

        r1 =
                new Runnable() {

                    public void run() {
                        runOnUI(
                                () -> {
                                    mSwipeRefreshLayout.setRefreshing(true);
                                });

                        GattingData = true;

                        try {
                            getPostRankAPI.get();
                            pushCount = getPostRankAPI.getLike();
                            int center_bar_index = -1;

                            for (int i = 0; i < data.size(); i++) {
                                if (StringUtils.notNullString(data.get(i).get("type"))
                                        .equalsIgnoreCase("center_bar")) {
                                    center_bar_index = i;
                                    break;
                                }
                            }

                            if (center_bar_index != -1) {
                                Map<String, Object> item = data.get(center_bar_index);

                                item.put("like", pushCount + "");
                            }

                            runOnUI(
                                    () -> {
                                        mSwipeRefreshLayout.setRefreshing(false);
                                        mAdapter.notifyDataSetChanged();
                                        article_read_item_textView_like.setText(pushCount + "");
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

    private ProgressDialog mDialog = null;

    private void setRank(final SetPostRankAPIHelper.iRank rank_) {
        mDialog = ProgressDialog.show(getCurrentActivity(), "", "Please wait.");
        mDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);

        new Thread() {

            @Override
            public void run() {
                try {
                    SetPostRankAPIHelper setPostRankAPI;
                    final Pattern p =
                            Pattern.compile(
                                    "www.ptt.cc/bbs/([-a-zA-Z0-9_]{2,})/([M|G].[-a-zA-Z0-9._]{1,30}).htm");
                    Matcher m = p.matcher(orgUrl);
                    if (m.find()) {
                        AidBean aid = AidConverter.urlToAid(orgUrl);
                        setPostRankAPI =
                                new SetPostRankAPIHelper(
                                        getContext(), aid.getBoardTitle(), aid.getAid());
                    } else {
                        throw new Exception("error");
                        // DebugUtils.Log("onAR", "not match");
                    }
                    String id =
                            getCurrentActivity()
                                    .getSharedPreferences("MainSetting", MODE_PRIVATE)
                                    .getString("APIPTTID", "");
                    if (id.length() == 0) {
                        throw new Exception("No Ptt id");
                    }
                    setPostRankAPI.get(id, rank_);
                    runOnUI(
                            () -> {
                                mDialog.dismiss();
                                rehreshRank();
                            });
                } catch (Exception e) {
                    runOnUI(
                            () -> {
                                mDialog.dismiss();
                                Toast.makeText(
                                                getCurrentActivity(),
                                                "Error : " + e.toString(),
                                                Toast.LENGTH_SHORT)
                                        .show();
                            });
                }
            }
        }.start();
    }

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

        if (postAPI != null) {
            postAPI.close();
        }

        if (data != null) data.clear();
        // 移除工作
        if (mThreadHandler != null) {
            mThreadHandler.removeCallbacks(r1);
        }
        // (關閉Thread)
        if (mThread != null) {
            mThread.quit();
        }

        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getCurrentActivity().getTheme();
        theme.resolveAttribute(R.attr.black, typedValue, true);
        @ColorInt int color = typedValue.data;
        Window window = getCurrentActivity().getWindow();
        window.setStatusBarColor(color);
    }
}
