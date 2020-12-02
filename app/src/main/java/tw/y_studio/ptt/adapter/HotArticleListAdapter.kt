package tw.y_studio.ptt.adapter

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
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.drawable.ScalingUtils
import com.facebook.drawee.generic.GenericDraweeHierarchy
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder
import com.facebook.drawee.interfaces.DraweeController
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.common.ResizeOptions
import com.facebook.imagepipeline.request.ImageRequest
import com.facebook.imagepipeline.request.ImageRequestBuilder
import tw.y_studio.ptt.R
import tw.y_studio.ptt.ui.ImageLoadingDrawable
import tw.y_studio.ptt.ui.stickyheader.StickyAdapter
import tw.y_studio.ptt.utils.StringUtils.SortDecimal
import tw.y_studio.ptt.utils.StringUtils.TextViewAutoSplitFix
import tw.y_studio.ptt.utils.StringUtils.notNullString
import tw.y_studio.ptt.utils.StringUtils.sortDecimal
import java.util.*
import kotlin.math.abs

class HotArticleListAdapter(private val context: Context, private val data: List<Map<String, Any>>) : StickyAdapter<RecyclerView.ViewHolder?, RecyclerView.ViewHolder?>() {
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
        return onCreateViewHolder(parent, TYPE_NORMAL)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_NORMAL -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.hot_article_list_item, parent, false)
                ViewHolder(view)
            }
            TYPE_TITLE -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.hot_article_list_item_title, parent, false)
                ViewHolderTitle(view)
            }
            TYPE_MORE -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.hot_article_list_item_more, parent, false)
                ViewHolderMore(view)
            }
            else -> throw IllegalStateException("illegal view type: $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        var pos = TYPE_NORMAL
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
            TYPE_NORMAL -> {
                (holder as? ViewHolder)?.apply {
                    TextViewAutoSplitFix(textViewTitle)
                    textViewTitle.text = notNullString(data[position]["title"])
                    val timeL = notNullString(data[position]["date"]).toLong() / 1000
                    val nowTime = Date().time / 1000
                    var showT = ""
                    showT = if (abs(nowTime - timeL) < 60 * 60) {
                        (abs(nowTime - timeL) / 60).toString() + "分鐘前"
                    } else if (abs(nowTime - timeL) < 60 * 60 * 24) {
                        (abs(nowTime - timeL) / 60 / 60).toString() + "小時前"
                    } else {
                        " " + abs(nowTime - timeL) / 60 / 24 / 60 + "天前"
                    }
                    if (showT.length <= 4) {
                        showT = " $showT"
                    }
                    textViewDate.text = showT
                    textViewClass.text = notNullString(
                        data[position]["board"]
                            .toString() + " / " +
                            notNullString(data[position]["class"])
                    )
                    textViewAuth.text = notNullString(data[position]["auth"])
                    val commit_ = sortDecimal(
                        notNullString(data[position]["commit"])
                    )
                    val like_ = sortDecimal(
                        notNullString(data[position]["like"])
                    )
                    textViewCommit.text = commit_.toString()
                    setNumberColor(textViewCommit, commit_)
                    textViewLike.text = like_.toString()
                    setNumberColor(textViewLike, like_)
                    if (notNullString(data[position]["url"]) == highLightUrl) {
                        val typedValue = TypedValue()
                        val theme = context.theme
                        theme.resolveAttribute(R.attr.tangerine, typedValue, true)
                        @ColorInt val color = typedValue.data
                        textViewTitle.setTextColor(color)
                    } else {
                        if (data[position]["readed"] as Boolean) {
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
                    val image = notNullString(data[position]["image"])
                    if (image.equals("null", ignoreCase = true) || image.isEmpty()) {
                        this.image.visibility = View.GONE
                    } else {
                        this.image.visibility = View.VISIBLE
                        setImageView(this.image, image)
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
                (holder as? ViewHolderMore)?.apply {
                    itemView.setOnClickListener { mOnItemClickListener?.onItemClick(it, adapterPosition) }
                    itemView.setOnLongClickListener {
                        mOnItemLongClickListener?.onItemClick(it, adapterPosition)
                        true
                    }
                }
            }
        }
    }

    private fun setNumberColor(tv: TextView, sd: SortDecimal) {
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
        val more: AppCompatImageButton = v.findViewById(R.id.article_list_item_imageButton_more)
        val image: SimpleDraweeView = v.findViewById(R.id.article_list_item_picture)
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
        private const val TYPE_NORMAL = 0
        private const val TYPE_TITLE = 1
        private const val TYPE_MORE = 2
    }
}
