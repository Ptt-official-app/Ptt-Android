package cc.ptt.android.presentation.articlelist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cc.ptt.android.data.model.remote.board.article.Article
import cc.ptt.android.databinding.ArticleListItemBinding
import cc.ptt.android.databinding.ArticleListItemDeleteBinding

class ArticleListAdapter(
    private val articleList: List<Article>,
    private val mOnItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var selectArticle: Article? = null

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
        return if (articleList[position].deleted) {
            1
        } else {
            0
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val article = articleList[position]
        if (viewHolder is PostViewHolder) {
            viewHolder.onBind(article, article == selectArticle)
            viewHolder.itemView.setOnClickListener {
                mOnItemClickListener.onItemClick(article)
                if (selectArticle != article) {
                    article.read = true
                    selectArticle = article
                    notifyDataSetChanged()
                }
            }
        } else if (viewHolder is DeletedViewHolder) {
            viewHolder.onBind(article)
        }
    }

    override fun getItemCount(): Int {
        return articleList.size
    }

    interface OnItemClickListener {
        fun onItemClick(article: Article)
    }
}
