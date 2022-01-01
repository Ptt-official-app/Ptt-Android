package cc.ptt.android.presentation.home.personalpage

import android.content.Context
import android.graphics.PointF
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import cc.ptt.android.R
import cc.ptt.android.data.common.PreferenceConstants
import cc.ptt.android.databinding.PersionalPageFragmentLayoutBinding
import cc.ptt.android.presentation.base.BaseFragment
import cc.ptt.android.presentation.common.GeneralFragmentStatePagerAdapter
import cc.ptt.android.presentation.common.ImageLoadingDrawable
import cc.ptt.android.presentation.home.EmptyFragment
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.drawable.ScalingUtils
import com.facebook.drawee.generic.GenericDraweeHierarchy
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder
import com.facebook.drawee.generic.RoundingParams
import com.facebook.drawee.interfaces.DraweeController
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.common.ResizeOptions
import com.facebook.imagepipeline.request.ImageRequest
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.lang.Exception
import java.util.ArrayList
import kotlin.math.abs

class PersonalPageFragment : BaseFragment() {

    private var _binding: PersionalPageFragmentLayoutBinding? = null
    private val binding get() = _binding!!

    private val mTabs: TabLayout get() = binding.pageTabs
    private val personPicture: SimpleDraweeView get() = binding.personPagePicture
    private val personIDTextView: TextView get() = binding.textViewPersionPageId
    private val personNickTextView: TextView get() = binding.textViewPersionPageNick
    private val personLikeTextView: TextView get() = binding.textViewPersionalLike
    private val mAppBar: AppBarLayout get() = binding.appBarLayoutPersonPage
    private val personPictureMini: SimpleDraweeView get() = binding.personPagePictureMini
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
        }.root.apply {
            setMainView(this)
        }

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

        mAppBar.addOnOffsetChangedListener(
            OnOffsetChangedListener { appBarLayout, verticalOffset ->
                if (isAlreadyReady) {
                    val percent = (
                        abs(verticalOffset).toDouble() /
                            abs(
                                headerRelativeLayout
                                    .height
                            ).toDouble() *
                            100.0
                        ).toInt()
                    val height = headerRelativeLayoutMini.height
                    headerRelativeLayoutMini.y =
                        (height * -1 + height * (percent / 100.0)).toFloat()
                }
                isAlreadyReady = true
            }
        )

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
        ) { tab: TabLayout.Tab, position: Int -> tab.text = fragmentArrayList[position].first }
            .attach()
        loadData()
        return view
    }

    private var isAlreadyReady = false
    fun loadData() {
        setImageView(personPicture, "asset:///List-Of-Android-R-Features.jpeg")
        setImageView(personPictureMini, "asset:///List-Of-Android-R-Features.jpeg")
        val id = currentActivity
            .getSharedPreferences(PreferenceConstants.prefName, Context.MODE_PRIVATE)
            .getString(PreferenceConstants.id, "Guest")
        personIDTextView.text = id
        personIDTextViewMini.text = id
        personNickTextView.text = "匿名訪客"
    }

    private fun setImageView(draweeView: SimpleDraweeView?, Url: String) {
        if (draweeView!!.tag != null) {
            if (draweeView.tag.toString() == Url) {
                return
            }
        }
        draweeView.tag = Url
        try {
            val uri = Uri.parse(Url)
            val request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setLocalThumbnailPreviewsEnabled(true)
                .setProgressiveRenderingEnabled(false)
                .setLowestPermittedRequestLevel(ImageRequest.RequestLevel.FULL_FETCH)
                .setResizeOptions(ResizeOptions(1024, 1024))
                .build()
            val controller: DraweeController = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setAutoPlayAnimations(true)
                .setOldController(draweeView.controller)
                .build()
            val builder = GenericDraweeHierarchyBuilder(this.resources)
            val roundingParams = RoundingParams.fromCornersRadius(200f)
            val pf = PointF(0.5f, 0.5f)
            var hierarchy: GenericDraweeHierarchy? = null
            hierarchy = builder.setActualImageScaleType(ScalingUtils.ScaleType.FOCUS_CROP)
                .setActualImageFocusPoint(pf)
                .setFadeDuration(0)
                .setProgressBarImage(ImageLoadingDrawable())
                .setRoundingParams(roundingParams)
                .build()
            draweeView.controller = controller
            draweeView.hierarchy = hierarchy
        } catch (e: Exception) {
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
