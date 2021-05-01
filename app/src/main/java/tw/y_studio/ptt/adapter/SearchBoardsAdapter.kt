package tw.y_studio.ptt.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import tw.y_studio.ptt.R
import tw.y_studio.ptt.api.model.board.search_board.SearchBoardsItem
import tw.y_studio.ptt.utils.PreferenceConstants
import tw.y_studio.ptt.utils.StringUtils.TextViewAutoSplitFix
import tw.y_studio.ptt.utils.StringUtils.notNullString

class SearchBoardsAdapter(
    private val data: List<SearchBoardsItem>,
    private val mOnItemClickListener: SearchBoardsAdapter.OnItemClickListener,
) : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
    private var ctx: Context? = null
    private var likeOnClickListener: View.OnClickListener? = null
    // private var mOnItemClickListener: OnItemClickListener? = null
    // private var mOnItemLongClickListener: OnItemLongClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        ctx = parent.context
        return when (viewType) {
            TYPE0 -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.search_boards_item, parent, false)
                ViewHolder(view)
            }
            TYPE1 -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.search_boards_item_2, parent, false)
                ViewHolder2(view)
            }
            else -> throw IllegalStateException("illegal view type: $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return ctx?.getSharedPreferences(PreferenceConstants.prefName, Context.MODE_PRIVATE)?.getInt("SEARCHSTYLE", TYPE0) ?: TYPE0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TYPE0 -> {
                (holder as? ViewHolder)?.apply {
                    textViewTitle.text = notNullString(data[position].title)
                    TextViewAutoSplitFix(holder.textViewTitle)
                    like.tag = position
                    ctx?.apply {
                        (data[position].like as? Boolean)?.let {
                            if (it) {
                                like.setColorFilter(this.resources.getColor(R.color.tangerine, this.theme))
                            } else {
                                like.setColorFilter(this.resources.getColor(R.color.slateGrey, this.theme))
                            }
                        }
                    }
                    like.setOnClickListener(likeOnClickListener)
                    itemView.setOnClickListener { mOnItemClickListener?.onItemClick(data[position]) }
                    /*itemView.setOnLongClickListener {
                        mOnItemLongClickListener?.onItemClick(it, adapterPosition)
                        true
                    }*/
                }
            }
            TYPE1 -> {
                (holder as? ViewHolder2)?.apply {
                    textViewTitle.text = notNullString(data[position].title)
                    holder.textViewSubtitle.text = notNullString(data[position].subtitle)
                    TextViewAutoSplitFix(holder.textViewTitle)
                    holder.like.tag = position
                    ctx?.apply {
                        (data[position].like as? Boolean)?.let {
                            if (it) {
                                like.setColorFilter(this.resources.getColor(R.color.tangerine, this.theme))
                            } else {
                                like.setColorFilter(this.resources.getColor(R.color.slateGrey, this.theme))
                            }
                        }
                    }
                    like.setOnClickListener(likeOnClickListener)
                    itemView.setOnClickListener { mOnItemClickListener?.onItemClick(data[position]) }
                    /*itemView.setOnLongClickListener {
                        mOnItemLongClickListener?.onItemClick(it, adapterPosition)
                        true
                    }*/
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    /*fun setOnItemClickListener(listener: OnItemClickListener?) {
        mOnItemClickListener = listener
    }

    fun setOnItemLongClickListener(listener: OnItemLongClickListener?) {
        mOnItemLongClickListener = listener
    }*/

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
        fun onItemClick(item: SearchBoardsItem)
    }

    /*interface OnItemLongClickListener {
        fun onItemClick(item : Map<String, Any>, position: Int)
    }*/

    companion object {
        private const val TYPE0 = 0
        private const val TYPE1 = 1
    }
}
