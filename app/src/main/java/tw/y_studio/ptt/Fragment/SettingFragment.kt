package tw.y_studio.ptt.Fragment

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.hot_article_list_fragment_layout.*
import tw.y_studio.ptt.Adapter.SettingAdapter
import tw.y_studio.ptt.R
import tw.y_studio.ptt.UI.BaseFragment
import tw.y_studio.ptt.UI.ClickFix
import tw.y_studio.ptt.UI.CustomLinearLayoutManager
import tw.y_studio.ptt.Utils.turnOnUrl

class SettingFragment : BaseFragment() {
    private val dataList = mutableListOf<SettingItem>()
    private val mClickFix = ClickFix()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    private fun createAdapter(): SettingAdapter {
        return SettingAdapter(
            dataList,
            object : SettingAdapter.OnItemClickListener {
                override fun onItemClick(view: View, data: SettingItem) {
                    if (mClickFix.isFastDoubleClick) return
                    when (data) {
                        SettingItem.PttId -> {
                            loadFragment(LoginPageFragment.newInstance(), currentFragment)
                        }
                        SettingItem.Policy -> {
                            turnOnUrl(context!!, "https://www.ptt.cc/index.ua.html")
                        }
                        else -> showSingleChoiceDialog(data)
                    }
                }
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.apply {
            article_list_fragment_recycler_view.apply {
                val layoutManager = CustomLinearLayoutManager(context)
                layoutManager.orientation = RecyclerView.VERTICAL
                setHasFixedSize(true)
                setLayoutManager(layoutManager)
                adapter = createAdapter()
            }
            article_list_fragment_refresh_layout.apply {
                setColorSchemeResources(
                    android.R.color.holo_red_light,
                    android.R.color.holo_blue_light,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light
                )
                setOnRefreshListener {
                    isRefreshing = false
                }
            }
        }
    }

    private fun showSingleChoiceDialog(data: SettingItem) {
        val context = context ?: return
        val preference = currentActivity.getSharedPreferences("MainSetting", Context.MODE_PRIVATE)
        AlertDialog.Builder(context).apply {
            setTitle(data.titleResId)
            setSingleChoiceItems(data.valueArrayKey, preference.getInt(data.key, 0)) { dialog, which ->
                dialog.dismiss()
                val editor = preference.edit()
                editor.putInt(data.key, which)
                editor.apply()
                data.onChoice(which)
            }
            setPositiveButton(R.string.cancel_button) { dialog, which ->
                dialog.dismiss()
            }
        }.create().run {
            window?.setBackgroundDrawableResource(R.drawable.dialog_background)
            show()
            getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context, R.color.colorAccent))
        }
    }

    override fun onAnimOver() {
        loadData()
    }

    private fun loadData() {
        dataList.clear()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            dataList.add(SettingItem.Theme)
        }
        dataList.add(SettingItem.SearchStyle)
        dataList.add(SettingItem.PostBottomStyle)
        dataList.add(SettingItem.Policy)
        dataList.add(SettingItem.PttId)
        view?.run {
            article_list_fragment_recycler_view?.adapter?.notifyDataSetChanged()
        }
    }

    enum class SettingItem(
        val titleResId: Int,
        val key: String = "",
        val valueArrayKey: Int = 0,
        val onChoice: (which: Int) -> Unit = {}
    ) {
        Theme(
            R.string.setting_theme,
            "THEME",
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) R.array.setting_theme_array else R.array.setting_theme_array_low_p,
            { which ->
                when (which) {
                    1 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    0 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }
            }
        ),
        SearchStyle(R.string.setting_search_item_style, "SEARCHSTYLE", R.array.setting_search_item_style_array),
        PostBottomStyle(R.string.setting_post_bottom_style, "POSTBOTTOMSTYLE", R.array.setting_post_bottom_style_array),
        Policy(R.string.ptt_policy),
        PttId(R.string.set_ptt_id, "APIPTTID");
    }

    companion object {
        fun newInstance(): SettingFragment {
            val args = Bundle()
            val fragment = SettingFragment()
            fragment.arguments = args
            return fragment
        }

        @JvmStatic
        fun newInstance(args: Bundle?): SettingFragment {
            val fragment = SettingFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
