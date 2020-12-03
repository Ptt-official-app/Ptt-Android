package tw.y_studio.ptt.ui.fragment;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import tw.y_studio.ptt.R;
import tw.y_studio.ptt.ui.BaseFragment;
import tw.y_studio.ptt.ui.setting.SettingFragment;

import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends BaseFragment {

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static HomeFragment newInstance(Bundle args) {
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private Map<String, Fragment> homeFragmentMap = new HashMap<>();
    private Fragment preFragment;
    private BottomNavigationView navigation;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;
    private BottomNavigationView.OnNavigationItemReselectedListener
            mOnNavigationItemReselectedListener;

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment_layout, container, false);

        setMainView(view);

        mOnNavigationItemSelectedListener =
                new BottomNavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        return changeFragnent(item.getItemId());
                    }
                };

        mOnNavigationItemReselectedListener =
                new BottomNavigationView.OnNavigationItemReselectedListener() {

                    @Override
                    public void onNavigationItemReselected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.home_bottom_navigation_item_hot_boards:
                                if (homeFragmentMap.containsKey("0")) {
                                    ((HotBoardsFragment) homeFragmentMap.get("0")).scrollToTop();
                                }
                                break;
                            case R.id.home_bottom_navigation_item_favorite_boards:
                                if (homeFragmentMap.containsKey("2")) {
                                    ((FavoriteBoardsFragment) homeFragmentMap.get("2"))
                                            .scrollToTop();
                                }
                                break;
                            case R.id.home_bottom_navigation_item_hot_articles:
                                if (homeFragmentMap.containsKey("4")) {
                                    ((HotArticleListFragment) homeFragmentMap.get("4"))
                                            .scrollToTop();
                                }
                                break;
                            case R.id.home_bottom_navigation_item_user_page:
                                break;
                            case R.id.home_bottom_navigation_item_more_action:
                                break;
                            default:
                                break;
                        }
                    }
                };

        navigation = (BottomNavigationView) getMainView().findViewById(R.id.home_bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setOnNavigationItemReselectedListener(mOnNavigationItemReselectedListener);

        return view;
    }

    private boolean changeFragnent(int id) {
        switch (navigation.getSelectedItemId()) {
            case R.id.home_bottom_navigation_item_favorite_boards:
                if (homeFragmentMap.containsKey("2")
                        && homeFragmentMap.get("2") instanceof FavoriteBoardsFragment) {
                    if (((FavoriteBoardsFragment) homeFragmentMap.get("2")).isEditMode()) {
                        Toast mm =
                                Toast.makeText(
                                        getContext(),
                                        R.string.attion_close_edit_mode,
                                        Toast.LENGTH_SHORT);
                        mm.setGravity(Gravity.CENTER, 0, 0);
                        mm.show();

                        return false;
                    }
                }
        }

        Fragment fragment;
        String key = "";

        switch (id) {
            case R.id.home_bottom_navigation_item_hot_boards:
                key = "0";
                break;
            case R.id.home_bottom_navigation_item_favorite_boards:
                key = "2";
                break;
            case R.id.home_bottom_navigation_item_hot_articles:
                key = "4";
                break;
            case R.id.home_bottom_navigation_item_more_action:
                key = "5";
                break;
            case R.id.home_bottom_navigation_item_user_page:
                key = "6";
                break;
            default:
                key = "3";
                break;
        }

        if (!homeFragmentMap.containsKey(key)) {
            switch (id) {
                case R.id.home_bottom_navigation_item_hot_boards:
                    fragment = HotBoardsFragment.newInstance(new Bundle());
                    break;
                case R.id.home_bottom_navigation_item_favorite_boards:
                    fragment = FavoriteBoardsFragment.newInstance(new Bundle());
                    break;
                case R.id.home_bottom_navigation_item_hot_articles:
                    fragment = HotArticleListFragment.newInstance(new Bundle());
                    break;
                case R.id.home_bottom_navigation_item_more_action:
                    fragment = SettingFragment.newInstance(new Bundle());
                    break;
                case R.id.home_bottom_navigation_item_user_page:
                    fragment = PersonalPageFragment.newInstance(new Bundle());
                    break;
                default:
                    fragment = EmptyFragment.newInstance(new Bundle());
                    break;
            }
            homeFragmentMap.put(key, fragment);
        } else {
            fragment = homeFragmentMap.get(key);
        }
        if (fragment != preFragment) {
            showFragment(homeFragmentMap.get(key));
        }

        return true;
    }

    protected void onAnimOver() {
        changeFragnent(navigation.getSelectedItemId());
    }

    public void showFragment(Fragment toFragment) {
        if (preFragment == null) {
            getChildFragmentManager()
                    .beginTransaction()
                    .add(R.id.framelayout_home, toFragment, toFragment.getClass().getSimpleName())
                    .show(toFragment)
                    .commit();
        } else {
            if (toFragment.isAdded()) {
                getChildFragmentManager()
                        .beginTransaction()
                        .hide(preFragment)
                        .show(toFragment)
                        .commit();
            } else {
                getChildFragmentManager()
                        .beginTransaction()
                        .add(
                                R.id.framelayout_home,
                                toFragment,
                                toFragment.getClass().getSimpleName())
                        .hide(preFragment)
                        .show(toFragment)
                        .commit();
            }
        }
        preFragment = toFragment;
    }

    public void closeFragment() {
        for (Fragment fr : getChildFragmentManager().getFragments()) {
            getChildFragmentManager().beginTransaction().remove(fr).commitAllowingStateLoss();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
