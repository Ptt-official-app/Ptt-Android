package cc.ptt.android.presentation.postarticle

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import cc.ptt.android.R
import cc.ptt.android.data.common.PreferenceConstants
import cc.ptt.android.databinding.PostArticleFragmentLayoutBinding
import cc.ptt.android.presentation.base.BaseFragment
import cc.ptt.android.presentation.common.KeyboardUtils
import cc.ptt.android.presentation.common.StaticValue
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import java.lang.Exception

class PostArticleFragment : BaseFragment() {

    private var _binding: PostArticleFragmentLayoutBinding? = null
    private val binding get() = _binding!!

    private val boardNameTextView: TextView get() = binding.postArticleFragmentTextViewTitle
    private val go2BackBtn: ImageButton get() = binding.articleReadItemHeaderImageViewBack
    private val navigation: BottomNavigationView get() = binding.postArticleFragmentBottomNavigation
    private lateinit var mOnNavigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener

    private val contentEditText: EditText get() = binding.postArticleFragmentEdittextContent
    private val titleEditText: EditText get() = binding.postArticleFragmentEdittextTitle
    private val categoryTextView: TextView get() = binding.postArticleFragmentTextViewCategory

    private val preferences get() = context.getSharedPreferences(PreferenceConstants.prefName, Context.MODE_PRIVATE)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = PostArticleFragmentLayoutBinding.inflate(inflater, container, false).apply {
            _binding = this
        }.root.apply {
            setMainView(this)
        }

        go2BackBtn.setOnClickListener { currentActivity.onBackPressed() }
        mOnNavigationItemSelectedListener =
            BottomNavigationView.OnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.post_article_navigation_item_load_demo -> {}
                    R.id.post_article_navigation_item_load_draft -> {}
                    R.id.post_article_navigation_item_insert_image -> {}
                    R.id.post_article_navigation_item_hide_keyboard ->
                        try {
                            val inputMethodManager = context
                                .getSystemService(
                                    Context.INPUT_METHOD_SERVICE
                                ) as InputMethodManager
                            inputMethodManager.hideSoftInputFromWindow(
                                mainView.windowToken, 0
                            )
                            navigation.menu.getItem(4).isVisible = false
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                }
                false
            }

        with(navigation) {
            labelVisibilityMode = if (preferences.getInt(PreferenceConstants.postBottomStyle, 0) == 0) {
                LabelVisibilityMode.LABEL_VISIBILITY_LABELED
            } else {
                LabelVisibilityMode.LABEL_VISIBILITY_UNLABELED
            }
            menu.clear()
            inflateMenu(R.menu.post_article_bottom_navigation_menu2)
            setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        }

        return view
    }

    private var keyboardMode = false
    private var globalLayoutListener: OnGlobalLayoutListener? = null
    override fun onAnimOver() {
        loadData()
        mainView
            .viewTreeObserver
            .addOnGlobalLayoutListener(
                OnGlobalLayoutListener {
                    val r = Rect()
                    mainView.getWindowVisibleDisplayFrame(r)
                    Log.d(
                        "onPost",
                        "-- " +
                            (
                                mainView
                                    .rootView
                                    .height -
                                    (r.bottom - r.top)
                                )
                    )
                    if (!keyboardMode &&
                        mainView.rootView.height -
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
                        mainView.rootView.height -
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
                }.also {
                    globalLayoutListener = it
                }
            )
    }

    private fun loadData() {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        try {
            KeyboardUtils.hideSoftInput(requireActivity())
        } catch (e: Exception) {
        }
        mainView.viewTreeObserver.removeOnGlobalLayoutListener(globalLayoutListener)
        globalLayoutListener = null
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
