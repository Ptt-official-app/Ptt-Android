package tw.y_studio.ptt.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_setting.view.*
import tw.y_studio.ptt.R
import tw.y_studio.ptt.fragment.SettingFragment

class SettingAdapter(
    private val dataList: List<SettingFragment.SettingItem>,
    private var mOnItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<SettingAdapter.ViewHolder>() {

    class ViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView) {
        fun onBind(data: SettingFragment.SettingItem) {
            itemView.apply {
                textView.text = resources.getString(data.titleResId)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_setting, parent, false)).apply {
            itemView.setOnClickListener {
                mOnItemClickListener.onItemClick(it, dataList[adapterPosition])
            }
            itemView.textView2.visibility = View.GONE
        }
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.onBind(dataList[position])
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    interface OnItemClickListener {
        fun onItemClick(view: View, data: SettingFragment.SettingItem)
    }
}
