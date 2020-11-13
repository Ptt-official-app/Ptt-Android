package tw.y_studio.ptt.ui.article.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.article_list_item.view.*
import tw.y_studio.ptt.R
import tw.y_studio.ptt.model.PartialPost
import tw.y_studio.ptt.utils.StringUtils
import tw.y_studio.ptt.utils.getColor

class PostViewHolder(v: View) : RecyclerView.ViewHolder(v) {

    fun onBind(data: PartialPost, selected: Boolean) {
        itemView.apply {
            StringUtils.TextViewAutoSplitFix(article_list_item_textView_title)
            article_list_item_textView_title.text = data.title
            article_list_item_textView_date.text = data.date
            article_list_item_textView_class.text = data.category
            article_list_item_textView_commit.text = data.comments.toString()
            article_list_item_textView_like.text = data.like.toString()
            article_list_item_textView_auth.text = data.auth
            if (adapterPosition % 2 == 0) {
                article_list_item_main.setBackgroundColor(getColor(context, R.attr.darkGreyTwo))
            } else {
                article_list_item_main.setBackgroundColor(getColor(context, R.attr.black))
            }

            if (selected) {
                article_list_item_textView_title.setTextColor(getColor(context, R.attr.tangerine))
            } else {
                if (data.read) {
                    article_list_item_textView_title.setTextColor(getColor(context, R.attr.blueGrey))
                } else {
                    article_list_item_textView_title.setTextColor(getColor(context, R.attr.paleGrey))
                }
            }
        }
    }
}
