package cc.ptt.android.presentation.home.hotarticle

import android.content.Context
import android.graphics.PointF
import android.net.Uri
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import cc.ptt.android.R
import cc.ptt.android.data.common.StringUtils
import cc.ptt.android.data.common.StringUtils.TextViewAutoSplitFix
import cc.ptt.android.data.common.StringUtils.sortDecimal
import cc.ptt.android.data.model.ui.hotarticle.HotArticleUI
import cc.ptt.android.data.model.ui.hotarticle.HotArticleUIType
import cc.ptt.android.presentation.common.ImageLoadingDrawable
import cc.ptt.android.presentation.common.stickyheader.StickyAdapter
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.drawable.ScalingUtils
import com.facebook.drawee.generic.GenericDraweeHierarchy
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder
import com.facebook.drawee.interfaces.DraweeController
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.common.ResizeOptions
import com.facebook.imagepipeline.request.ImageRequest
import com.facebook.imagepipeline.request.ImageRequestBuilder

class HotArticleListAdapter constructor(
    private val context: Context,
    private val data: List<HotArticleUI>
) : StickyAdapter<RecyclerView.ViewHolder?, RecyclerView.ViewHolder?>() {

    private var ringColor = 0
    private var ringBackgroundColor = 0
    private var highLightUrl = ""
    private var moreClickListen: View.OnClickListener? = null
    private var mOnItemClickListener: OnItemClickListener? = null
    private var mOnItemLongClickListener: OnItemLongClickListener? = null

    override fun getHeaderPositionForItem(itemPosition: Int): Int {
        return 0
    }

    override fun onBindHeaderViewHolder(holder: RecyclerView.ViewHolder?, headerPosition: Int) {
        holder?.run {
            onBindViewHolder(this, headerPosition)
        }
    }

    override fun onCreateHeaderViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return onCreateViewHolder(parent, HotArticleUIType.TITLE.value)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            HotArticleUIType.NORMAL.value -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.hot_article_list_item, parent, false)
                ViewHolder(view)
            }
            HotArticleUIType.TITLE.value -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.hot_article_list_item_title, parent, false)
                ViewHolderTitle(view)
            }
            HotArticleUIType.MORE.value -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.hot_article_list_item_more, parent, false)
                ViewHolderMore(view)
            }
            else -> throw IllegalStateException("illegal view type: $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return data[position].type.value
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = data[position]
        when (getItemViewType(position)) {
            HotArticleUIType.NORMAL.value -> {
                (holder as? ViewHolder)?.update(item)
            }
            HotArticleUIType.TITLE.value -> {
                (holder as? ViewHolderTitle)?.update(item)
            }
            HotArticleUIType.MORE.value -> {
                (holder as? ViewHolderMore)?.update(item)
            }
        }
    }

    private fun setNumberColor(tv: TextView, sd: StringUtils.SortDecimal) {
        if (sd.isOverDecimal()) {
            val typedValue = TypedValue()
            val theme = context.theme
            theme.resolveAttribute(R.attr.tangerine, typedValue, true)
            @ColorInt val color = typedValue.data
            tv.setTextColor(color)
        } else {
            val typedValue = TypedValue()
            val theme = context.theme
            theme.resolveAttribute(R.attr.lightBlueGrey, typedValue, true)
            @ColorInt val color = typedValue.data
            tv.setTextColor(color)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setMoreClickListen(moreClickListen: View.OnClickListener?) {
        this.moreClickListen = moreClickListen
    }

    fun setHighLightUrl(url: String) {
        highLightUrl = url
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mOnItemClickListener = listener
    }

    fun setOnItemLongClickListener(listener: OnItemLongClickListener?) {
        mOnItemLongClickListener = listener
    }

    private fun setImageView(draweeView: SimpleDraweeView, Url: String) {
        if (draweeView.tag != null) {
            if (draweeView.tag.toString() == Url) {
                return
            }
        }
        draweeView.tag = Url
        try {
            val uri = Uri.parse(Url)
            val request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setLocalThumbnailPreviewsEnabled(true)
                .setProgressiveRenderingEnabled(false)
                .setLowestPermittedRequestLevel(ImageRequest.RequestLevel.FULL_FETCH)
                .setResizeOptions(ResizeOptions(1024, 1024))
                .build()
            val controller: DraweeController = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setAutoPlayAnimations(true)
                .setOldController(draweeView.controller)
                .build()
            val builder = GenericDraweeHierarchyBuilder(context.resources)
            val pf = PointF(0.5f, 0.5f)
            var hierarchy: GenericDraweeHierarchy? = null
            hierarchy = builder.setActualImageScaleType(ScalingUtils.ScaleType.FOCUS_CROP)
                .setActualImageFocusPoint(pf)
                .setFadeDuration(0)
                .setProgressBarImage(ImageLoadingDrawable()) // .setRoundingParams(roundingParams)
                .build()
            draweeView.controller = controller
            draweeView.hierarchy = hierarchy
        } catch (e: Exception) {
        }
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val textViewTitle: TextView = v.findViewById(R.id.article_list_item_textView_title)
        val textViewClass: TextView = v.findViewById(R.id.article_list_item_textView_class)
        val textViewAuth: TextView = v.findViewById(R.id.article_list_item_textView_auth)
        val textViewLike: TextView = v.findViewById(R.id.article_list_item_textView_like)
        val textViewCommit: TextView = v.findViewById(R.id.article_list_item_textView_commit)
        val textViewDate: TextView = v.findViewById(R.id.article_list_item_textView_date)
        val like: AppCompatImageButton = v.findViewById(R.id.article_list_item_imageButton_like)
        val dislike: AppCompatImageButton = v.findViewById(R.id.article_list_item_imageButton_dislike)
        val image: SimpleDraweeView = v.findViewById(R.id.article_list_item_picture)

        fun update(data: HotArticleUI) {
            TextViewAutoSplitFix(textViewTitle)
            textViewTitle.text = data.title ?: ""
            textViewDate.text = data.getDateText()
            textViewClass.text = data.getClassText()
            textViewAuth.text = data.auth
            val commit_ = sortDecimal(data.commit)
            val like_ = sortDecimal(data.like)
            textViewCommit.text = commit_.toString()
            setNumberColor(textViewCommit, commit_)
            textViewLike.text = like_.toString()
            setNumberColor(textViewLike, like_)
            if (data.url == highLightUrl) {
                val typedValue = TypedValue()
                val theme = context.theme
                theme.resolveAttribute(R.attr.tangerine, typedValue, true)
                @ColorInt val color = typedValue.data
                textViewTitle.setTextColor(color)
            } else {
                if (data.readed) {
                    val typedValue = TypedValue()
                    val theme = context.theme
                    theme.resolveAttribute(R.attr.blueGrey, typedValue, true)
                    @ColorInt val color = typedValue.data
                    textViewTitle.setTextColor(color)
                } else {
                    val typedValue = TypedValue()
                    val theme = context.theme
                    theme.resolveAttribute(R.attr.paleGrey, typedValue, true)
                    @ColorInt val color = typedValue.data
                    textViewTitle.setTextColor(color)
                }
            }
            val image = data.image
            if (image.equals("null", ignoreCase = true) || image.isEmpty()) {
                this.image.visibility = View.GONE
            } else {
                this.image.visibility = View.VISIBLE
                setImageView(this.image, image)
            }
            itemView.setOnClickListener { mOnItemClickListener?.onItemClick(it, adapterPosition, data) }
            itemView.setOnLongClickListener {
                mOnItemLongClickListener?.onItemClick(it, adapterPosition, data)
                true
            }
        }
    }

    private inner class ViewHolderTitle(v: View) : RecyclerView.ViewHolder(v) {
        val textViewTitle: TextView = v.findViewById(R.id.article_list_item_textView_title)
        val main: LinearLayout = v.findViewById(R.id.article_list_item_main)
        val more: ImageButton = v.findViewById(R.id.article_list_item_imageButton_more)

        fun update(data: HotArticleUI) {
            textViewTitle.text = data.title ?: ""
            more.setOnClickListener(moreClickListen)
            itemView.setOnClickListener { mOnItemClickListener?.onItemClick(it, adapterPosition, data) }
            itemView.setOnLongClickListener {
                mOnItemLongClickListener?.onItemClick(it, adapterPosition, data)
                true
            }
        }
    }

    private inner class ViewHolderMore(v: View) : RecyclerView.ViewHolder(v) {
        val main: LinearLayout = v.findViewById(R.id.article_list_item_main)

        fun update(data: HotArticleUI) {
            itemView.setOnClickListener { mOnItemClickListener?.onItemClick(it, adapterPosition, data) }
            itemView.setOnLongClickListener {
                mOnItemLongClickListener?.onItemClick(it, adapterPosition, data)
                true
            }
        }
    }

    // define interface
    interface OnItemClickListener {
        fun onItemClick(view: View?, position: Int, data: HotArticleUI)
    }

    interface OnItemLongClickListener {
        fun onItemClick(view: View?, position: Int, data: HotArticleUI)
    }

    init {
        val typedValue = TypedValue()
        val theme = context.theme
        theme.resolveAttribute(R.attr.slateGrey, typedValue, true)
        ringColor = typedValue.data
        theme.resolveAttribute(R.attr.paleGrey, typedValue, true)
        ringBackgroundColor = typedValue.data
    }
}
