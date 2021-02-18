package tw.y_studio.ptt.ui.article.list

import androidx.recyclerview.widget.RecyclerView
import tw.y_studio.ptt.R
import tw.y_studio.ptt.api.model.PartialPost
import tw.y_studio.ptt.databinding.ArticleListItemDeleteBinding
import tw.y_studio.ptt.utils.ResourcesUtils

class DeletedViewHolder(private val binding: ArticleListItemDeleteBinding) : RecyclerView.ViewHolder(binding.root) {
    fun onBind(data: PartialPost) {
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
