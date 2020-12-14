package tw.y_studio.ptt.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import tw.y_studio.ptt.R
import tw.y_studio.ptt.ptt.PttColor
import tw.y_studio.ptt.ui.dragitemmove.ItemMoveCallback.ItemTouchHelperContract
import tw.y_studio.ptt.ui.dragitemmove.StartDragListener
import tw.y_studio.ptt.utils.StringUtils.notNullString
import java.util.*

class FavoriteBoardsListAdapter(
    private val data: List<Map<String, Any>?>,
    private val mStartDragListener: StartDragListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), ItemTouchHelperContract {
    private var mOnItemClickListener: OnItemClickListener? = null
    private var mOnItemLongClickListener: OnItemLongClickListener? = null
    private var dislikeOnClickListener: View.OnClickListener? = null
    private var editMode = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_NORMAL -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.hot_boards_list_item, parent, false)
                ViewHolder(view)
            }
            TYPE_EDIT -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.hot_boards_list_item_edit, parent, false)
                ViewHolderEdit(view)
            }
            else -> throw IllegalStateException("illegal view type: $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int = if (editMode) TYPE_EDIT else TYPE_NORMAL

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TYPE_NORMAL -> {
                (holder as? ViewHolder)?.apply {
                    textViewTitle.text = notNullString(data[position]?.get("title"))
                    textViewSubtitle.text = notNullString(data[position]?.get("subtitle"))
                    textViewOnlinePeople.text = notNullString(data[position]?.get("online"))
                    person.setColorFilter(
                        PttColor.ColorTrans(
                            notNullString(data[position]?.get("onlineColor"))
                        )
                    )
                    itemView.setOnClickListener { mOnItemClickListener?.onItemClick(it, adapterPosition) }
                    holder.itemView.setOnLongClickListener {
                        mOnItemLongClickListener?.onItemClick(it, adapterPosition)
                        true
                    }
                }
            }
            TYPE_EDIT -> {
                (holder as? ViewHolderEdit)?.apply {
                    textViewTitle.text = notNullString(data[position]?.get("title"))
                    textViewSubtitle.text = notNullString(data[position]?.get("subtitle"))
                    drag.setOnTouchListener { _: View?, event: MotionEvent ->
                        if (event.action == MotionEvent.ACTION_DOWN) {
                            mStartDragListener.requestDrag(holder)
                        }
                        false
                    }
                    unfav.tag = notNullString(data[position]?.get("title"))
                    unfav.setOnClickListener(dislikeOnClickListener)
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

    override fun onRowMoved(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (x in fromPosition until toPosition) {
                Collections.swap(data, x, x + 1)
            }
        } else {
            for (x in fromPosition downTo toPosition + 1) {
                Collections.swap(data, x, x - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onRowSelected(viewHolder: RecyclerView.ViewHolder?) {
    }

    override fun onRowClear(viewHolder: RecyclerView.ViewHolder?) {
    }

    fun setDislikeOnClickListener(dislikeOnClickListener: View.OnClickListener?) {
        this.dislikeOnClickListener = dislikeOnClickListener
    }

    fun setEditMode(enable: Boolean) {
        editMode = enable
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

    inner class ViewHolderEdit(v: View) : RecyclerView.ViewHolder(v) {
        val textViewTitle: TextView = v.findViewById(R.id.textView_hot_boards_title)
        val textViewSubtitle: TextView = v.findViewById(R.id.textView_hot_boards_subtitle)
        var unfav: AppCompatImageButton = v.findViewById(R.id.hot_boards_online_imageButton_unfav)
        val drag: AppCompatImageButton = v.findViewById(R.id.hot_boards_online_imageButton_drag)
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
