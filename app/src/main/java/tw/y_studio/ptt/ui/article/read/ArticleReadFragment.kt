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
import tw.y_studio.ptt.api.PostAPIHelper
import tw.y_studio.ptt.api.SetPostRankAPIHelper
import tw.y_studio.ptt.api.SetPostRankAPIHelper.iRank
import tw.y_studio.ptt.databinding.ArticleReadFragmentLayoutBinding
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

    private var mAdapter: ArticleReadAdapter? = null
    private val data: MutableList<MutableMap<String, Any>>? = ArrayList()
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
                mAdapter = ArticleReadAdapter(currentActivity, data!!)
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
    private val data_temp: MutableList<MutableMap<String, Any>> = ArrayList()
    private var pushCount = 0
    private val gettedUrl = false
    private var postAPI: PostAPIHelper? = null
    private var getPostRankAPI: GetPostRankAPIHelper? = null

    // mAdapter.notifyDataSetChanged();
    private val dataFromApi: Unit
        private get() {
            if (postAPI == null) {
                val p = Pattern.compile(
                    "www.ptt.cc/bbs/([-a-zA-Z0-9_]{2,})/([M|G].[-a-zA-Z0-9._]{1,30}).htm"
                )
                val m = p.matcher(orgUrl)
                if (m.find()) {
                    postAPI = PostAPIHelper(context, m.group(1), m.group(2))
                } else {
                    Log("onAR", "not match")
                }
            }
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
                data_temp.clear()
                Log("onAR", "get data from web start")
                try {
                    postAPI!!.get()
                    getPostRankAPI!!.get()
                    pushCount = getPostRankAPI!!.like
                    floorNum = postAPI!!.floorNum
                    originalArticleTitle = postAPI!!.title
                    if (true) {
                        val item: MutableMap<String, Any> = HashMap()
                        item["type"] = "header"
                        item["title"] = postAPI!!.title
                        item["auth"] = (
                            postAPI!!.auth +
                                " (" +
                                postAPI!!.auth_nickName +
                                ")"
                            )
                        item["date"] = postAPI!!.date
                        item["class"] = postAPI!!.classString
                        item["board"] = postAPI!!.board
                        data_temp.add(0, item)
                    }
                    val contents = postAPI!!.content.split("\r\n".toRegex()).toTypedArray()
                    var contentTemp = StringBuilder()
                    for (i in contents.indices) {
                        val cmd = contents[i]
                        val urlM = StringUtils.UrlPattern.matcher(cmd)
                        if (urlM.find()) {
                            if (true) {
                                val item: MutableMap<String, Any> = HashMap()
                                item["type"] = "content"
                                item["text"] = contentTemp.toString()
                                data_temp.add(item)
                            }
                            contentTemp = StringBuilder()
                            if (true) {
                                val item: MutableMap<String, Any> = HashMap()
                                item["type"] = "content"
                                item["text"] = cmd
                                data_temp.add(item)
                            }
                            val imageUrl = StringUtils.getImgUrl(cmd)
                            for (urlString in imageUrl) {
                                if (true) {
                                    val item: MutableMap<String, Any> = HashMap()
                                    item["type"] = "content_image"
                                    item["url"] = urlString
                                    item["index"] = -1
                                    data_temp.add(item)
                                }
                            }
                        } else {
                            contentTemp.append(cmd)
                            if (i < contents.size - 1) {
                                contentTemp.append("\n")
                            }
                        }
                    }
                    if (contentTemp.toString().length > 0) {
                        val item: MutableMap<String, Any> = HashMap()
                        item["type"] = "content"
                        item["text"] = contentTemp.toString()
                        data_temp.add(item)
                        contentTemp = StringBuilder()
                    }
                    if (true) {
                        val item: MutableMap<String, Any> = HashMap()
                        item["type"] = "center_bar"
                        item["like"] = pushCount.toString() + ""
                        item["floor"] = floorNum.toString() + ""
                        data_temp.add(item)
                    }
                    data_temp.addAll(postAPI!!.pushData)
                    runOnUI {
                        data!!.clear()
                        // mAdapter.notifyDataSetChanged();
                        data.addAll(data_temp)
                        mAdapter!!.notifyDataSetChanged()
                        data_temp.clear()
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
        val item: MutableMap<String, Any> = HashMap()
        item["type"] = "header"
        item["title"] = articleTitle
        item["auth"] = articleAuth
        item["date"] = articleTime
        item["class"] = articleClass
        item["board"] = articleBoard
        data!!.add(item)
    }

    private var GattingData = false
    private fun loadData() {
        if (GattingData) return
        data!!.clear()
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
            var rank = iRank.non
            when (item.itemId) {
                R.id.post_article_rank_like -> rank = iRank.like
                R.id.post_article_rank_dislike -> rank = iRank.dislike
                R.id.post_article_rank_non -> rank = iRank.non
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
                var center_bar_index = -1
                for (i in data!!.indices) {
                    if (StringUtils.notNullString(data[i]["type"])
                        .equals("center_bar", ignoreCase = true)
                    ) {
                        center_bar_index = i
                        break
                    }
                }
                if (center_bar_index != -1) {
                    val item = data[center_bar_index]
                    item["like"] = pushCount.toString() + ""
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
    private fun setRank(rank_: iRank) {
        mDialog = ProgressDialog.show(currentActivity, "", "Please wait.").apply {
            getWindow()!!.setBackgroundDrawableResource(R.drawable.dialog_background)
            object : Thread() {
                override fun run() {
                    try {
                        val setPostRankAPI: SetPostRankAPIHelper
                        val p = Pattern.compile(
                            "www.ptt.cc/bbs/([-a-zA-Z0-9_]{2,})/([M|G].[-a-zA-Z0-9._]{1,30}).htm"
                        )
                        val m = p.matcher(orgUrl)
                        setPostRankAPI = if (m.find()) {
                            val aid = AidConverter.urlToAid(orgUrl)
                            SetPostRankAPIHelper(
                                context, aid.boardTitle, aid.aid
                            )
                        } else {
                            throw Exception("error")
                            // DebugUtils.Log("onAR", "not match");
                        }
                        val id = currentActivity
                            .getSharedPreferences("MainSetting", Context.MODE_PRIVATE)
                            .getString("APIPTTID", "")
                        if (id!!.length == 0) {
                            throw Exception("No Ptt id")
                        }
                        setPostRankAPI[id, rank_]
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
        if (postAPI != null) {
            postAPI!!.close()
        }
        data?.clear()
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
