package tw.y_studio.ptt.ui.setting

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import tw.y_studio.ptt.R
import tw.y_studio.ptt.databinding.FragmentSettingBinding
import tw.y_studio.ptt.fragment.login.LoginPageFragment
import tw.y_studio.ptt.ui.BaseFragment
import tw.y_studio.ptt.ui.ClickFix
import tw.y_studio.ptt.ui.CustomLinearLayoutManager
import tw.y_studio.ptt.utils.PreferenceConstants
import tw.y_studio.ptt.utils.turnOnUrl

class SettingFragment : BaseFragment() {
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    private val dataList = mutableListOf<SettingItem>()
    private val mClickFix = ClickFix()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentSettingBinding.inflate(inflater, container, false).apply {
            _binding = this
        }.root
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
                        SettingItem.ApiDomain -> {
                            showEditTextDialog(data)
                        }
                        else -> showSingleChoiceDialog(data)
                    }
                }
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            articleListFragmentRecyclerView.apply {
                val layoutManager = CustomLinearLayoutManager(context)
                layoutManager.orientation = RecyclerView.VERTICAL
                setHasFixedSize(true)
                setLayoutManager(layoutManager)
                adapter = createAdapter()
            }
            articleListFragmentRefreshLayout.apply {
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

    private fun showEditTextDialog(data: SettingItem) {
        val context = context ?: return
        val preference = currentActivity.getSharedPreferences(PreferenceConstants.prefName, Context.MODE_PRIVATE)
        AlertDialog.Builder(context).apply {
            setTitle(data.titleResId)
            val editText = AppCompatEditText(context).apply {
                setText(preference.getString(data.key, ""))
            }
            setView(editText)
            setNegativeButton(R.string.cancel_button) { dialog, which ->
                dialog.dismiss()
            }
            setPositiveButton(R.string.save_button) { dialog, which ->
                dialog.dismiss()
                val editor = preference.edit()
                with(editText.text?.toString() ?: "") {
                    when (this.isNullOrEmpty()) {
                        true -> {
                            editor.remove(data.key)
                        }
                        false -> {
                            editor.putString(data.key, this)
                        }
                    }
                }
                editor.apply()
            }
        }.create().run {
            window?.setBackgroundDrawableResource(R.drawable.dialog_background)
            show()
            getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(context, R.color.colorAccent))
            getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context, R.color.colorAccent))
        }
    }

    private fun showSingleChoiceDialog(data: SettingItem) {
        val context = context ?: return
        val preference = currentActivity.getSharedPreferences(PreferenceConstants.prefName, Context.MODE_PRIVATE)
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
        dataList.add(SettingItem.ApiDomain)
        dataList.add(SettingItem.PttId)
        binding.run {
            articleListFragmentRecyclerView?.adapter?.notifyDataSetChanged()
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
        PttId(R.string.set_ptt_id, "APIPTTID"),
        ApiDomain(R.string.set_api_domain, PreferenceConstants.apiDomain);
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
