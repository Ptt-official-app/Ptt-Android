package tw.y_studio.ptt.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import tw.y_studio.ptt.API.PostListAPIHelper;
import tw.y_studio.ptt.R;
import tw.y_studio.ptt.UI.ClickFix;


public class ArticleListSearchFragment extends Fragment {

    private View Mainview=null;
    public static ArticleListSearchFragment newInstance() {
        Bundle args = new Bundle();
        ArticleListSearchFragment fragment = new ArticleListSearchFragment();
        fragment.setArguments(args);
        return fragment;
    }
    public static ArticleListSearchFragment newInstance(Bundle args) {
        ArticleListSearchFragment fragment = new ArticleListSearchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    //private RecyclerView mRecyclerView;
    //private SwipeRefreshLayout mSwipeRefreshLayout;
    //private ArticleListAdapter mArticleListAdapter;

    //private List<Map<String, Object>> data;



    private String BoardName = "搜尋文章";
    private String BoardSubName = "";
    private TextView mTextView_BoardName;
    private TextView mTextView_BoardSubName;
    private AppCompatImageButton Go2Back;

    private BottomNavigationView navigation;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;

    private ClickFix mClickFix = new ClickFix();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.article_list_search_fragment_layout, container, false);
        Mainview=view;

        Bundle bundle = getArguments();//取得Bundle

        mTextView_BoardName = Mainview.findViewById(R.id.article_list_fragment_textView_title);
        mTextView_BoardSubName = Mainview.findViewById(R.id.article_list_fragment_textView_subtitle);
        mTextView_BoardName.setText(BoardName);
        mTextView_BoardSubName.setText(BoardSubName);

        Go2Back = Mainview.findViewById(R.id.article_read_item_header_imageView_back);
        Go2Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.article_list_search__navigation_item_cross:
                        getActivity().onBackPressed();
                        return false;
                    case R.id.article_list_search__navigation_item_sure:
                    default:

                }
                return false;
            }

        };
        navigation = (BottomNavigationView) Mainview.findViewById(R.id.article_list_fragment_bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        loadData();
        return view;
    }

    private void getDataFromApi(){


    }

    private void loadNextData(){
        if(GattingData) return;
        getDataFromApi();
    }
    private boolean GattingData = false;
    private void loadData(){
        if(GattingData) return;

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(Mainview.getWindowToken(), 0);
        } catch (Exception e) {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
