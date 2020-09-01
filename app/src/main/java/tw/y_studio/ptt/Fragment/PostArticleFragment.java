package tw.y_studio.ptt.Fragment;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tw.y_studio.ptt.API.PostListAPIHelper;
import tw.y_studio.ptt.R;
import tw.y_studio.ptt.UI.ClickFix;
import tw.y_studio.ptt.UI.StaticValue;

import static android.content.Context.MODE_PRIVATE;

public class PostArticleFragment extends Fragment {
    private View Mainview=null;
    public static PostArticleFragment newInstance() {
        Bundle args = new Bundle();
        PostArticleFragment fragment = new PostArticleFragment();
        fragment.setArguments(args);
        return fragment;
    }
    public static PostArticleFragment newInstance(Bundle args) {
        PostArticleFragment fragment = new PostArticleFragment();
        fragment.setArguments(args);
        return fragment;
    }



    private TextView mTextView_BoardName;

    private AppCompatImageButton Go2Back;
    private BottomNavigationView navigation;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;
    private EditText content;
    private EditText title;
    private TextView ctaegory;
    private ClickFix mClickFix = new ClickFix();
    private LinearLayout bottomBar;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.post_article_fragment_layout, container, false);



        Mainview=view;

        Bundle bundle = getArguments();//取得Bundle

        mTextView_BoardName = Mainview.findViewById(R.id.ppst_article_fragment_textView_title);

        Go2Back = Mainview.findViewById(R.id.article_read_item_header_imageView_back);
        Go2Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        ctaegory = Mainview.findViewById(R.id.post_article_fragment_textView_category);
        content = Mainview.findViewById(R.id.post_article_fragment_edittext_content);
        title = Mainview.findViewById(R.id.post_article_fragment_edittext_title);



        mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.post_article_navigation_item_load_demo:
                        break;
                    case R.id.post_article_navigation_item_load_draft:
                        break;
                    case R.id.post_article_navigation_item_insert_image:
                        break;
                    case R.id.post_article_navigation_item_hide_keyboard:
                        try {
                            InputMethodManager inputMethodManager = (InputMethodManager)  getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(Mainview.getWindowToken(), 0);
                            navigation.getMenu().getItem(4).setVisible(false);
                            Log.d("onPostArticle","navigation.getMenu().getItem(4).setVisible(false);");
                        }catch (Exception e){

                        }
                        break;



                }
                return false;
            }

        };
        navigation = (BottomNavigationView) Mainview.findViewById(R.id.post_article_fragment_bottom_navigation);
        if(getContext().getSharedPreferences("MainSetting", MODE_PRIVATE).getInt("POSTBOTTOMSTYLE",0)==0){
            navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        }else {
            navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_UNLABELED);
        }

        navigation.getMenu().clear();
        navigation.inflateMenu(R.menu.post_article_bottom_navigation_menu2);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        onAnimOver();
        return view;
    }

    private boolean keyboardMode=false;
    private ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener;
    protected void onAnimOver() {


        loadData();

        Mainview.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener=new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(Mainview!=null){
                    Rect r = new Rect();
                    Mainview.getWindowVisibleDisplayFrame(r);
                    Log.d("onPost","-- "+(Mainview.getRootView().getHeight() - (r.bottom - r.top)));
                    if (!keyboardMode&&Mainview.getRootView().getHeight() - (r.bottom - r.top) > Math.min(StaticValue.widthPixels,StaticValue.highPixels)/2) { // if more than 100 pixels, its probably a keyboard...
                        //navigation.getMenu().getItem(4).setVisible(true);
                        navigation.getMenu().clear();
                        navigation.inflateMenu(R.menu.post_article_bottom_navigation_menu3);
                        keyboardMode=true;
                    } else if(keyboardMode&&Mainview.getRootView().getHeight() - (r.bottom - r.top) < Math.min(StaticValue.widthPixels,StaticValue.highPixels)/2){
                        keyboardMode=false;
                        navigation.getMenu().clear();
                        navigation.inflateMenu(R.menu.post_article_bottom_navigation_menu2);

                    }
                }

            }
        });






    }

    private Handler mUI_Handler = new Handler();
    private Handler mThreadHandler;
    private HandlerThread mThread;
    private Runnable r1;

    private String orgUrl = "";
    private String nextUrl = "";
    private String getDataErr = "";
    private int alreadyGetNum=1;
    private int nowNum=99999;
    private List<Map<String, Object>> data_temp = new ArrayList<>();

    private PostListAPIHelper postListAPI;
    private int NowApiNum = 0;



    private boolean haveApi = true;
    private boolean GattingData = false;
    private void loadData(){
        if(GattingData) return;


    }
    private void initView() throws Exception{

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            InputMethodManager inputMethodManager = (InputMethodManager)  getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(Mainview.getWindowToken(), 0);
        }catch (Exception e){

        }
        Mainview.getViewTreeObserver().removeOnGlobalLayoutListener(globalLayoutListener);
        globalLayoutListener=null;
        Mainview=null;
    }
    @Override
    public void onDestroy() {

        super.onDestroy();
        if(postListAPI!=null){
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
