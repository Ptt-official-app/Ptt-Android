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
import tw.y_studio.ptt.databinding.ArticleReadFragmentLayoutBinding
import tw.y_studio.ptt.fragment.login.LoginPageFragment
import tw.y_studio.ptt.ptt.AidConverter
import tw.y_studio.ptt.ui.BaseFragment
import tw.y_studio.ptt.ui.CustomLinearLayoutManager
import tw.y_studio.ptt.utils.ResourcesUtils
import tw.y_studio.ptt.utils.observeNotNull
import tw.y_studio.ptt.utils.shareTo
import tw.y_studio.ptt.utils.useApi
import java.util.regex.Pattern

class ArticleReadFragment : BaseFragment() {
    private var _binding: ArticleReadFragmentLayoutBinding? = null
    private val binding get() = _binding!!
    private val urlPattern = Pattern.compile("www.ptt.cc/bbs/([-a-zA-Z0-9_]{2,})/([M|G].[-a-zA-Z0-9._]{1,30}).htm")
    private var adapter: ArticleReadAdapter? = null
    private var fileName = ""
    private var board = ""
    private var aid = ""
    private var articleTitle = ""
    private var articleBoard = ""
    private var articleAuth = " "
    private var articleTime = ""
    private var articleClass = ""
    private var orgUrl = ""

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
                    viewModel.originalArticleTitle,
                    """
                                ${viewModel.originalArticleTitle}
                                $orgUrl
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
                    viewModel.loadData(board, fileName, aid, articleBoard)
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

        val bundle = arguments // 取得Bundle
        orgUrl = bundle!!.getString("url", "")
        val matcher = urlPattern.matcher(orgUrl)
        if (matcher.find()) {
            board = matcher.group(1)
            fileName = matcher.group(2)
            aid = AidConverter.urlToAid(orgUrl).aid
        }
        articleBoard = bundle.getString("board", "")
        articleTitle = bundle.getString("title", "")
        articleAuth = bundle.getString("auth", "")
        articleClass = bundle.getString("class", "")
        articleTime = bundle.getString("date", "")
        viewModel.createDefaultHeader(articleTitle, articleAuth, articleTime, articleClass, articleBoard)
        viewModel.putDefaultHeader()
    }

    override fun onAnimOver() {
        viewModel.loadData(board, fileName, aid, articleBoard)
    }

    private fun setRankMenu(view: View) {
        if (!(haveApi && useApi)) {
            return
        }
        val id = preferences.getString("APIPTTID", "")
        if (id!!.isEmpty()) {
            loadFragment(LoginPageFragment.newInstance(), currentFragment)
            return
        }
        val popupMenu = PopupMenu(currentActivity, view)
        popupMenu.menuInflater.inflate(R.menu.post_article_rank_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            var rank = PostRankMark.None
            when (item.itemId) {
                R.id.post_article_rank_like -> rank = PostRankMark.Like
                R.id.post_article_rank_dislike -> rank = PostRankMark.Dislike
                R.id.post_article_rank_non -> rank = PostRankMark.None
            }
            progressDialog = ProgressDialog.show(
                currentActivity,
                "",
                "Please wait."
            ).apply {
                window?.setBackgroundDrawableResource(R.drawable.dialog_background)
                viewModel.setRank(board, aid, orgUrl, rank)
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
        fun newInstance(): ArticleReadFragment {
            val args = Bundle()
            val fragment = ArticleReadFragment()
            fragment.arguments = args
            return fragment
        }

        @JvmStatic
        fun newInstance(args: Bundle?): ArticleReadFragment {
            val fragment = ArticleReadFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
