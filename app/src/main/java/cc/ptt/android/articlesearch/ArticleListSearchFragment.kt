package cc.ptt.android.articlesearch

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import cc.ptt.android.R
import cc.ptt.android.base.BaseFragment
import cc.ptt.android.common.ClickFix
import cc.ptt.android.databinding.ArticleListSearchFragmentLayoutBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class ArticleListSearchFragment : BaseFragment() {

    private var _binding: ArticleListSearchFragmentLayoutBinding? = null
    private val binding get() = _binding!!

    private val mClickFix = ClickFix()

    private val BoardName = "搜尋文章"
    private val BoardSubName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments // 取得Bundle
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ArticleListSearchFragmentLayoutBinding.inflate(inflater, container, false).apply {
            _binding = this
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            binding.articleListFragmentTextViewTitle.text = BoardName
            binding.articleListFragmentTextViewSubtitle.text = BoardSubName

            binding.articleReadItemHeaderImageViewBack.setOnClickListener {
                currentActivity.onBackPressed()
            }

            binding.articleListFragmentBottomNavigation.setOnNavigationItemSelectedListener(
                BottomNavigationView.OnNavigationItemSelectedListener { item ->
                    when (item.itemId) {
                        R.id.article_list_search__navigation_item_cross -> {
                            currentActivity.onBackPressed()
                            return@OnNavigationItemSelectedListener false
                        }
                        R.id.article_list_search__navigation_item_sure -> {
                            return@OnNavigationItemSelectedListener false
                        }
                        else -> {
                        }
                    }
                    false
                }
            )
        }
    }

    override fun onAnimOver() {
    }

    override fun onDestroyView() {
        super.onDestroyView()

        try {
            val inputMethodManager = currentActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(_binding?.root?.windowToken, 0)
        } catch (e: Exception) {
        }

        _binding = null
    }

    companion object {
        fun newInstance(): ArticleListSearchFragment {
            val args = Bundle()
            val fragment = ArticleListSearchFragment()
            fragment.arguments = args
            return fragment
        }

        @JvmStatic
        fun newInstance(args: Bundle?): ArticleListSearchFragment {
            val fragment = ArticleListSearchFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
