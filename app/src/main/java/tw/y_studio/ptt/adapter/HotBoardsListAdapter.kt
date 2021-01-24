package tw.y_studio.ptt.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import tw.y_studio.ptt.R
import tw.y_studio.ptt.model.HotBoardsItem
import tw.y_studio.ptt.ptt.PttColor

class HotBoardsListAdapter(
    private val data: List<HotBoardsItem>,
    private val onItemClickListener: OnItemClickListener,
) : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
    private val editMode = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_NORMAL -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.hot_boards_list_item, parent, false)
                ViewHolder(view)
            }
            TYPE_EDIT -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.hot_boards_list_item_edit, parent, false)
                ViewHolder(view)
            }
            else -> throw IllegalStateException("illegal view type: $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int = if (editMode) TYPE_EDIT else TYPE_NORMAL

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TYPE_NORMAL -> {
                (holder as? ViewHolder)?.apply {
                    textViewTitle.text = data[position].title
                    textViewSubtitle.text = data[position].subtitle
                    textViewOnlinePeople.text = data[position].online
                    person.setColorFilter(
                        PttColor.ColorTrans(
                            data[position].onlineColor
                        )
                    )
                    itemView.setOnClickListener { onItemClickListener.onItemClick(data[position]) }
                }
            }
            TYPE_EDIT -> {
                (holder as? ViewHolderEdit)?.apply {
                    textViewTitle.text = data[position].title
                    textViewSubtitle.text = data[position].subtitle
                    textViewOnlinePeople.text = data[position].online
                    itemView.setOnClickListener { onItemClickListener.onItemClick(data[position]) }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    private inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val textViewTitle: TextView = v.findViewById(R.id.textView_hot_boards_title)
        val textViewSubtitle: TextView = v.findViewById(R.id.textView_hot_boards_subtitle)
        val textViewOnlinePeople: TextView = v.findViewById(R.id.textView_hot_boards_online)
        val person: AppCompatImageButton = v.findViewById(R.id.hot_boards_online_imageButton_person)
    }

    private inner class ViewHolderEdit(v: View) : RecyclerView.ViewHolder(v) {
        val textViewTitle: TextView = v.findViewById(R.id.textView_hot_boards_title)
        val textViewSubtitle: TextView = v.findViewById(R.id.textView_hot_boards_subtitle)
        val textViewOnlinePeople: TextView = v.findViewById(R.id.textView_hot_boards_online)
    }

    // define interface
    interface OnItemClickListener {
        fun onItemClick(item: HotBoardsItem)
    }

    companion object {
        private const val TYPE_NORMAL = 0
        private const val TYPE_EDIT = 1
    }
}
