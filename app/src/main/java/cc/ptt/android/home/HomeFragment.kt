package cc.ptt.android.home

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import cc.ptt.android.R
import cc.ptt.android.base.BaseFragment
import cc.ptt.android.databinding.HomeFragmentLayoutBinding
import cc.ptt.android.home.favoriteboards.FavoriteBoardsFragment
import cc.ptt.android.home.hotarticle.HotArticleListFragment
import cc.ptt.android.home.hotboard.HotBoardsFragment
import cc.ptt.android.home.personalpage.PersonalPageFragment
import cc.ptt.android.home.setting.SettingFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemReselectedListener
import java.util.*

class HomeFragment : BaseFragment() {

    enum class PageType(val value: Int) {
        HotBoards(1),
        FavoriteBoards(2),
        HotArticles(4),
        MoreActions(5),
        UserPage(6),
        EmptyPage(3),
    }

    private val homeFragmentMap: MutableMap<PageType, Fragment> = EnumMap(PageType::class.java)
    private var preFragment: Fragment? = null
    private val navigation: BottomNavigationView get() = binding.homeBottomNavigation

    private var mOnNavigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener? = null
    private var mOnNavigationItemReselectedListener: OnNavigationItemReselectedListener? = null

    private var _binding: HomeFragmentLayoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = HomeFragmentLayoutBinding.inflate(inflater, container, false).apply {
            _binding = this
        }.root
        return view
    }

    private fun changeFragment(id: Int): Boolean {
        when (navigation.selectedItemId) {
            R.id.home_bottom_navigation_item_favorite_boards ->
                if (homeFragmentMap.containsKey(PageType.FavoriteBoards) &&
                    homeFragmentMap[PageType.FavoriteBoards] is FavoriteBoardsFragment
                ) {
                    if ((homeFragmentMap[PageType.FavoriteBoards] as? FavoriteBoardsFragment)?.isEditMode() == true) {
                        val mm = Toast.makeText(
                            context,
                            R.string.attion_close_edit_mode,
                            Toast.LENGTH_SHORT
                        )
                        mm.setGravity(Gravity.CENTER, 0, 0)
                        mm.show()
                        return false
                    }
                }
        }
        val fragment: Fragment?
        val key = getPageTypeById(id)
        if (!homeFragmentMap.containsKey(key)) {
            fragment = when (id) {
                R.id.home_bottom_navigation_item_hot_boards -> HotBoardsFragment.newInstance(Bundle())
                R.id.home_bottom_navigation_item_favorite_boards -> FavoriteBoardsFragment.newInstance(
                    Bundle()
                )
                R.id.home_bottom_navigation_item_hot_articles -> HotArticleListFragment.newInstance(
                    Bundle()
                )
                R.id.home_bottom_navigation_item_more_action -> SettingFragment.newInstance(Bundle())
                R.id.home_bottom_navigation_item_user_page -> PersonalPageFragment.newInstance(
                    Bundle()
                )
                else -> EmptyFragment.newInstance(Bundle())
            }.apply {
                homeFragmentMap[key] = this
            }
        } else {
            fragment = homeFragmentMap[key]
        }

        if (fragment !== preFragment) {
            homeFragmentMap[key]?.let { showFragment(it) }
        }
        return true
    }

    private fun getPageTypeById(id: Int): PageType {
        return when (id) {
            R.id.home_bottom_navigation_item_hot_boards -> PageType.HotBoards
            R.id.home_bottom_navigation_item_favorite_boards -> PageType.FavoriteBoards
            R.id.home_bottom_navigation_item_hot_articles -> PageType.HotArticles
            R.id.home_bottom_navigation_item_more_action -> PageType.MoreActions
            R.id.home_bottom_navigation_item_user_page -> PageType.UserPage
            else -> PageType.EmptyPage
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mOnNavigationItemSelectedListener =
            BottomNavigationView.OnNavigationItemSelectedListener { item -> changeFragment(item.itemId) }
        mOnNavigationItemReselectedListener = OnNavigationItemReselectedListener { item ->
            when (item.itemId) {
                R.id.home_bottom_navigation_item_hot_boards ->
                    if (homeFragmentMap.containsKey(
                            PageType.HotBoards
                        )
                    ) {
                        (homeFragmentMap[PageType.HotBoards] as? HotBoardsFragment)?.scrollToTop()
                    }
                R.id.home_bottom_navigation_item_favorite_boards ->
                    if (homeFragmentMap.containsKey(
                            PageType.FavoriteBoards
                        )
                    ) {
                        (homeFragmentMap[PageType.FavoriteBoards] as? FavoriteBoardsFragment)?.scrollToTop()
                    }
                R.id.home_bottom_navigation_item_hot_articles ->
                    if (homeFragmentMap.containsKey(
                            PageType.HotArticles
                        )
                    ) {
                        (homeFragmentMap[PageType.HotArticles] as? HotArticleListFragment)?.scrollToTop()
                    }
                R.id.home_bottom_navigation_item_user_page -> {}
                R.id.home_bottom_navigation_item_more_action -> {}
                else -> {}
            }
        }
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navigation.setOnNavigationItemReselectedListener(mOnNavigationItemReselectedListener)
    }

    override fun onAnimOver() {
        super.onAnimOver()
        changeFragment(navigation.selectedItemId)
    }

    private fun showFragment(toFragment: Fragment) {
        preFragment?.let {
            if (toFragment.isAdded) {
                childFragmentManager
                    .beginTransaction()
                    .hide(preFragment!!)
                    .show(toFragment)
                    .commit()
            } else {
                childFragmentManager
                    .beginTransaction()
                    .add(
                        R.id.framelayout_home,
                        toFragment,
                        toFragment.javaClass.simpleName
                    )
                    .hide(preFragment!!)
                    .show(toFragment)
                    .commit()
            }
        } ?: run {
            childFragmentManager
                .beginTransaction()
                .add(R.id.framelayout_home, toFragment, toFragment.javaClass.simpleName)
                .show(toFragment)
                .commit()
        }

        preFragment = toFragment
    }

    fun closeFragment() {
        for (fr in childFragmentManager.fragments) {
            childFragmentManager.beginTransaction().remove(fr!!).commitAllowingStateLoss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        homeFragmentMap.clear()
    }

    companion object {
        fun newInstance(): HomeFragment {
            val args = Bundle()
            val fragment = HomeFragment()
            fragment.arguments = args
            return fragment
        }

        fun newInstance(args: Bundle?): HomeFragment {
            val fragment = HomeFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
