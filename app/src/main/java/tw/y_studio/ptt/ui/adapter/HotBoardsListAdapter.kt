package tw.y_studio.ptt.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import tw.y_studio.ptt.R
import tw.y_studio.ptt.network.ptt.PttColor
import tw.y_studio.ptt.utils.StringUtils.notNullString

class HotBoardsListAdapter(
    private val data: List<Map<String, Any>>
) : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {

    private var mOnItemClickListener: OnItemClickListener? = null
    private var mOnItemLongClickListener: OnItemLongClickListener? = null
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
                    textViewTitle.text = notNullString(data[position]["title"])
                    textViewSubtitle.text = notNullString(data[position]["subtitle"])
                    textViewOnlinePeople.text = notNullString(data[position]["online"])
                    person.setColorFilter(
                        PttColor.ColorTrans(
                            notNullString(data[position]["onlineColor"])
                        )
                    )
                    itemView.setOnClickListener { mOnItemClickListener?.onItemClick(it, adapterPosition) }
                    itemView.setOnLongClickListener {
                        mOnItemLongClickListener?.onItemClick(it, adapterPosition)
                        true
                    }
                }
            }
            TYPE_EDIT -> {
                (holder as? ViewHolderEdit)?.apply {
                    textViewTitle.text = notNullString(data[position]["title"])
                    textViewSubtitle.text = notNullString(data[position]["subtitle"])
                    textViewOnlinePeople.text = notNullString(data[position]["online"])
                    itemView.setOnClickListener { mOnItemClickListener?.onItemClick(it, adapterPosition) }
                    itemView.setOnLongClickListener {
                        mOnItemLongClickListener?.onItemClick(it, adapterPosition)
                        true
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mOnItemClickListener = listener
    }

    fun setOnItemLongClickListener(listener: OnItemLongClickListener?) {
        mOnItemLongClickListener = listener
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
        fun onItemClick(view: View?, position: Int)
    }

    interface OnItemLongClickListener {
        fun onItemClick(view: View?, position: Int)
    }

    companion object {
        private const val TYPE_NORMAL = 0
        private const val TYPE_EDIT = 1
    }
}
