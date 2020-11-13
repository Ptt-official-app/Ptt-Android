package tw.y_studio.ptt.ui.article.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tw.y_studio.ptt.R
import tw.y_studio.ptt.model.PartialPost

class ArticleListAdapter(
    private val partialPostList: List<PartialPost>,
    private val mOnItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var selectedPartialPost = PartialPost()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> {
                PostViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.article_list_item, parent, false))
            }
            else -> {
                DeletedViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.article_list_item_delete, parent, false))
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (partialPostList[position].deleted) {
            1
        } else {
            0
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val data = partialPostList[position]
        if (viewHolder is PostViewHolder) {
            viewHolder.onBind(data, data == selectedPartialPost)
            viewHolder.itemView.setOnClickListener {
                mOnItemClickListener.onItemClick(data)
                if (selectedPartialPost != data) {
                    data.read = true
                    selectedPartialPost = data
                    notifyDataSetChanged()
                }
            }
        } else if (viewHolder is DeletedViewHolder) {
            viewHolder.onBind(data)
        }
    }

    override fun getItemCount(): Int {
        return partialPostList.size
    }

    interface OnItemClickListener {
        fun onItemClick(partialPost: PartialPost)
    }
}
