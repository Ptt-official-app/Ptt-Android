package cc.ptt.android.articlelist

import androidx.recyclerview.widget.RecyclerView
import cc.ptt.android.R
import cc.ptt.android.common.ResourcesUtils
import cc.ptt.android.data.model.remote.board.article.Article
import cc.ptt.android.databinding.ArticleListItemDeleteBinding

class DeletedViewHolder(private val binding: ArticleListItemDeleteBinding) : RecyclerView.ViewHolder(binding.root) {
    fun onBind(data: Article) {
        binding.apply {
            articleListItemTextViewTitle.text = data.title
            if (adapterPosition % 2 == 0) {
                articleListItemMain.setBackgroundColor(ResourcesUtils.getColor(itemView.context, R.attr.darkGreyTwo))
            } else {
                articleListItemMain.setBackgroundColor(ResourcesUtils.getColor(itemView.context, R.attr.black))
            }
        }
    }
}
