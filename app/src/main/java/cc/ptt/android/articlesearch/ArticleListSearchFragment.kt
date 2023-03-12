package cc.ptt.android.articlesearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cc.ptt.android.R
import cc.ptt.android.base.BaseFragment
import cc.ptt.android.databinding.ArticleListSearchFragmentLayoutBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class ArticleListSearchFragment : BaseFragment() {

    private var _binding: ArticleListSearchFragmentLayoutBinding? = null
    private val binding get() = _binding!!

    private val boardName = "搜尋文章"
    private val boardSubName = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ArticleListSearchFragmentLayoutBinding.inflate(inflater, container, false).apply {
            _binding = this
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            binding.articleListFragmentTextViewTitle.text = boardName
            binding.articleListFragmentTextViewSubtitle.text = boardSubName

            binding.articleReadItemHeaderImageViewBack.setOnClickListener {
                requireActivity().onBackPressed()
            }

            binding.articleListFragmentBottomNavigation.setOnNavigationItemSelectedListener(
                BottomNavigationView.OnNavigationItemSelectedListener { item ->
                    when (item.itemId) {
                        R.id.article_list_search__navigation_item_cross -> {
                            requireActivity().onBackPressed()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
