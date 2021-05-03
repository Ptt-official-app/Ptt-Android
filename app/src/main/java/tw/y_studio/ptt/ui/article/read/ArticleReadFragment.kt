package tw.y_studio.ptt.ui.article.read

import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import tw.y_studio.ptt.R
import tw.y_studio.ptt.api.PostRankMark
import tw.y_studio.ptt.api.model.board.article.Article
import tw.y_studio.ptt.databinding.ArticleReadFragmentLayoutBinding
import tw.y_studio.ptt.fragment.login.LoginPageFragment
import tw.y_studio.ptt.ui.BaseFragment
import tw.y_studio.ptt.ui.CustomLinearLayoutManager
import tw.y_studio.ptt.utils.*

class ArticleReadFragment : BaseFragment() {
    private var _binding: ArticleReadFragmentLayoutBinding? = null
    private val binding get() = _binding!!
    private var adapter: ArticleReadAdapter? = null

    private val article by bundleDelegate<Article>()

    private val boardName by bundleDelegate<String>()

    private val haveApi = true

    private var progressDialog: ProgressDialog? = null

    private val viewModel: ArticleReadViewModel by viewModel()

    private val preferences: SharedPreferences by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ArticleReadFragmentLayoutBinding.inflate(inflater, container, false).apply {
            _binding = this
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            articleReadItemEditTextReply.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    articleReadItemLinearlayoutOrgLeft.visibility = View.GONE
                    articleReadItemLinearlayoutOrgRight.visibility = View.GONE
                    articleReadItemLinearlayoutReplyLeft.visibility = View.VISIBLE
                    articleReadItemLinearlayoutReplyRight.visibility = View.VISIBLE
                    articleReadItemEditTextReply.isSingleLine = false
                    articleReadItemEditTextReply.maxLines = 5
                }
            }
            articleReadItemImageButtonHideReply.setOnClickListener {
                articleReadItemEditTextReply.clearFocus()
                articleReadItemLinearlayoutOrgLeft.visibility = View.VISIBLE
                articleReadItemLinearlayoutOrgRight.visibility = View.VISIBLE
                articleReadItemLinearlayoutReplyLeft.visibility = View.GONE
                articleReadItemLinearlayoutReplyRight.visibility = View.GONE
                articleReadItemEditTextReply.isSingleLine = true
            }
            articleReadItemImageButtonLike.setOnClickListener { setRankMenu(it) }
            articleReadItemImageButtonShare.setOnClickListener {
                shareTo(
                    requireContext(),
                    viewModel.originalTitle(article.classX, article.title),
                    """
                                ${viewModel.originalTitle(article.classX, article.title)}
                                ${article.url}
                    """.trimIndent(),
                    "分享文章"
                )
            }
            articleReadFragmentRecyclerView.apply {
                this@ArticleReadFragment.adapter = ArticleReadAdapter(viewModel.data)
                setHasFixedSize(true)
                val layoutManager = CustomLinearLayoutManager(context)
                layoutManager.orientation = RecyclerView.VERTICAL
                setLayoutManager(layoutManager)
                adapter = this@ArticleReadFragment.adapter
            }
            articleReadFragmentRefreshLayout.apply {
                setColorSchemeResources(
                    android.R.color.holo_red_light,
                    android.R.color.holo_blue_light,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light
                )
                setOnRefreshListener {
                    viewModel.loadData(article)
                }
            }
        }
        viewModel.apply {
            observeNotNull(loadingState) {
                binding.articleReadFragmentRefreshLayout.isRefreshing = it
                binding.articleReadFragmentRecyclerView.adapter?.notifyDataSetChanged()
            }
            observeNotNull(errorMessage) {
                Toast.makeText(currentActivity, "Error : $it", Toast.LENGTH_SHORT).show()
            }
            observeNotNull(progressDialogState) {
                progressDialog?.dismiss()
            }
            observeNotNull(likeNumber) {
                binding.articleReadItemTextViewLike.text = it
            }
        }

        val window = currentActivity.window
        window.statusBarColor = ResourcesUtils.getColor(requireContext(), R.attr.article_header)

        // 取得Bundle
        viewModel.createDefaultHeader(
            article.title, article.owner, article.createTime, article.classX, boardName
        )
        viewModel.putDefaultHeader()
    }

    override fun onAnimOver() {
        viewModel.loadData(article)
    }

    private fun setRankMenu(view: View) {
        if (!(haveApi && useApi)) {
            return
        }
        val id = preferences.getString(PreferenceConstants.id, "")
        if (id!!.isEmpty()) {
            loadFragment(LoginPageFragment.newInstance(), currentFragment)
            return
        }
        val popupMenu = PopupMenu(currentActivity, view)
        popupMenu.menuInflater.inflate(R.menu.post_article_rank_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            var rank = when (item.itemId) {
                R.id.post_article_rank_like -> PostRankMark.Like
                R.id.post_article_rank_dislike -> PostRankMark.Dislike
                R.id.post_article_rank_non -> PostRankMark.None
                else -> PostRankMark.None
            }
            progressDialog = ProgressDialog.show(
                currentActivity,
                "",
                "Please wait."
            ).apply {
                window?.setBackgroundDrawableResource(R.drawable.dialog_background)
                viewModel.setRank(article, rank)
            }
            true
        }
        popupMenu.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        try {
            val inputMethodManager = currentActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(mainView.windowToken, 0)
        } catch (e: Exception) {
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.data.clear()
        val typedValue = TypedValue()
        val theme = currentActivity.theme
        theme.resolveAttribute(R.attr.black, typedValue, true)
        @ColorInt val color = typedValue.data
        val window = currentActivity.window
        window.statusBarColor = color
    }

    companion object {
        @JvmStatic
        fun newInstance(args: Bundle?): ArticleReadFragment {
            val fragment = ArticleReadFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
