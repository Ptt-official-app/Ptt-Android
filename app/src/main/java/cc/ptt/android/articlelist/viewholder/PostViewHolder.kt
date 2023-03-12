package cc.ptt.android.articlelist.viewholder

import androidx.recyclerview.widget.RecyclerView
import cc.ptt.android.R
import cc.ptt.android.common.ResourcesUtils
import cc.ptt.android.common.StringUtils
import cc.ptt.android.common.date.DateFormatUtils
import cc.ptt.android.common.date.DatePatternConstants
import cc.ptt.android.data.model.remote.board.article.Article
import cc.ptt.android.databinding.ArticleListItemBinding

class PostViewHolder constructor(
    private val binding: ArticleListItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(data: Article, selected: Boolean) {
        binding.apply {
            val context = itemView.context
            val dateTime = DateFormatUtils.secondsToDateTime(
                data.createTime.toLong(),
                DatePatternConstants.articleDateTime
            )
            StringUtils.TextViewAutoSplitFix(articleListItemTextViewTitle)
            articleListItemTextViewTitle.text = data.title
            articleListItemTextViewDate.text = dateTime
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
