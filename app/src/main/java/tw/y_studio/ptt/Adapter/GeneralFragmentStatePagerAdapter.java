package tw.y_studio.ptt.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class GeneralFragmentStatePagerAdapter extends FragmentStateAdapter {
    private ArrayList<Fragment> fragmentArrayList;

    public GeneralFragmentStatePagerAdapter(
            @NonNull FragmentActivity fm, @NonNull ArrayList<Fragment> fragmentArrayList) {
        super(fm);
        this.fragmentArrayList = fragmentArrayList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentArrayList.get(position);
    }

    @Override
    public int getItemCount() {
        return fragmentArrayList.size();
    }
}
