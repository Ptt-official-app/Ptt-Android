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
import androidx.recyclerview.widget.RecyclerView
import cc.ptt.android.R
import cc.ptt.android.data.common.StringUtils
import cc.ptt.android.data.common.StringUtils.TextViewAutoSplitFix
import cc.ptt.android.data.common.StringUtils.notNullString
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

class HotArticleFilterAdapter(
    private val context: Context,
    private val data: List<Map<String, Any>>
) : StickyAdapter<RecyclerView.ViewHolder?, RecyclerView.ViewHolder?>() {

    private var moreClickListen: View.OnClickListener? = null
    private var mOnItemClickListener: OnItemClickListener? = null
    private var mOnItemLongClickListener: OnItemLongClickListener? = null
    private var highLightUrl = ""

    @ColorInt
    private var ringColor = 0
    @ColorInt
    private var ringBackgroundColor = 0

    override fun getHeaderPositionForItem(itemPosition: Int): Int {
        return 0
    }

    override fun onBindHeaderViewHolder(holder: RecyclerView.ViewHolder?, headerPosition: Int) {
        holder?.run {
            onBindViewHolder(this, headerPosition)
        }
    }

    override fun onCreateHeaderViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return onCreateViewHolder(parent, TYPE_TITLE)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            TYPE_SUBITEM -> {
                val view = LayoutInflater.from(parent.context).inflate(
                    R.layout.hot_article_list_item_title_subitem, parent, false
                )
                return ViewHolderTitleSubitem(view)
            }
            TYPE_TITLE -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.hot_article_list_item_title_top, parent, false)
                return ViewHolderTitle(view)
            }
            TYPE_MORE -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.hot_article_list_item_more, parent, false)
                return ViewHolderMore(view)
            }
            else -> throw IllegalStateException("illegal view type: $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        var pos = TYPE_SUBITEM
        if (notNullString(data[position]["type"]).equals("title", ignoreCase = true)) {
            pos = TYPE_TITLE
        } else if (notNullString(data[position]["type"])
            .equals("more", ignoreCase = true)
        ) {
            pos = TYPE_MORE
        }
        return pos
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TYPE_SUBITEM -> {
                (holder as? ViewHolderTitleSubitem)?.apply {
                    textViewTitle.text = notNullString(data[position]["title"])
                    TextViewAutoSplitFix(textViewTitle)
                    if (data[position]["select"] as Boolean) {
                        val typedValue = TypedValue()
                        val theme = context.theme
                        theme.resolveAttribute(R.attr.tangerine, typedValue, true)
                        @ColorInt val color = typedValue.data
                        textViewTitle.setTextColor(color)
                    } else {
                        val typedValue = TypedValue()
                        val theme = context.theme
                        theme.resolveAttribute(R.attr.paleGrey, typedValue, true)
                        @ColorInt val color = typedValue.data
                        textViewTitle.setTextColor(color)
                    }
                    itemView.setOnClickListener { mOnItemClickListener?.onItemClick(it, adapterPosition) }
                    itemView.setOnLongClickListener {
                        mOnItemLongClickListener?.onItemClick(it, adapterPosition)
                        true
                    }
                }
            }
            TYPE_TITLE -> {
                (holder as? ViewHolderTitle)?.apply {
                    textViewTitle.text = notNullString(data[position]["title"])
                    more.setOnClickListener(moreClickListen)
                    itemView.setOnClickListener { mOnItemClickListener?.onItemClick(it, adapterPosition) }
                    itemView.setOnLongClickListener {
                        mOnItemLongClickListener?.onItemClick(it, adapterPosition)
                        true
                    }
                }
            }
            TYPE_MORE -> {
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
                .setProgressBarImage(ImageLoadingDrawable())
                .build()
            draweeView.controller = controller
            draweeView.hierarchy = hierarchy
        } catch (e: Exception) {
        }
    }

    inner class ViewHolderTitleSubitem(v: View) : RecyclerView.ViewHolder(v) {
        val textViewTitle: TextView = v.findViewById(R.id.article_list_item_textView_title)
        val main: LinearLayout = v.findViewById(R.id.article_list_item_main)
    }

    private inner class ViewHolderTitle(v: View) : RecyclerView.ViewHolder(v) {
        val textViewTitle: TextView = v.findViewById(R.id.article_list_item_textView_title)
        val main: LinearLayout = v.findViewById(R.id.article_list_item_main)
        val more: ImageButton = v.findViewById(R.id.article_list_item_imageButton_more)
    }

    private inner class ViewHolderMore(v: View) : RecyclerView.ViewHolder(v) {
        val main: LinearLayout = v.findViewById(R.id.article_list_item_main)
    }

    // define interface
    interface OnItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }

    interface OnItemLongClickListener {
        fun onItemClick(view: View?, position: Int)
    }

    init {
        val typedValue = TypedValue()
        val theme = context.theme
        theme.resolveAttribute(R.attr.slateGrey, typedValue, true)
        ringColor = typedValue.data
        theme.resolveAttribute(R.attr.paleGrey, typedValue, true)
        ringBackgroundColor = typedValue.data
    }

    companion object {
        private const val TYPE_SUBITEM = 0
        private const val TYPE_TITLE = 1
        private const val TYPE_MORE = 2
    }
}
