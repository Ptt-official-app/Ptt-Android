package cc.ptt.android.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cc.ptt.android.R
import cc.ptt.android.presentation.base.BaseFragment
import cc.ptt.android.presentation.common.CustomLinearLayoutManager

class EmptyFragment : BaseFragment() {
    private var recyclerView: RecyclerView? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.empty_fragment_layout, container, false)
        setMainView(view)
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView_empty)
        val layoutManager = CustomLinearLayoutManager(context)
        layoutManager.orientation = RecyclerView.VERTICAL
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
            override fun getItemCount(): Int {
                return 0
            }

            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): RecyclerView.ViewHolder {
                return object : RecyclerView.ViewHolder(View(null)) {
                }
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            }
        }
        return view
    }

    override fun onAnimOver() {}

    companion object {
        @JvmStatic
        fun newInstance(): EmptyFragment {
            val args = Bundle()
            val fragment = EmptyFragment()
            fragment.arguments = args
            return fragment
        }

        @JvmStatic
        fun newInstance(args: Bundle?): EmptyFragment {
            val fragment = EmptyFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
