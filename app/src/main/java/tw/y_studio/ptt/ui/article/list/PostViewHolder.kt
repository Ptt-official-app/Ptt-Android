package tw.y_studio.ptt.ui.article.list

import androidx.recyclerview.widget.RecyclerView
import tw.y_studio.ptt.R
import tw.y_studio.ptt.databinding.ArticleListItemBinding
import tw.y_studio.ptt.model.PartialPost
import tw.y_studio.ptt.utils.ResourcesUtils
import tw.y_studio.ptt.utils.StringUtils

class PostViewHolder(private val binding: ArticleListItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(data: PartialPost, selected: Boolean) {
        binding.apply {
            val context = itemView.context
            StringUtils.TextViewAutoSplitFix(articleListItemTextViewTitle)
            articleListItemTextViewTitle.text = data.title
            articleListItemTextViewDate.text = data.date
            articleListItemTextViewClass.text = data.category
            articleListItemTextViewCommit.text = data.comments.toString()
            articleListItemTextViewLike.text = data.like.toString()
            articleListItemTextViewAuth.text = data.auth
            if (adapterPosition % 2 == 0) {
                articleListItemMain.setBackgroundColor(ResourcesUtils.getColor(context, R.attr.darkGreyTwo))
            } else {
                articleListItemMain.setBackgroundColor(ResourcesUtils.getColor(context, R.attr.black))
            }

            if (selected) {
                articleListItemTextViewTitle.setTextColor(ResourcesUtils.getColor(context, R.attr.tangerine))
            } else {
                if (data.read) {
                    articleListItemTextViewTitle.setTextColor(ResourcesUtils.getColor(context, R.attr.blueGrey))
                } else {
                    articleListItemTextViewTitle.setTextColor(ResourcesUtils.getColor(context, R.attr.paleGrey))
                }
            }
        }
    }
}
