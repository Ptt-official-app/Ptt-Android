package tw.y_studio.ptt.Fragment;

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
import tw.y_studio.ptt.UI.BaseFragment;


public class HomeFragment extends BaseFragment {
    private View Mainview=null;
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

    private Fragment homeFragment;
    private Fragment homeFragment2;
    private Fragment homeFragment3;
    private Fragment homeFragment4;
    private Fragment homeFragment5;
    private Fragment homeFragment6;
    private Fragment preFragment;
    private BottomNavigationView navigation;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;
    private BottomNavigationView.OnNavigationItemReselectedListener mOnNavigationItemReselectedListener;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment_layout, container, false);



        Mainview=view;

        mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

               return changeFragnent(item.getItemId());
                //return true;
            }

        };
        mOnNavigationItemReselectedListener = new BottomNavigationView.OnNavigationItemReselectedListener (){
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.home_bottom_navigation_item_hot_boards:

                        if(homeFragment!=null&&preFragment==homeFragment){
                            ((HotBoardsFragment)homeFragment).scrollToTop();
                        }
                        break;
                    case R.id.home_bottom_navigation_item_favorite_boards:
                        if(homeFragment2!=null&&preFragment==homeFragment2){
                            ((FavoriteBoardsFragment)homeFragment2).scrollToTop();
                        }
                        break;
                    case R.id.home_bottom_navigation_item_hot_articles:
                        if(homeFragment4!=null&&preFragment==homeFragment4){
                            ((HotArticleListFragment)homeFragment4).scrollToTop();
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

        navigation = (BottomNavigationView) Mainview.findViewById(R.id.home_bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setOnNavigationItemReselectedListener(mOnNavigationItemReselectedListener);

        return view;
    }

    private boolean changeFragnent(int id){
        switch (navigation.getSelectedItemId()){
            case R.id.home_bottom_navigation_item_favorite_boards:
                if(homeFragment2!=null && homeFragment2 instanceof FavoriteBoardsFragment){
                    if(((FavoriteBoardsFragment)homeFragment2).isEditMode()){
                        Toast mm = Toast.makeText(getContext(),R.string.attion_close_edit_mode,Toast.LENGTH_SHORT);
                        mm.setGravity(Gravity.CENTER,0,0);
                        mm.show();

                        return false;
                    }
                }
        }

        switch (id) {
            case R.id.home_bottom_navigation_item_hot_boards:
                if(homeFragment==null){
                    Bundle bundle = new Bundle();
                    //bundle.putString("Title","看板分類");
                    homeFragment = HotBoardsFragment.newInstance(bundle);
                }
                if(preFragment==homeFragment){
                    //((HotBoardsFragment)homeFragment).scrollToTop();
                }else {
                    showFragment(homeFragment);
                }
                return true;
            case R.id.home_bottom_navigation_item_favorite_boards:
                if(homeFragment2==null){
                    Bundle bundle = new Bundle();
                    //bundle.putString("Title","看板分類");
                    homeFragment2 = FavoriteBoardsFragment.newInstance(bundle);
                }
                if(preFragment==homeFragment2){
                    //((HotBoardsFragment)homeFragment).scrollToTop();
                }else {
                    showFragment(homeFragment2);
                }
                return true;
            case R.id.home_bottom_navigation_item_hot_articles:
                if(homeFragment4==null){
                    Bundle bundle = new Bundle();
                    //bundle.putString("Title","看板分類");
                    homeFragment4 = HotArticleListFragment.newInstance(bundle);
                }
                if(preFragment==homeFragment4){
                    //((HotBoardsFragment)homeFragment).scrollToTop();
                }else {
                    showFragment(homeFragment4);
                }
                return true;
            case R.id.home_bottom_navigation_item_more_action:
                if(homeFragment5==null){
                    Bundle bundle = new Bundle();
                    //bundle.putString("Title","看板分類");
                    homeFragment5 = SettingFragment.newInstance(bundle);
                }
                if(preFragment==homeFragment5){
                    //((HotBoardsFragment)homeFragment).scrollToTop();
                }else {
                    showFragment(homeFragment5);
                }
                return true;
            case R.id.home_bottom_navigation_item_user_page:
                if(homeFragment6==null){
                    Bundle bundle = new Bundle();
                    //bundle.putString("Title","看板分類");
                    homeFragment6 = PersonalPageFragment.newInstance(bundle);
                }
                if(preFragment==homeFragment6){
                    //((HotBoardsFragment)homeFragment).scrollToTop();
                }else {
                    showFragment(homeFragment6);
                }
                return true;
            default:
                if(homeFragment3==null){
                    Bundle bundle = new Bundle();
                    //bundle.putString("Title","看板分類");
                    homeFragment3 = EmptyFragment.newInstance(bundle);
                }
                if(preFragment==homeFragment3){
                    //((HotBoardsFragment)homeFragment).scrollToTop();
                }else {
                    showFragment(homeFragment3);
                }
                return true;

        }
    }
    protected void onAnimOver() {

        changeFragnent(navigation.getSelectedItemId());

    }

    public void showFragment(Fragment toFragment) {

        if(preFragment==null) {
            getChildFragmentManager()
                    .beginTransaction()
                    .add(R.id.framelayout_home, toFragment, toFragment.getClass().getSimpleName())
                    .show(toFragment)
                    .commit();

        }else {
            if(toFragment.isAdded()) {
                getChildFragmentManager()
                        .beginTransaction()
                        .hide(preFragment)
                        .show(toFragment)
                        .commit();
            }else {
                getChildFragmentManager()
                        .beginTransaction()
                        .add(R.id.framelayout_home, toFragment, toFragment.getClass().getSimpleName())
                        .hide(preFragment)
                        .show(toFragment)
                        .commit();
            }

        }
        preFragment = toFragment;
    }

    public void closeFragment(){

        for(Fragment fr:getChildFragmentManager().getFragments()){
            getChildFragmentManager().beginTransaction().remove(fr).commitAllowingStateLoss();
        }

    }
    @Override
    public void onActivityCreated (Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState!=null){

        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Mainview=null;
    }

}
