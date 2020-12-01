package tw.y_studio.ptt.ui.article.read

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.ColorInt
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import tw.y_studio.ptt.R
import tw.y_studio.ptt.api.GetPostRankAPIHelper
import tw.y_studio.ptt.api.PostRankAPI
import tw.y_studio.ptt.api.PostRankAPI.PostRank
import tw.y_studio.ptt.databinding.ArticleReadFragmentLayoutBinding
import tw.y_studio.ptt.di.Injection
import tw.y_studio.ptt.fragment.LoginPageFragment
import tw.y_studio.ptt.ptt.AidConverter
import tw.y_studio.ptt.ui.BaseFragment
import tw.y_studio.ptt.ui.CustomLinearLayoutManager
import tw.y_studio.ptt.utils.*
import java.util.*
import java.util.regex.Pattern

class ArticleReadFragment : BaseFragment() {
    private var _binding: ArticleReadFragmentLayoutBinding? = null
    private val binding get() = _binding!!
    private val urlPattern = Pattern.compile("www.ptt.cc/bbs/([-a-zA-Z0-9_]{2,})/([M|G].[-a-zA-Z0-9._]{1,30}).htm")
    private var mAdapter: ArticleReadAdapter? = null
    private val data: MutableList<ArticleReadAdapter.Item> = ArrayList()
    private var fileName = ""
    private var board = ""
    private var articleTitle = ""
    private var articleBoard = ""
    private var articleAuth = " "
    private var articleTime = ""
    private var articleClass = ""
    private var originalArticleTitle = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
                    originalArticleTitle,
                    """
                                $originalArticleTitle
                                $orgUrl
                    """.trimIndent(),
                    "分享文章"
                )
            }
            articleReadFragmentRecyclerView.apply {
                mAdapter = ArticleReadAdapter(data)
                setHasFixedSize(true)
                val layoutManager = CustomLinearLayoutManager(context)
                layoutManager.orientation = RecyclerView.VERTICAL
                setLayoutManager(layoutManager)
                adapter = mAdapter
            }
            articleReadFragmentRefreshLayout.apply {
                setColorSchemeResources(
                    android.R.color.holo_red_light,
                    android.R.color.holo_blue_light,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light
                )
                setOnRefreshListener {
                    loadData()
                }
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
        }
        articleBoard = bundle.getString("board", "")
        articleTitle = bundle.getString("title", "")
        articleAuth = bundle.getString("auth", "")
        articleClass = bundle.getString("class", "")
        articleTime = bundle.getString("date", "")
        putDefaultHeader()
    }

    override fun onAnimOver() {
        loadData()
    }

    private var mThreadHandler: Handler? = null
    private var mThread: HandlerThread? = null
    private lateinit var r1: Runnable
    private var orgUrl = ""
    private var floorNum = 0
    private val dataTemp = mutableListOf<ArticleReadAdapter.Item>()
    private var pushCount = 0
    private val gettedUrl = false
    private val postRemoteDataSource = Injection.RemoteDataSource.postRemoteDataSource
    private var getPostRankAPI: GetPostRankAPIHelper? = null
    private val postRankRemoteDataSource = Injection.RemoteDataSource.postRankRemoteDataSource

    // mAdapter.notifyDataSetChanged();
    private val dataFromApi: Unit
        private get() {
            if (getPostRankAPI == null) {
                val p = Pattern.compile(
                    "www.ptt.cc/bbs/([-a-zA-Z0-9_]{2,})/([M|G].[-a-zA-Z0-9._]{1,30}).htm"
                )
                val m = p.matcher(orgUrl)
                if (m.find()) {
                    val aid = AidConverter.urlToAid(orgUrl)
                    getPostRankAPI = GetPostRankAPIHelper(context, aid.boardTitle, aid.aid)
                } else {
                    Log("onAR", "not match")
                }
            }
            r1 = Runnable {
                runOnUI { binding.articleReadFragmentRefreshLayout.isRefreshing = true }
                GattingData = true
                Log("onAR", "get data from web start")
                try {
                    val post = postRemoteDataSource.getPostData(board, fileName)
                    getPostRankAPI!!.get()
                    pushCount = getPostRankAPI!!.like
                    floorNum = post.comments.size
                    originalArticleTitle = post.title

                    dataTemp.add(
                        ArticleReadAdapter.Item.HeaderItem(
                            post.title,
                            "${post.auth} (${post.authNickName})",
                            post.date,
                            post.classString,
                            articleBoard
                        )
                    )

                    val contents = post.content.split("\r\n".toRegex()).toTypedArray()
                    var contentTemp = StringBuilder()
                    for (i in contents.indices) {
                        val cmd = contents[i]
                        val urlM = StringUtils.UrlPattern.matcher(cmd)
                        if (urlM.find()) {
                            dataTemp.add(ArticleReadAdapter.Item.ContentLineItem(contentTemp.toString()))
                            contentTemp = StringBuilder()
                            dataTemp.add(ArticleReadAdapter.Item.ContentLineItem(cmd))
                            val imageUrl = StringUtils.getImgUrl(cmd)
                            for (urlString in imageUrl) {
                                dataTemp.add(ArticleReadAdapter.Item.ImageItem(-2, urlString))
                            }
                        } else {
                            contentTemp.append(cmd)
                            if (i < contents.size - 1) {
                                contentTemp.append("\n")
                            }
                        }
                    }
                    if (contentTemp.toString().isNotEmpty()) {
                        dataTemp.add(ArticleReadAdapter.Item.ContentLineItem(contentTemp.toString()))
                    }
                    dataTemp.add(ArticleReadAdapter.Item.CenterBarItem(pushCount.toString(), floorNum.toString()))
                    post.comments.forEachIndexed { index, comment ->
                        dataTemp.add(ArticleReadAdapter.Item.CommentItem(index, comment.content, comment.userid))
                        val imageUrl: List<String> = StringUtils.getImgUrl(StringUtils.notNullString(comment.content))
                        for (urlString in imageUrl) {
                            dataTemp.add(ArticleReadAdapter.Item.ImageItem(index, urlString))
                        }
                        dataTemp.add(
                            ArticleReadAdapter.Item.CommentBarItem(
                                index,
                                comment.date,
                                "${index + 1}F",
                                "0"
                            )
                        )
                    }

                    runOnUI {
                        data.clear()
                        data.addAll(dataTemp)
                        mAdapter!!.notifyDataSetChanged()
                        binding.articleReadFragmentRefreshLayout.isRefreshing = false
                        binding.articleReadItemTextViewLike.text = pushCount.toString() + ""
                    }
                    Log("onAL", "get data from web over")
                } catch (e: Exception) {
                    Log("onAL", "Error : $e")
                    runOnUI {
                        Toast.makeText(
                            activity,
                            "Error : $e",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        binding.articleReadFragmentRefreshLayout.isRefreshing = false
                    }
                }
                GattingData = false
            }
            mThread = HandlerThread("name")
            mThread!!.start()
            mThreadHandler = Handler(mThread!!.looper)
            mThreadHandler!!.post(r1)
        }
    private val haveApi = true
    private fun putDefaultHeader() {
        data.add(
            ArticleReadAdapter.Item.HeaderItem(
                articleTitle,
                articleAuth,
                articleTime,
                articleClass,
                articleBoard
            )
        )
    }

    private var GattingData = false
    private fun loadData() {
        if (GattingData) return
        data.clear()
        putDefaultHeader()
        mAdapter!!.notifyDataSetChanged()
        dataFromApi
    }

    private fun setRankMenu(view: View) {
        if (!(haveApi && useApi)) {
            return
        }
        val id = currentActivity
            .getSharedPreferences("MainSetting", Context.MODE_PRIVATE)
            .getString("APIPTTID", "")
        if (id!!.isEmpty()) {
            loadFragment(LoginPageFragment.newInstance(), currentFragment)
            return
        }
        val popupMenu = PopupMenu(currentActivity, view)
        popupMenu.menuInflater.inflate(R.menu.post_article_rank_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            var rank = PostRank.None
            when (item.itemId) {
                R.id.post_article_rank_like -> rank = PostRank.Like
                R.id.post_article_rank_dislike -> rank = PostRank.Dislike
                R.id.post_article_rank_non -> rank = PostRank.None
            }
            setRank(rank)
            true
        }
        popupMenu.show()
    }

    private fun rehreshRank() {
        if (getPostRankAPI == null) {
            val p = Pattern.compile(
                "www.ptt.cc/bbs/([-a-zA-Z0-9_]{2,})/([M|G].[-a-zA-Z0-9._]{1,30}).htm"
            )
            val m = p.matcher(orgUrl)
            if (m.find()) {
                val aid = AidConverter.urlToAid(orgUrl)
                getPostRankAPI = GetPostRankAPIHelper(context, aid.boardTitle, aid.aid)
            } else {
                Log("onAR", "not match")
            }
        }
        r1 = Runnable {
            runOnUI { binding.articleReadFragmentRefreshLayout.isRefreshing = true }
            GattingData = true
            try {
                getPostRankAPI!!.get()
                pushCount = getPostRankAPI!!.like
                for (i in data.indices) {
                    val item = data[i]
                    if (item !is ArticleReadAdapter.Item.CenterBarItem) continue
                    data[i] = ArticleReadAdapter.Item.CenterBarItem(pushCount.toString(), item.floor)
                    break
                }
                runOnUI {
                    binding.articleReadFragmentRefreshLayout.isRefreshing = false
                    mAdapter!!.notifyDataSetChanged()
                    binding.articleReadItemTextViewLike.text = pushCount.toString() + ""
                }
                Log("onAL", "get data from web over")
            } catch (e: Exception) {
                Log("onAL", "Error : $e")
                runOnUI {
                    Toast.makeText(
                        currentActivity,
                        "Error : $e",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    binding.articleReadFragmentRefreshLayout.isRefreshing = false
                }
            }
            GattingData = false
        }
        mThread = HandlerThread("name")
        mThread!!.start()
        mThreadHandler = Handler(mThread!!.looper)
        mThreadHandler!!.post(r1)
    }

    private var mDialog: ProgressDialog? = null
    private fun setRank(rank: PostRank) {
        mDialog = ProgressDialog.show(currentActivity, "", "Please wait.").apply {
            getWindow()!!.setBackgroundDrawableResource(R.drawable.dialog_background)
            object : Thread() {
                override fun run() {
                    try {
                        val setPostRankAPI: PostRankAPI
                        val id = currentActivity
                            .getSharedPreferences("MainSetting", Context.MODE_PRIVATE)
                            .getString("APIPTTID", "")
                        if (id.isNullOrEmpty()) {
                            throw Exception("No Ptt id")
                        }
                        val p = Pattern.compile(
                            "www.ptt.cc/bbs/([-a-zA-Z0-9_]{2,})/([M|G].[-a-zA-Z0-9._]{1,30}).htm"
                        )
                        val m = p.matcher(orgUrl)
                        if (m.find()) {
                            val aid = AidConverter.urlToAid(orgUrl)
                            postRankRemoteDataSource.setPostRank(
                                aid.boardTitle,
                                aid.aid,
                                id,
                                rank
                            )
                        } else {
                            throw Exception("error")
                        }
                        runOnUI {
                            dismiss()
                            rehreshRank()
                        }
                    } catch (e: Exception) {
                        runOnUI {
                            dismiss()
                            Toast.makeText(
                                currentActivity,
                                "Error : $e",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }
                }
            }.start()
        }
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
        postRemoteDataSource.disposeAll()
        data.clear()
        // 移除工作
        if (mThreadHandler != null) {
            mThreadHandler!!.removeCallbacks(r1!!)
        }
        // (關閉Thread)
        if (mThread != null) {
            mThread!!.quit()
        }
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
