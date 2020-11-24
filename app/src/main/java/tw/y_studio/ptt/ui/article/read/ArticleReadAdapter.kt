package tw.y_studio.ptt.ui.article.read

import android.app.Activity
import android.content.Context
import android.graphics.PointF
import android.graphics.drawable.Animatable
import android.net.Uri
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.PrecomputedTextCompat
import androidx.recyclerview.widget.RecyclerView
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.controller.BaseControllerListener
import com.facebook.drawee.drawable.ScalingUtils
import com.facebook.drawee.generic.GenericDraweeHierarchy
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder
import com.facebook.drawee.interfaces.DraweeController
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.common.ResizeOptions
import com.facebook.imagepipeline.image.ImageInfo
import com.facebook.imagepipeline.request.ImageRequest
import com.facebook.imagepipeline.request.ImageRequestBuilder
import tw.y_studio.ptt.R
import tw.y_studio.ptt.ui.ImageLoadingDrawable
import tw.y_studio.ptt.ui.StaticValue
import tw.y_studio.ptt.ui.TextViewMovementMethod
import tw.y_studio.ptt.ui.article.read.ArticleReadAdapter.ViewHolderCenterBar
import tw.y_studio.ptt.ui.article.read.ArticleReadAdapter.ViewHolderComment
import tw.y_studio.ptt.ui.article.read.ArticleReadAdapter.ViewHolderCommitBar
import tw.y_studio.ptt.ui.article.read.ArticleReadAdapter.ViewHolderCommitSort
import tw.y_studio.ptt.ui.article.read.ArticleReadAdapter.ViewHolderContent
import tw.y_studio.ptt.ui.article.read.ArticleReadAdapter.ViewHolderContentImage
import tw.y_studio.ptt.ui.article.read.ArticleReadAdapter.ViewHolderHeader
import tw.y_studio.ptt.utils.StringUtils
import tw.y_studio.ptt.utils.StringUtils.SortDecimal
import java.lang.Exception
import java.lang.RuntimeException

class ArticleReadAdapter(private val context: Context, private val data: List<Map<String, Any>>) : RecyclerView.Adapter<RecyclerView.ViewHolder?>(), View.OnClickListener, OnLongClickListener {
    inner class ViewHolderHeader(v: View) : RecyclerView.ViewHolder(v) {
        val mTextView_title: TextView
        val mTextView_board: TextView
        val mTextView_auth: TextView
        val mTextView_date: TextView
        val back: AppCompatImageButton

        init {
            mTextView_title = v.findViewById(R.id.article_read_item_header_textView_title)
            mTextView_board = v.findViewById(R.id.article_read_item_header_textView_board)
            mTextView_auth = v.findViewById(R.id.article_read_item_header_textView_auth)
            mTextView_date = v.findViewById(R.id.article_read_item_header_textView_time)
            back = v.findViewById(R.id.article_read_item_header_imageView_back)
        }
    }

    inner class ViewHolderContent(v: View) : RecyclerView.ViewHolder(v) {
        val mTextView_text: AppCompatTextView

        init {
            mTextView_text = v.findViewById(R.id.article_read_item_textView)
        }
    }

    inner class ViewHolderContentImage(v: View) : RecyclerView.ViewHolder(v) {
        val image: SimpleDraweeView
        val main: LinearLayout

        init {
            image = v.findViewById(R.id.article_read_item_picture)
            main = v.findViewById(R.id.article_read_item_picture_main)
        }
    }

    inner class ViewHolderCenterBar(v: View) : RecyclerView.ViewHolder(v) {
        val textView_floor: TextView
        val textView_like: TextView

        init {
            textView_floor = v.findViewById(R.id.article_read_item_centerbar_textView_commit)
            textView_like = v.findViewById(R.id.article_read_item_centerbar_textView_like)
        }
    }

