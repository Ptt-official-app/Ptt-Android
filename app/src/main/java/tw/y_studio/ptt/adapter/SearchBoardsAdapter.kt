package tw.y_studio.ptt.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import tw.y_studio.ptt.R
import tw.y_studio.ptt.utils.StringUtils.TextViewAutoSplitFix
import tw.y_studio.ptt.utils.StringUtils.notNullString

class SearchBoardsAdapter(private val context: Context, private val data: List<Map<String, Any>>) : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var likeOnClickListener: View.OnClickListener? = null
    private var mOnItemClickListener: OnItemClickListener? = null
    private var mOnItemLongClickListener: OnItemLongClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE0 -> {
                val view = inflater.inflate(R.layout.search_boards_item, parent, false)
                ViewHolder(view)
            }
            TYPE1 -> {
                val view = inflater.inflate(R.layout.search_boards_item_2, parent, false)
                ViewHolder2(view)
            }
            else -> throw IllegalStateException("illegal view type: $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return context.getSharedPreferences("MainSetting", Context.MODE_PRIVATE).getInt("SEARCHSTYLE", 0)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TYPE0 -> {
                (holder as? ViewHolder)?.apply {
                    textViewTitle.text = notNullString(data[position]["title"])
                    TextViewAutoSplitFix(holder.textViewTitle)
                    like.tag = position
                    (data[position]["like"] as? Boolean)?.let {
                        if (it) {
                            like.setColorFilter(context.resources.getColor(R.color.tangerine, context.theme))
                        } else {
                            like.setColorFilter(context.resources.getColor(R.color.slateGrey, context.theme))
                        }
                    }

                    like.setOnClickListener(likeOnClickListener)
                    itemView.setOnClickListener { v: View? -> mOnItemClickListener?.onItemClick(v, holder.adapterPosition) }
                    itemView.setOnLongClickListener { v: View? ->
                        mOnItemLongClickListener?.onItemClick(
                            v, holder.adapterPosition
                        )
                        true
                    }
                }
            }
            TYPE1 -> {
                (holder as? ViewHolder2)?.apply {
                    textViewTitle.text = notNullString(data[position]["title"])
                    holder.textViewSubtitle.text = notNullString(data[position]["subtitle"])
                    TextViewAutoSplitFix(holder.textViewTitle)
                    holder.like.tag = position
                    (data[position]["like"] as? Boolean)?.let {
                        if (it) {
                            like.setColorFilter(context.resources.getColor(R.color.tangerine, context.theme))
                        } else {
                            like.setColorFilter(context.resources.getColor(R.color.slateGrey, context.theme))
                        }
                    }
                    like.setOnClickListener(likeOnClickListener)
                    itemView.setOnClickListener { v: View? -> mOnItemClickListener?.onItemClick(v, holder.adapterPosition) }
                    itemView.setOnLongClickListener { v: View? ->
                        mOnItemLongClickListener?.onItemClick(
                            v, holder.adapterPosition
                        )
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

    fun setLikeOnClickListener(likeOnClickListener: View.OnClickListener?) {
        this.likeOnClickListener = likeOnClickListener
    }

    private inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val textViewTitle: TextView = v.findViewById(R.id.search_boards_item_textView_title)
        val like: AppCompatImageButton = v.findViewById(R.id.search_boards_item_imageView_like)
    }

    private inner class ViewHolder2(v: View) : RecyclerView.ViewHolder(v) {
        val textViewTitle: TextView = v.findViewById(R.id.textView_hot_boards_title)
        val textViewSubtitle: TextView = v.findViewById(R.id.textView_hot_boards_subtitle)
        val like: AppCompatImageButton = v.findViewById(R.id.search_boards_item_imageView_like)
    }

    // define interface
    interface OnItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }

    interface OnItemLongClickListener {
        fun onItemClick(view: View?, position: Int)
    }

    companion object {
        private const val TYPE0 = 0
        private const val TYPE1 = 1
    }
}
