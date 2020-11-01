package tw.y_studio.ptt.UI;

import android.app.Activity;
import android.content.res.ColorStateList;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class UiFix {



    /**
     * Method to change the color state of bottom bar view
     * @param bottomNavigationView - BottomNavigation view instance
     * @param checkedColorHex int value of checked color code
     * @param uncheckedColorHex int value of unchecked color code
     */
    public static void changeMenuItemCheckedStateColor(BottomNavigationView bottomNavigationView,
                                                       int checkedColorHex, int uncheckedColorHex) {

        int[][] states = new int[][]{
                new int[]{-android.R.attr.state_checked}, // unchecked
                new int[]{android.R.attr.state_checked}, // checked
        };

        int[] colors = new int[]{
                uncheckedColorHex,
                checkedColorHex
        };

        ColorStateList colorStateList = new ColorStateList(states, colors);
        bottomNavigationView.setItemTextColor(colorStateList);
        bottomNavigationView.setItemIconTintList(colorStateList);

    }

    public static void setSwipeRefreshLayoutBackground(SwipeRefreshLayout mSwipeRefreshLayout,Activity _mActivity){

            /*TypedValue typedValue = new TypedValue();
            Resources.Theme theme = _mActivity.getTheme();
            theme.resolveAttribute(R.attr.darkGrey, typedValue, true);
            @ColorInt int color = typedValue.data;

            mSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(color);*/


    }
}
