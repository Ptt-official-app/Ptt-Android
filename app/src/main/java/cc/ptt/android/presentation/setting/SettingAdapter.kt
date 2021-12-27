package cc.ptt.android.presentation.setting

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cc.ptt.android.databinding.ItemSettingBinding

class SettingAdapter(
    private val dataList: List<SettingFragment.SettingItem>,
    private var mOnItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<SettingAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemSettingBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: SettingFragment.SettingItem) {
            binding.apply {
                textView.text = itemView.resources.getString(data.titleResId)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemSettingBinding.inflate(LayoutInflater.from(parent.context), parent, false)).apply {
            itemView.setOnClickListener {
                mOnItemClickListener.onItemClick(it, dataList[adapterPosition])
            }
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
