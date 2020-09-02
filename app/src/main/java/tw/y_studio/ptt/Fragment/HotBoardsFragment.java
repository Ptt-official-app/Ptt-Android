package tw.y_studio.ptt.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tw.y_studio.ptt.API.PopularBoardListAPIHelper;
import tw.y_studio.ptt.Adapter.HotBoardsListAdapter;
import tw.y_studio.ptt.HomeActivity;
import tw.y_studio.ptt.Ptt.WebUtils;
import tw.y_studio.ptt.R;
import tw.y_studio.ptt.UI.BaseFragment;
import tw.y_studio.ptt.UI.ClickFix;
import tw.y_studio.ptt.UI.CustomLinearLayoutManager;
import tw.y_studio.ptt.UI.StaticValue;
import tw.y_studio.ptt.Utils.DebugUtils;
import tw.y_studio.ptt.Utils.StringUtils;

import static tw.y_studio.ptt.Utils.DebugUtils.useApi;

public class HotBoardsFragment extends BaseFragment {
    private View Mainview=null;
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

    private List<Map<String, Object>> data;
    private ClickFix mClickFix = new ClickFix();

    private LinearLayout search_bar;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hot_boards_fragment_layout, container, false);

        Mainview=view;

        data = new ArrayList<>();
        Bundle bundle = getArguments();//取得Bundle

        search_bar = Mainview.findViewById(R.id.hot_boards_fragment_search);
        search_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ((HomeActivity)getContext()).loadFragmentNoAnim(SearchBoardsFragment.newInstance(),getParentFragment());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        mRecyclerView = Mainview.findViewById(R.id.hot_boards_fragment_recyclerView);

        mHotBoardsListAdapter = new HotBoardsListAdapter(getThisActivity(),data);

        final CustomLinearLayoutManager layoutManager = new CustomLinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mHotBoardsListAdapter);


        mSwipeRefreshLayout= Mainview.findViewById(R.id.hot_boards_fragment_refresh_layout);
        //mSwipeRefreshLayout.setColorSchemeColors(Color.BLUE);
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

        mHotBoardsListAdapter.setOnItemClickListener(new HotBoardsListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(mClickFix.isFastDoubleClick()) return;
                Bundle bundle = new Bundle();
                bundle.putString("title", StringUtils.notNullString(data.get(position).get("title")));
                bundle.putString("subtitle", StringUtils.notNullString(data.get(position).get("subtitle")));
                try {
                    ((HomeActivity)getContext()).loadFragment(ArticleListFragment.newInstance(bundle),getParentFragment());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    protected void onAnimOver() {
        loadData();
    }

    private Handler mUI_Handler = new Handler();
    private Handler mThreadHandler;
    private HandlerThread mThread;
    private Runnable r1;

    public void scrollToTop(){
        try {
            if(mRecyclerView!=null){
                mRecyclerView.scrollToPosition(0);
            }
        }catch (Exception e){

        }
    }

    private List<Map<String, Object>> data_temp = new ArrayList<>();
    private PopularBoardListAPIHelper popularBoardListAPI;
    private void getDataFromApi(){
        if (popularBoardListAPI == null) {
            popularBoardListAPI =  new PopularBoardListAPIHelper(getContext());
        }

        r1 = new Runnable() {
            public void run() {
                getThisActivity().runOnUiThread (new Thread(new Runnable() {
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(true);

                    }
                }));
                GattingData=true;
                data_temp.clear();

                try {

                    //PopularBoardListAPIHelper api = new PopularBoardListAPIHelper(this)
                    data_temp.addAll(popularBoardListAPI.get(1,128).getData());

                    getThisActivity().runOnUiThread (new Thread(new Runnable() {
                        public void run() {
                            data.addAll(data_temp);
                            mHotBoardsListAdapter.notifyDataSetChanged();
                            data_temp.clear();
                            mSwipeRefreshLayout.setRefreshing(false);

                        }
                    }));
                    DebugUtils.Log("onHotBoards","get data from web over");

                }catch (final Exception e){
                    DebugUtils.Log("onHotBoards","Error : "+e.toString());
                    final Activity thisActivity = getThisActivity();
                    if(thisActivity != null){
                        getThisActivity().runOnUiThread (new Thread(new Runnable() {
                            public void run() {
                                Toast.makeText(thisActivity,"Error : "+e.toString(),Toast.LENGTH_SHORT).show();

                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                        }));
                    }

                }

                GattingData=false;
            }

        };

        mThread = new HandlerThread("name");
        mThread.start();
        mThreadHandler = new Handler(mThread.getLooper());
        mThreadHandler.post(r1);
    }
    private boolean GattingData = false;
    private void loadData(){
        if(GattingData) return;
        GattingData = true;
        data.clear();
        mHotBoardsListAdapter.notifyDataSetChanged();
        getDataFromApi();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(data!=null)
        data.clear();
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
