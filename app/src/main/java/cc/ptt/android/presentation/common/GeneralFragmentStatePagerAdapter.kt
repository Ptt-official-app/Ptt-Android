package cc.ptt.android.presentation.common

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import java.util.*

class GeneralFragmentStatePagerAdapter(
    fm: FragmentActivity,
    private val fragmentArrayList: List<Fragment>
) : FragmentStateAdapter(fm) {

    override fun createFragment(position: Int): Fragment {
        return fragmentArrayList[position]
    }

    override fun getItemCount(): Int {
        return fragmentArrayList.size
    }
}
