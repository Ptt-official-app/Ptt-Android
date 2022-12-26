package cc.ptt.android.postarticle

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import cc.ptt.android.R
import cc.ptt.android.base.BaseFragment
import cc.ptt.android.common.KeyboardUtils
import cc.ptt.android.common.StaticValue
import cc.ptt.android.data.preference.MainPreferences
import cc.ptt.android.databinding.PostArticleFragmentLayoutBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import org.koin.android.ext.android.inject
import java.lang.Exception

class PostArticleFragment : BaseFragment() {

    private val mainPreferences: MainPreferences by inject()
    private var _binding: PostArticleFragmentLayoutBinding? = null
    private val binding get() = _binding!!

    private val boardNameTextView: TextView get() = binding.postArticleFragmentTextViewTitle
    private val go2BackBtn: ImageButton get() = binding.articleReadItemHeaderImageViewBack
    private val navigation: BottomNavigationView get() = binding.postArticleFragmentBottomNavigation
    private lateinit var mOnNavigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener

    private val contentEditText: EditText get() = binding.postArticleFragmentEdittextContent
    private val titleEditText: EditText get() = binding.postArticleFragmentEdittextTitle
    private val categoryTextView: TextView get() = binding.postArticleFragmentTextViewCategory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = PostArticleFragmentLayoutBinding.inflate(inflater, container, false).apply {
        _binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        go2BackBtn.setOnClickListener { currentActivity.onBackPressed() }
        mOnNavigationItemSelectedListener =
            BottomNavigationView.OnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.post_article_navigation_item_load_demo -> {}
                    R.id.post_article_navigation_item_load_draft -> {}
                    R.id.post_article_navigation_item_insert_image -> {}
                    R.id.post_article_navigation_item_hide_keyboard ->
                        try {
                            val inputMethodManager = requireActivity()
                                .getSystemService(
                                    Context.INPUT_METHOD_SERVICE
                                ) as InputMethodManager
                            inputMethodManager.hideSoftInputFromWindow(
                                binding.root.windowToken, 0
                            )
                            navigation.menu.getItem(4).isVisible = false
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                }
                false
            }

        with(navigation) {
            labelVisibilityMode =
                if (mainPreferences.getPostBottomStyle() == 0) {
                    LabelVisibilityMode.LABEL_VISIBILITY_LABELED
                } else {
                    LabelVisibilityMode.LABEL_VISIBILITY_UNLABELED
                }
            menu.clear()
            inflateMenu(R.menu.post_article_bottom_navigation_menu2)
            setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        }

        binding.root.viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)
    }

    private var keyboardMode = false
    private val globalLayoutListener = OnGlobalLayoutListener {
        _binding?.root?.let {
            val r = Rect()
            it.getWindowVisibleDisplayFrame(r)
            if (!keyboardMode &&
                it.rootView.height -
                (r.bottom - r.top)
            > StaticValue.widthPixels.coerceAtMost(StaticValue.highPixels) /
                2
            ) {
                navigation.menu.clear()
                navigation.inflateMenu(
                    R.menu.post_article_bottom_navigation_menu3
                )
                keyboardMode = true
            } else if (keyboardMode &&
                it.rootView.height -
                (r.bottom - r.top)
                < StaticValue.widthPixels.coerceAtMost(StaticValue.highPixels) /
                2
            ) {
                keyboardMode = false
                navigation.menu.clear()
                navigation.inflateMenu(
                    R.menu.post_article_bottom_navigation_menu2
                )
            }
        }
    }

    override fun onAnimOver() {
        loadData()
    }

    private fun loadData() {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        try {
            KeyboardUtils.hideSoftInput(requireActivity())
        } catch (e: Exception) {
        }
        binding.root.viewTreeObserver.removeOnGlobalLayoutListener(globalLayoutListener)
        _binding = null
    }

    companion object {
        fun newInstance(): PostArticleFragment {
            val args = Bundle()
            val fragment = PostArticleFragment()
            fragment.arguments = args
            return fragment
        }

        fun newInstance(args: Bundle?): PostArticleFragment {
            val fragment = PostArticleFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
