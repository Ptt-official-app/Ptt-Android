package tw.y_studio.ptt.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import tw.y_studio.ptt.R;
import tw.y_studio.ptt.UI.BaseFragment;
import tw.y_studio.ptt.UI.ClickFix;


public class ArticleListSearchFragment extends BaseFragment {

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
    private ImageButton Go2Back;

    private BottomNavigationView navigation;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;

    private ClickFix mClickFix = new ClickFix();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.article_list_search_fragment_layout, container, false);

        setMainView(view);

        Bundle bundle = getArguments();//取得Bundle

        mTextView_BoardName = findViewById(R.id.article_list_fragment_textView_title);
        mTextView_BoardSubName = findViewById(R.id.article_list_fragment_textView_subtitle);
        Go2Back = findViewById(R.id.article_read_item_header_imageView_back);
        navigation = (BottomNavigationView) findViewById(R.id.article_list_fragment_bottom_navigation);

        mTextView_BoardName.setText(BoardName);
        mTextView_BoardSubName.setText(BoardSubName);


        Go2Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentActivity().onBackPressed();
            }
        });

        mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.article_list_search__navigation_item_cross:
                        getCurrentActivity().onBackPressed();
                        return false;
                    case R.id.article_list_search__navigation_item_sure:
                    default:

                }
                return false;
            }

        };

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        return view;
    }

    private boolean GattingData = false;

    private void loadData(){
        if(GattingData) return;

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getCurrentActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getMainView().getWindowToken(), 0);
        } catch (Exception e) {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
