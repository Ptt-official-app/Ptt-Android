package tw.y_studio.ptt.ui.article.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tw.y_studio.ptt.api.model.PartialPost
import tw.y_studio.ptt.databinding.ArticleListItemBinding
import tw.y_studio.ptt.databinding.ArticleListItemDeleteBinding

class ArticleListAdapter(
    private val partialPostList: List<PartialPost>,
    private val mOnItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var selectedPartialPost = PartialPost()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> {
                PostViewHolder(ArticleListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
            else -> {
                DeletedViewHolder(ArticleListItemDeleteBinding.inflate(LayoutInflater.from(parent.context), parent, false))
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
