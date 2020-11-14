package tw.y_studio.ptt.UI.article.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.article_list_item_delete.view.*
import tw.y_studio.ptt.R
import tw.y_studio.ptt.Utils.getColor
import tw.y_studio.ptt.model.PartialPost

class DeletedViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    fun onBind(data: PartialPost) {
        itemView.apply {
            article_list_item_textView_title.text = data.title
            if (adapterPosition % 2 == 0) {
                article_list_item_main.setBackgroundColor(getColor(itemView.context, R.attr.darkGreyTwo))
            } else {
                article_list_item_main.setBackgroundColor(getColor(itemView.context, R.attr.black))
            }
        }
    }
}
