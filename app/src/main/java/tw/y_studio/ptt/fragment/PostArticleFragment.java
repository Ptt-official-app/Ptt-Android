package tw.y_studio.ptt.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;

import tw.y_studio.ptt.R;
import tw.y_studio.ptt.ui.BaseFragment;
import tw.y_studio.ptt.ui.ClickFix;
import tw.y_studio.ptt.ui.StaticValue;

public class PostArticleFragment extends BaseFragment {

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

    private ImageButton Go2Back;
    private BottomNavigationView navigation;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;
    private EditText content;
    private EditText title;
    private TextView ctaegory;
    private ClickFix mClickFix = new ClickFix();
    private LinearLayout bottomBar;

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.post_article_fragment_layout, container, false);

        setMainView(view);

        mTextView_BoardName = findViewById(R.id.ppst_article_fragment_textView_title);
        Go2Back = findViewById(R.id.article_read_item_header_imageView_back);

        ctaegory = findViewById(R.id.post_article_fragment_textView_category);
        content = findViewById(R.id.post_article_fragment_edittext_content);
        title = findViewById(R.id.post_article_fragment_edittext_title);

        navigation =
                (BottomNavigationView) findViewById(R.id.post_article_fragment_bottom_navigation);

        Bundle bundle = getArguments(); // 取得Bundle

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
                            case R.id.post_article_navigation_item_load_demo:
                                break;
                            case R.id.post_article_navigation_item_load_draft:
                                break;
                            case R.id.post_article_navigation_item_insert_image:
                                break;
                            case R.id.post_article_navigation_item_hide_keyboard:
                                try {
                                    InputMethodManager inputMethodManager =
                                            (InputMethodManager)
                                                    getContext()
                                                            .getSystemService(
                                                                    Context.INPUT_METHOD_SERVICE);
                                    inputMethodManager.hideSoftInputFromWindow(
                                            getMainView().getWindowToken(), 0);
                                    navigation.getMenu().getItem(4).setVisible(false);
                                    Log.d(
                                            "onPostArticle",
                                            "navigation.getMenu().getItem(4).setVisible(false);");
                                } catch (Exception e) {
                                }
                                break;
                        }
                        return false;
                    }
                };

        if (getContext()
                        .getSharedPreferences("MainSetting", MODE_PRIVATE)
                        .getInt("POSTBOTTOMSTYLE", 0)
                == 0) {
            navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        } else {
            navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_UNLABELED);
        }

        navigation.getMenu().clear();
        navigation.inflateMenu(R.menu.post_article_bottom_navigation_menu2);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        return view;
    }

    private boolean keyboardMode = false;
    private ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener;

    protected void onAnimOver() {
        loadData();

        getMainView()
                .getViewTreeObserver()
                .addOnGlobalLayoutListener(
                        globalLayoutListener =
                                new ViewTreeObserver.OnGlobalLayoutListener() {

                                    @Override
                                    public void onGlobalLayout() {
                                        if (getMainView() != null) {
                                            Rect r = new Rect();
                                            getMainView().getWindowVisibleDisplayFrame(r);
                                            Log.d(
                                                    "onPost",
                                                    "-- "
                                                            + (getMainView()
                                                                            .getRootView()
                                                                            .getHeight()
                                                                    - (r.bottom - r.top)));
                                            if (!keyboardMode
                                                    && getMainView().getRootView().getHeight()
                                                                    - (r.bottom - r.top)
                                                            > Math.min(
                                                                            StaticValue.widthPixels,
                                                                            StaticValue.highPixels)
                                                                    / 2) { // if more than 100
                                                // pixels, its probably a
                                                // keyboard...
                                                // navigation.getMenu().getItem(4).setVisible(true);
                                                navigation.getMenu().clear();
                                                navigation.inflateMenu(
                                                        R.menu.post_article_bottom_navigation_menu3);
                                                keyboardMode = true;
                                            } else if (keyboardMode
                                                    && getMainView().getRootView().getHeight()
                                                                    - (r.bottom - r.top)
                                                            < Math.min(
                                                                            StaticValue.widthPixels,
                                                                            StaticValue.highPixels)
                                                                    / 2) {
                                                keyboardMode = false;
                                                navigation.getMenu().clear();
                                                navigation.inflateMenu(
                                                        R.menu.post_article_bottom_navigation_menu2);
                                            }
                                        }
                                    }
                                });
    }

    private boolean GattingData = false;

    private void loadData() {
        if (GattingData) return;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        try {
            InputMethodManager inputMethodManager =
                    (InputMethodManager)
                            getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getMainView().getWindowToken(), 0);
        } catch (Exception e) {
        }

        getMainView().getViewTreeObserver().removeOnGlobalLayoutListener(globalLayoutListener);
        globalLayoutListener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