    inner class ViewHolderComment(v: View) : RecyclerView.ViewHolder(v) {
        val textView_auth: TextView
        val textView_text: AppCompatTextView
        val main: LinearLayout

        init {
            textView_auth = v.findViewById(R.id.article_read_item_commit_textview_auth)
            textView_text = v.findViewById(R.id.article_read_item_commit_textView_text)
            main = v.findViewById(R.id.article_read_item_commit_main)
        }
    }

    inner class ViewHolderCommitBar(v: View) : RecyclerView.ViewHolder(v) {
        val textView_floor: TextView
        val textView_time: TextView
        val textView_like: TextView
        val main: LinearLayout

        init {
            textView_floor = v.findViewById(R.id.article_read_item_commit_textView_floor)
            textView_time = v.findViewById(R.id.article_read_item_commit_textView_time)
            textView_like = v.findViewById(R.id.aarticle_read_item_commit_textView_like)
            main = v.findViewById(R.id.article_read_item_commit_main)
        }
    }

    inner class ViewHolderCommitSort(v: View?) : RecyclerView.ViewHolder(v!!)

    private val inflater: LayoutInflater
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            0 -> {
                val view1 = inflater.inflate(R.layout.article_read_item_header, parent, false)
                // view1.setOnClickListener(this);
                // view1.setOnLongClickListener(this);
                return ViewHolderHeader(view1)
            }
            1 -> {
                val view2 = inflater.inflate(R.layout.article_read_item_content, parent, false)
                // view1.setOnClickListener(this);
                // view1.setOnLongClickListener(this);
                return ViewHolderContent(view2)
            }
            2 -> {
                val view3 = inflater.inflate(R.layout.article_read_item_image, parent, false)
                // view1.setOnClickListener(this);
                // view1.setOnLongClickListener(this);
                return ViewHolderContentImage(view3)
            }
            3 -> {
                val view4 = inflater.inflate(R.layout.article_read_item_center_bar, parent, false)
                // view1.setOnClickListener(this);
                // view1.setOnLongClickListener(this);
                return ViewHolderCenterBar(view4)
            }
            4 -> {
                val view5 = inflater.inflate(R.layout.article_read_item_commit, parent, false)
                // view1.setOnClickListener(this);
                // view1.setOnLongClickListener(this);
                return ViewHolderComment(view5)
            }
            5 -> {
                val view6 = inflater.inflate(R.layout.article_read_item_commit_sort, parent, false)
                // view1.setOnClickListener(this);
                // view1.setOnLongClickListener(this);
                return ViewHolderCommitSort(view6)
            }
            6 -> {
                val view7 = inflater.inflate(R.layout.article_read_item_commit_bar, parent, false)
                // view1.setOnClickListener(this);
                // view1.setOnLongClickListener(this);
                return ViewHolderCommitBar(view7)
            }
            else -> {
            }
        }
        throw RuntimeException("non expected type")
    }

    override fun getItemViewType(position: Int): Int {
        var pos = 5
        if (StringUtils.notNullString(data[position]["type"]).equals("header", ignoreCase = true)) {
            pos = 0
        } else if (StringUtils.notNullString(data[position]["type"])
            .equals("content", ignoreCase = true)
        ) {
            pos = 1
        } else if (StringUtils.notNullString(data[position]["type"])
            .equals("content_image", ignoreCase = true)
        ) {
            pos = 2
        } else if (StringUtils.notNullString(data[position]["type"])
            .equals("center_bar", ignoreCase = true)
        ) {
            pos = 3
        } else if (StringUtils.notNullString(data[position]["type"])
            .equals("commit", ignoreCase = true)
        ) {
            pos = 4
        } else if (StringUtils.notNullString(data[position]["type"])
            .equals("commit_sort", ignoreCase = true)
        ) {
            pos = 5
        } else if (StringUtils.notNullString(data[position]["type"])
            .equals("commit_bar", ignoreCase = true)
        ) {
            pos = 6
        }
        return pos
    }

    override fun onBindViewHolder(holderO: RecyclerView.ViewHolder, position: Int) {
        holderO!!.itemView.tag = position
        if (holderO is ViewHolderHeader) {
            val holder = holderO
            StringUtils.TextViewAutoSplitFix(holder.mTextView_title)
            holder.mTextView_title.text = StringUtils.notNullString(data[position]["title"])
            holder.mTextView_date.text = StringUtils.notNullString(data[position]["date"])
            holder.mTextView_auth.text = StringUtils.notNullString(data[position]["auth"])
            holder.mTextView_board.text = (
                StringUtils.notNullString(data[position]["board"]) +
                    " / " +
                    StringUtils.notNullString(data[position]["class"])
                )
            holder.back.setOnClickListener { (context as Activity).onBackPressed() }
        } else if (holderO is ViewHolderContent) {
            val holder = holderO

            // holder.mTextView_text.setText(StringUtils.ColorString(StringUtils.notNullString(data.get(position).get("text"))));
            // holder.mTextView_text.setText((StringUtils.notNullString(data.get(position).get("text"))));
            StringUtils.TextViewAutoSplitFix(holder.mTextView_text)
            // Log.d(this.getClass().getName(),"mText.getLineHeight() =
            // "+holder.mTextView_text.getLineHeight());
            // holder.mTextView_text.setLineSpacing(0, (float) (400d/StaticValue.densityDpi));
            holder.mTextView_text.movementMethod = TextViewMovementMethod(context)
            val future = PrecomputedTextCompat.getTextFuture(
                StringUtils.ColorString(
                    StringUtils.notNullString(data[position]["text"])
                ),
                holder.mTextView_text.textMetricsParamsCompat,
                null
            )
            holder.mTextView_text.setTextFuture(future)
        } else if (holderO is ViewHolderComment) {
            val holder = holderO

            // holder.textView_text.setText(StringUtils.ColorString(StringUtils.notNullString(data.get(position).get("text"))));
            // holder.mTextView_text.setText((StringUtils.notNullString(data.get(position).get("text"))));
            StringUtils.TextViewAutoSplitFix(holder.textView_text)
            holder.textView_text.movementMethod = TextViewMovementMethod(context)
            // holder.textView_text.setLineSpacing(0, (float) (400d/StaticValue.densityDpi));
            holder.textView_auth.text = StringUtils.ColorString(
                StringUtils.notNullString(data[position]["auth"])
            )
            // holder.textView_time.setText(StringUtils.ColorString(StringUtils.notNullString(data.get(position).get("time"))));
            // holder.textView_floor.setText(StringUtils.ColorString(StringUtils.notNullString(data.get(position).get("floor"))));
            // holder.textView_like.setText(StringUtils.ColorString(StringUtils.notNullString(data.get(position).get("like"))));
            val index = data[position]["index"] as Int
            if (index % 2 == 0) {
                val typedValue = TypedValue()
                val theme = context.theme
                theme.resolveAttribute(R.attr.darkGreyTwo, typedValue, true)
                @ColorInt val color = typedValue.data
                holder.main.setBackgroundColor(color)
            } else {
                // holder.main.setBackgroundResource(R.color.black);
                val typedValue = TypedValue()
                val theme = context.theme
                theme.resolveAttribute(R.attr.article_header, typedValue, true)
                @ColorInt val color = typedValue.data
                holder.main.setBackgroundColor(color)
            }
            val future = PrecomputedTextCompat.getTextFuture(
                StringUtils.ColorString(
                    StringUtils.notNullString(data[position]["text"])
                ),
                holder.textView_text.textMetricsParamsCompat,
                null
            )
            holder.textView_text.setTextFuture(future)
        } else if (holderO is ViewHolderCommitBar) {
            val holder = holderO

            // holder.textView_text.setText(StringUtils.ColorString(StringUtils.notNullString(data.get(position).get("text"))));
            // holder.mTextView_text.setText((StringUtils.notNullString(data.get(position).get("text"))));
            // StringUtils.TextViewAutoSplitFix(holder.textView_text);
            // holder.textView_text.setMovementMethod(new TextViewMovementMethod(context));
            // holder.textView_auth.setText(StringUtils.ColorString(StringUtils.notNullString(data.get(position).get("auth"))));
            holder.textView_time.text = StringUtils.ColorString(
                StringUtils.notNullString(data[position]["time"])
            )
            val floor_ = StringUtils.sortDecimal(
                StringUtils.notNullString(data[position]["floor"])
            )
            holder.textView_floor.text = StringUtils.notNullString(data[position]["floor"])
            // setNumberColor(holder.textView_floor,floor_);
            val like_ = StringUtils.sortDecimal(
                StringUtils.notNullString(data[position]["like"])
            )
            // holder.textView_floor.setText((StringUtils.notNullString(data.get(position).get("floor"))));
            holder.textView_like.text = like_.toString()
            setNumberColor(holder.textView_like, like_)

            // holder.textView_floor.setText(StringUtils.ColorString(StringUtils.notNullString(data.get(position).get("floor"))));
            // holder.textView_like.setText(StringUtils.ColorString(StringUtils.notNullString(data.get(position).get("like"))));
            val index = data[position]["index"] as Int
            if (index % 2 == 0) {
                val typedValue = TypedValue()
                val theme = context.theme
                theme.resolveAttribute(R.attr.darkGreyTwo, typedValue, true)
                @ColorInt val color = typedValue.data
                holder.main.setBackgroundColor(color)
            } else {
                // holder.main.setBackgroundResource(R.color.black);
                val typedValue = TypedValue()
                val theme = context.theme
                theme.resolveAttribute(R.attr.article_header, typedValue, true)
                @ColorInt val color = typedValue.data
                holder.main.setBackgroundColor(color)
            }
        } else if (holderO is ViewHolderCommitSort) {
            val holder = holderO
        } else if (holderO is ViewHolderCenterBar) {
            val holder = holderO
            val floor_ = StringUtils.sortDecimal(
                StringUtils.notNullString(data[position]["floor"])
            )
            holder.textView_floor.text = floor_.toString()
            setNumberColor(holder.textView_floor, floor_)
            val like_ = StringUtils.sortDecimal(
                StringUtils.notNullString(data[position]["like"])
            )
            // holder.textView_floor.setText((StringUtils.notNullString(data.get(position).get("floor"))));
            holder.textView_like.text = like_.toString()
            setNumberColor(holder.textView_like, like_)
            var like = 0
            try {
                like =
                    StringUtils.notNullString(data[position]["like"]).toInt()
            } catch (e: Exception) {
            }
            if (like > 1000) {
                val typedValue = TypedValue()
                val theme = context.theme
                theme.resolveAttribute(R.attr.tangerine, typedValue, true)
                @ColorInt val color = typedValue.data
                holder.textView_like.setTextColor(color)
            } else {
                val typedValue = TypedValue()
                val theme = context.theme
                theme.resolveAttribute(R.attr.lightBlueGrey, typedValue, true)
                @ColorInt val color = typedValue.data
                holder.textView_like.setTextColor(color)
            }
            // StringUtils.TextViewAutoSplitFix(holder.mTextView_text);
        } else if (holderO is ViewHolderContentImage) {
            val holder = holderO
            setImageView(holder.image, StringUtils.notNullString(data[position]["url"]))
            val index = data[position]["index"] as Int
            if (Math.abs(index % 2) == 1) {
                val typedValue = TypedValue()
                val theme = context.theme
                theme.resolveAttribute(R.attr.darkGreyTwo, typedValue, true)
                @ColorInt val color = typedValue.data
                holder.main.setBackgroundColor(color)
            } else {
                // holder.main.setBackgroundResource(R.color.black);
                val typedValue = TypedValue()
                val theme = context.theme
                theme.resolveAttribute(R.attr.article_header, typedValue, true)
                @ColorInt val color = typedValue.data
                holder.main.setBackgroundColor(color)
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

    private fun setImageView(draweeView: SimpleDraweeView, Url: String) {
        if (draweeView.tag != null) {
            if (draweeView.tag.toString() == Url) {
                return
            }
        }
        draweeView.tag = Url
        try {
            val controllerListener = object : BaseControllerListener<ImageInfo?>() {
                override fun onFinalImageSet(
                    id: String,
                    imageInfo: ImageInfo?,
                    anim: Animatable?
                ) {
                    if (imageInfo == null) {
                        return
                    }
                    val qualityInfo = imageInfo.qualityInfo
                    var caculor = (
                        StaticValue.widthPixels /
                            imageInfo.width
                            * imageInfo.height
                        )
                    if (caculor > StaticValue.widthPixels) {
                        caculor = StaticValue.widthPixels
                    }
                    /*if (!imageHight.containsKey(Url)) {
                                            imageHight.put(Url, (int) caculor);
                            }*/if (caculor < StaticValue.widthPixels * 0.3) caculor = StaticValue.widthPixels * 0.3
                    draweeView.layoutParams.height = caculor.toInt()
                    draweeView.requestLayout()
                    // mtv.setVisibility(View.GONE);
                }

                override fun onIntermediateImageSet(
                    id: String,
                    imageInfo: ImageInfo?
                ) {
                    // mtv.setText(id);
                }

                override fun onFailure(id: String, throwable: Throwable) {
                    // mtv.setText("載入失敗("+throwable.getMessage()+")");
                }
            }
            val uri = Uri.parse(Url)
            val request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setLocalThumbnailPreviewsEnabled(true)
                .setProgressiveRenderingEnabled(false)
                .setLowestPermittedRequestLevel(ImageRequest.RequestLevel.FULL_FETCH)
                .setResizeOptions(ResizeOptions(2048, 2048))
                .build()
            val controller: DraweeController = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setTapToRetryEnabled(true)
                .setAutoPlayAnimations(true)
                .setControllerListener(controllerListener)
                .setOldController(draweeView.controller)
                .build()
            val builder = GenericDraweeHierarchyBuilder(context.resources)
            val pf = PointF(0.5f, 0.5f)
            var hierarchy: GenericDraweeHierarchy? = null
            hierarchy = builder.setPressedStateOverlay(
                context.resources.getDrawable(R.drawable.image_click)
            )
                .setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER)
                .setActualImageFocusPoint(pf) // .setBackground(context.getResources().getDrawable(R.drawable.image_backgroung))
                .setFadeDuration(0)
                .setProgressBarImage(ImageLoadingDrawable(2f)) // .setRoundingParams(roundingParams)
                .build()
            draweeView.controller = controller
            draweeView.hierarchy = hierarchy
        } catch (e: Exception) {
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    // define interface
    interface OnItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }

    interface OnItemLongClickListener {
        fun onItemClick(view: View?, position: Int)
    }

    private var mOnItemClickListener: OnItemClickListener? = null
    private var mOnItemLongClickListener: OnItemLongClickListener? = null
    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mOnItemClickListener = listener
    }

    fun setOnItemLongClickListener(listener: OnItemLongClickListener?) {
        mOnItemLongClickListener = listener
    }

    override fun onClick(v: View) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener!!.onItemClick(v, v.tag as Int)
        }
    }

    override fun onLongClick(v: View): Boolean {
        if (mOnItemLongClickListener != null) {
            mOnItemLongClickListener!!.onItemClick(v, v.tag as Int)
            return true
        }
        return false
    }

    init {
        inflater = LayoutInflater.from(context)
    }
}
