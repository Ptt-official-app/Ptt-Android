package tw.y_studio.ptt;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import tw.y_studio.ptt.fragment.ArticleListSearchFragment;
import tw.y_studio.ptt.fragment.HomeFragment;
import tw.y_studio.ptt.fragment.HotArticleFilterFragment;
import tw.y_studio.ptt.fragment.LoginPageFragment;
import tw.y_studio.ptt.fragment.PostArticleFragment;
import tw.y_studio.ptt.fragment.SearchBoardsFragment;
import tw.y_studio.ptt.ui.BaseActivity;
import tw.y_studio.ptt.ui.StaticValue;
import tw.y_studio.ptt.ui.article.list.ArticleListFragment;
import tw.y_studio.ptt.ui.article.read.ArticleReadFragment;
import tw.y_studio.ptt.ui.common.extension.NavExtKt;

import java.util.Date;
import java.util.Objects;

public class HomeActivity extends BaseActivity {
    private HomeFragment homeFragment;
    private int themeType = 0;
    private long TimeTemp = 0;

    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        SharedPreferences preference2 = getSharedPreferences("MainSetting", MODE_PRIVATE);

        themeType = preference2.getInt("THEME", 0);

        StaticValue.ThemMode = themeType;

        if (themeType == 1) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else if (themeType == 0) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }

        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getTheme();
        theme.resolveAttribute(R.attr.black, typedValue, true);
        @ColorInt int color = typedValue.data;
        Window window = this.getWindow();
        window.setStatusBarColor(color);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

        if (themeType == 0) {
            window.getDecorView().setSystemUiVisibility(0);
        } else if (themeType == 1) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_main);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        StaticValue.densityDpi = metrics.densityDpi;
        StaticValue.ScreenDensity = metrics.density;
        StaticValue.widthPixels = metrics.widthPixels;
        StaticValue.highPixels = metrics.heightPixels;

        /**
         * TODO when refactor to Kotlin we can declare: private val navController by lazy {
         * (supportFragmentManager.findFragmentById( R.id.nav_host_fragment) as
         * NavHostFragment).navController }
         */
        navController =
                ((NavHostFragment)
                                Objects.requireNonNull(
                                        getSupportFragmentManager()
                                                .findFragmentById(R.id.nav_host_fragment)))
                        .getNavController();

        isReadyLaunch = true;
    }

    private boolean isReadyShowHome = false;
    private boolean isReadyLaunch = false;

    @Override
    public void closeAllFragment() {
        try {
            while (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                String backStackId =
                        getSupportFragmentManager()
                                .getBackStackEntryAt(
                                        getSupportFragmentManager().getBackStackEntryCount() - 1)
                                .getName();
                getSupportFragmentManager()
                        .popBackStackImmediate(
                                backStackId, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void loadFragment(Fragment toFragment, Fragment thisFragment) throws Exception {
        int id = 0;
        if (toFragment instanceof LoginPageFragment) {
            id = R.id.loginPageFragment;
        } else if (toFragment instanceof ArticleListFragment) {
            id = R.id.articleListFragment;
        } else if (toFragment instanceof ArticleReadFragment) {
            id = R.id.articleReadFragment;
        } else if (toFragment instanceof ArticleListSearchFragment) {
            id = R.id.articleListSearchFragment;
        } else if (toFragment instanceof PostArticleFragment) {
            id = R.id.postArticleFragment;
        }
        NavExtKt.navigateForward(navController, id, toFragment.getArguments(), false, true);
    }

    @Override
    public void loadFragmentNoAnim(Fragment toFragment, Fragment thisFragment) throws Exception {
        int id = 0;
        if (toFragment instanceof SearchBoardsFragment) {
            id = R.id.searchBoardsFragment;
        } else if (toFragment instanceof HotArticleFilterFragment) {
            id = R.id.hotArticleFilterFragment;
        }
        NavExtKt.navigateForward(navController, id, toFragment.getArguments(), false, false);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) { // 確定按下退出鍵and防止重複按下退出鍵
            // TODO refactor this check
            if (!navController
                    .getCurrentDestination()
                    .getLabel()
                    .equals(HomeFragment.class.getSimpleName())) {
                onBackPressed();
            } else {
                Date myDate = new Date();
                long myDate2 = myDate.getTime();
                if (Math.abs(TimeTemp - myDate2) > 1500) {
                    Toast.makeText(
                                    this,
                                    getString(R.string.press_again_to_leave),
                                    Toast.LENGTH_SHORT)
                            .show();
                    TimeTemp = myDate2;
                } else {
                    finish();
                    System.gc();
                }
            }
        } else {
            super.onKeyDown(keyCode, event);
        }
        return false;
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
