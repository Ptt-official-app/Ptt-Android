package cc.ptt.android.home.personalpage

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import cc.ptt.android.R
import cc.ptt.android.base.BaseFragment
import cc.ptt.android.common.GeneralFragmentStatePagerAdapter
import cc.ptt.android.data.preference.UserInfoPreferences
import cc.ptt.android.databinding.PersionalPageFragmentLayoutBinding
import cc.ptt.android.home.EmptyFragment
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.android.ext.android.inject
import java.util.ArrayList
import kotlin.math.abs

class PersonalPageFragment : BaseFragment() {

    private val userInfoPreferences: UserInfoPreferences by inject()

    private var _binding: PersionalPageFragmentLayoutBinding? = null
    private val binding get() = _binding!!

    private val mTabs: TabLayout get() = binding.pageTabs
    private val personPicture: ImageView get() = binding.personPagePicture
    private val personIDTextView: TextView get() = binding.textViewPersionPageId
    private val personNickTextView: TextView get() = binding.textViewPersionPageNick
    private val personLikeTextView: TextView get() = binding.textViewPersionalLike
    private val mAppBar: AppBarLayout get() = binding.appBarLayoutPersonPage
    private val personPictureMini: ImageView get() = binding.personPagePictureMini
    private val personIDTextViewMini: TextView get() = binding.textViewPersonPageIdMini
    private val headerRelativeLayout: RelativeLayout get() = binding.relativeLayoutPersonPageHeader
    private val headerRelativeLayoutMini: RelativeLayout get() = binding.relativeLayoutPersonPageHeaderMini
    private val likeBarRelativeLayout: RelativeLayout get() = binding.relativeLayoutPersonPageLikeBar
    private val mViewPager: ViewPager2 get() = binding.viewPagerPersonPage

    private var fragmentStatePagerAdapter: GeneralFragmentStatePagerAdapter? = null

    private lateinit var fragmentArrayList: ArrayList<Pair<String, Fragment>>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = PersionalPageFragmentLayoutBinding.inflate(inflater, container, false).apply {
            _binding = this
        }.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentArrayList = arrayListOf(
            Pair(getString(R.string.persion_page_tabs_info), PersonInfoFragment.newInstance()),
            Pair(getString(R.string.persion_page_tabs_articles), EmptyFragment.newInstance()),
            Pair(getString(R.string.persion_page_tabs_comments), EmptyFragment.newInstance())
        )

        fragmentArrayList.apply {
            onEach {
                mTabs.addTab(mTabs.newTab().setText(it.first))
            }
        }

        mAppBar.addOnOffsetChangedListener(offsetChangedListener)

        mViewPager.adapter = GeneralFragmentStatePagerAdapter(
            requireActivity(),
            fragmentArrayList.map {
                it.second
            }
        ).apply {
            fragmentStatePagerAdapter = this
        }

        TabLayoutMediator(
            mTabs,
            mViewPager
        ) { tab: TabLayout.Tab, position: Int ->
            tab.text = fragmentArrayList[position].first
        }.attach()

        loadData()
    }

    private val offsetChangedListener = OnOffsetChangedListener { _, verticalOffset ->
        val percent = (
            abs(verticalOffset).toFloat() /
                abs(headerRelativeLayout.height).toFloat() *
                100.0
            ).toInt()
        val height = headerRelativeLayoutMini.height
        if (height > 0) {
            headerRelativeLayoutMini.y =
                (height * -1 + height * (percent / 100.0)).toFloat()
        }
    }

    @SuppressLint("SetTextI18n")
    fun loadData() {
        setImageView(personPicture, null)
        setImageView(personPictureMini, null)
        val id = userInfoPreferences.getLogin()?.userId.orEmpty()
        personIDTextView.text = id
        personIDTextViewMini.text = id
        personNickTextView.text = "匿名訪客"
        personLikeTextView.text = "1.8k"
    }

    private fun setImageView(imageView: ImageView, url: String?) {
        if (imageView.tag != null) {
            if (imageView.tag.toString() == url) {
                return
            }
        }
        imageView.tag = url

        url?.let {
            imageView.load(it) {
                placeholder(R.drawable.person_picture)
                transformations(CircleCropTransformation())
            }
        } ?: run {
            imageView.load(R.drawable.person_picture) {
                transformations(CircleCropTransformation())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): PersonalPageFragment {
            val args = Bundle()
            val fragment = PersonalPageFragment()
            fragment.arguments = args
            return fragment
        }

        fun newInstance(args: Bundle?): PersonalPageFragment {
            val fragment = PersonalPageFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
