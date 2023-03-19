package cc.ptt.android.articleread

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.text.PrecomputedTextCompat
import androidx.recyclerview.widget.RecyclerView
import cc.ptt.android.R
import cc.ptt.android.common.ResourcesUtils
import cc.ptt.android.common.StringUtils
import cc.ptt.android.common.TextViewMovementMethod
import cc.ptt.android.databinding.*
import cc.ptt.android.domain.model.ui.article.ArticleReadInfo
import coil.load
import java.lang.Exception
import java.lang.RuntimeException

class ArticleReadAdapter(private val data: List<ArticleReadInfo>) : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
    class ViewHolderHeader(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ArticleReadItemHeaderBinding.bind(view)

        init {
            binding.articleReadItemHeaderImageViewBack.setOnClickListener {
                (itemView.context as? Activity)?.onBackPressed()
            }
        }

        @SuppressLint("SetTextI18n")
        fun onBind(headerInfo: ArticleReadInfo.HeaderInfo) {
            binding.apply {
                articleReadItemHeaderTextViewTitle.text = headerInfo.title
                articleReadItemHeaderTextViewTime.text = headerInfo.date
                articleReadItemHeaderTextViewAuth.text = headerInfo.auth
                articleReadItemHeaderTextViewBoard.text = "${headerInfo.board} / ${headerInfo.type}"
            }
        }
    }

    class ViewHolderContent(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ArticleReadItemContentBinding.bind(view)

        fun onBind(contentLineInfo: ArticleReadInfo.ContentLineInfo) {
            binding.apply {
                articleReadItemTextView.apply {
                    StringUtils.TextViewAutoSplitFix(this)
                    movementMethod = TextViewMovementMethod(context)
                    setTextFuture(
                        PrecomputedTextCompat.getTextFuture(
                            contentLineInfo.richText,
                            textMetricsParamsCompat,
                            null
                        )
                    )
                }
            }
        }
    }

    inner class ViewHolderContentImage(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ArticleReadItemImageBinding.bind(view)

        fun onBind(imageInfo: ArticleReadInfo.ImageInfo) {
            binding.apply {
                setImageView(articleReadItemPicture, imageInfo.url.orEmpty())
            }
        }
    }

    inner class ViewHolderCenterBar(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ArticleReadItemCenterBarBinding.bind(view)

        fun onBind(centerBarInfo: ArticleReadInfo.CenterBarInfo) {
            binding.apply {
                articleReadItemCenterbarTextViewCommit.apply {
                    val floorString = StringUtils.sortDecimal(StringUtils.notNullString(centerBarInfo.floor))
                    text = floorString.toString()
                    setNumberColor(this, floorString)
                }
                articleReadItemCenterbarTextViewLike.apply {
                    val likeString = StringUtils.sortDecimal(StringUtils.notNullString(centerBarInfo.like))
                    text = likeString.toString()
                    setNumberColor(this, likeString)
                    val like = try {
                        StringUtils.notNullString(centerBarInfo.like).toInt()
                    } catch (e: Exception) {
                        0
                    }
                    setTextColor(
                        ResourcesUtils.getColor(
                            itemView.context,
                            if (like > 1000) {
                                R.attr.tangerine
                            } else {
                                R.attr.lightBlueGrey
                            }
                        )
                    )
                }
            }
        }
    }

    inner class ViewHolderComment(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ArticleReadItemCommitBinding.bind(view)

        fun onBind(commentInfo: ArticleReadInfo.CommentInfo) {
            binding.apply {

                articleReadItemCommitTextViewText.apply {
                    StringUtils.TextViewAutoSplitFix(this)
                    movementMethod = TextViewMovementMethod(context)
                    val future = PrecomputedTextCompat.getTextFuture(
                        commentInfo.text,
                        textMetricsParamsCompat,
                        null
                    )
                    setTextFuture(future)
                }
                articleReadItemCommitTextviewAuth.text = commentInfo.auth
            }
        }
    }

    inner class ViewHolderCommitBar(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ArticleReadItemCommitBarBinding.bind(view)

        fun onBind(commentBarInfo: ArticleReadInfo.CommentBarInfo) {
            binding.apply {
                articleReadItemCommitTextViewTime.text = commentBarInfo.time
                articleReadItemCommitTextViewFloor.text = commentBarInfo.floor
                val colorString = StringUtils.sortDecimal(StringUtils.notNullString(commentBarInfo.like))
                aarticleReadItemCommitTextViewLike.text = colorString.toString()
                setNumberColor(aarticleReadItemCommitTextViewLike, colorString)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(viewType, parent, false)
        when (viewType) {
            R.layout.article_read_item_header -> {
                return ViewHolderHeader(view)
            }
            R.layout.article_read_item_content -> {
                return ViewHolderContent(view)
            }
            R.layout.article_read_item_image -> {
                return ViewHolderContentImage(view)
            }
            R.layout.article_read_item_center_bar -> {
                return ViewHolderCenterBar(view)
            }
            R.layout.article_read_item_commit -> {
                return ViewHolderComment(view)
            }
            R.layout.article_read_item_commit_bar -> {
                return ViewHolderCommitBar(view)
            }
            else -> {
                throw RuntimeException("non expected type")
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (data[position]) {
            is ArticleReadInfo.HeaderInfo -> R.layout.article_read_item_header
            is ArticleReadInfo.ContentLineInfo -> R.layout.article_read_item_content
            is ArticleReadInfo.ImageInfo -> R.layout.article_read_item_image
            is ArticleReadInfo.CenterBarInfo -> R.layout.article_read_item_center_bar
            is ArticleReadInfo.CommentInfo -> R.layout.article_read_item_commit
            is ArticleReadInfo.CommentBarInfo -> R.layout.article_read_item_commit_bar
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val item = data[position]
        if (viewHolder is ViewHolderHeader && item is ArticleReadInfo.HeaderInfo) {
            viewHolder.onBind(item)
        } else if (viewHolder is ViewHolderContent && item is ArticleReadInfo.ContentLineInfo) {
            viewHolder.onBind(item)
        } else if (viewHolder is ViewHolderComment && item is ArticleReadInfo.CommentInfo) {
            viewHolder.onBind(item)
            updateBackgroundColor(viewHolder, item.index)
        } else if (viewHolder is ViewHolderCommitBar && item is ArticleReadInfo.CommentBarInfo) {
            viewHolder.onBind(item)
            updateBackgroundColor(viewHolder, item.index)
        } else if (viewHolder is ViewHolderCenterBar && item is ArticleReadInfo.CenterBarInfo) {
            viewHolder.onBind(item)
        } else if (viewHolder is ViewHolderContentImage && item is ArticleReadInfo.ImageInfo) {
            viewHolder.onBind(item)
            updateBackgroundColor(viewHolder, item.index)
        }
    }

    private fun setNumberColor(tv: TextView, sd: StringUtils.SortDecimal) {
        tv.setTextColor(
            ResourcesUtils.getColor(
                tv.context,
                if (sd.isOverDecimal()) {
                    R.attr.tangerine
                } else {
                    R.attr.lightBlueGrey
                }
            )
        )
    }

    private fun updateBackgroundColor(viewHolder: RecyclerView.ViewHolder, index: Int) {
        viewHolder.itemView.apply {
            setBackgroundColor(getBackgroundColor(context, index))
        }
    }

    @ColorInt
    private fun getBackgroundColor(context: Context, index: Int): Int {
        val typedValue = TypedValue()
        val theme = context.theme
        return if (index % 2 == 0) {
            theme.resolveAttribute(R.attr.darkGreyTwo, typedValue, true)
            typedValue.data
        } else {
            theme.resolveAttribute(R.attr.article_header, typedValue, true)
            typedValue.data
        }
    }

    private fun setImageView(imageView: ImageView, url: String) {
        if (imageView.tag != null) {
            if (imageView.tag.toString() == url) {
                return
            }
        }
        imageView.tag = url
        imageView.load(url)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
