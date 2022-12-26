package cc.ptt.android.home.personalpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cc.ptt.android.R
import cc.ptt.android.base.BaseFragment
import cc.ptt.android.common.CustomLinearLayoutManager

class PersonInfoFragment : BaseFragment() {
    private var recyclerView: RecyclerView? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.personal_info_fragment_layout, container, false)
        recyclerView = findViewById<RecyclerView>(R.id.persion_info_fragment_recyclerView)
        val bundle = arguments // 取得Bundle
        val title_ = bundle!!.getString("Title")
        val layoutManager = CustomLinearLayoutManager(context)
        layoutManager.orientation = RecyclerView.VERTICAL
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = layoutManager
        return view
    }

    override fun onAnimOver() {}

    companion object {
        fun newInstance(): PersonInfoFragment {
            val args = Bundle()
            val fragment = PersonInfoFragment()
            fragment.arguments = args
            return fragment
        }

        fun newInstance(args: Bundle?): PersonInfoFragment {
            val fragment = PersonInfoFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
