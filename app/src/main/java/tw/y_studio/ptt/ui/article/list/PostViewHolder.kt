package tw.y_studio.ptt.ui.article.list

import androidx.recyclerview.widget.RecyclerView
import tw.y_studio.ptt.R
import tw.y_studio.ptt.api.model.board.article.Article
import tw.y_studio.ptt.databinding.ArticleListItemBinding
import tw.y_studio.ptt.utils.ResourcesUtils
import tw.y_studio.ptt.utils.StringUtils

class PostViewHolder(private val binding: ArticleListItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(data: Article, selected: Boolean) {
        binding.apply {
            val context = itemView.context
            StringUtils.TextViewAutoSplitFix(articleListItemTextViewTitle)
            articleListItemTextViewTitle.text = data.title
            articleListItemTextViewDate.text = data.createTime.toString()
            articleListItemTextViewClass.text = data.classX
            articleListItemTextViewCommit.text = data.nComments.toString()
            articleListItemTextViewLike.text = data.recommend.toString()
            articleListItemTextViewAuth.text = data.owner
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
