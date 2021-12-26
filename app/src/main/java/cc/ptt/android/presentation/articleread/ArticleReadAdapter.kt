package cc.ptt.android.presentation.articleread

import android.app.Activity
import android.content.Context
import android.graphics.PointF
import android.graphics.drawable.Animatable
import android.net.Uri
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.text.PrecomputedTextCompat
import androidx.recyclerview.widget.RecyclerView
import cc.ptt.android.R
import cc.ptt.android.data.common.ResourcesUtils
import cc.ptt.android.data.common.StringUtils
import cc.ptt.android.databinding.*
import cc.ptt.android.presentation.common.ImageLoadingDrawable
import cc.ptt.android.presentation.common.StaticValue
import cc.ptt.android.presentation.common.TextViewMovementMethod
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
import java.lang.Exception
import java.lang.RuntimeException

class ArticleReadAdapter(private val data: List<Item>) : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
    class ViewHolderHeader(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ArticleReadItemHeaderBinding.bind(view)

        init {
            binding.articleReadItemHeaderImageViewBack.setOnClickListener {
                (itemView.context as? Activity)?.onBackPressed()
            }
        }

        fun onBind(headerItem: Item.HeaderItem) {
            binding.apply {
                articleReadItemHeaderTextViewTitle.text = headerItem.title
                articleReadItemHeaderTextViewTime.text = headerItem.date
                articleReadItemHeaderTextViewAuth.text = headerItem.auth
                articleReadItemHeaderTextViewBoard.text = "${headerItem.board} / ${headerItem.type}"
            }
        }
    }

    class ViewHolderContent(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ArticleReadItemContentBinding.bind(view)

        fun onBind(contentLineItem: Item.ContentLineItem) {
            binding.apply {
                articleReadItemTextView.apply {
                    StringUtils.TextViewAutoSplitFix(this)
                    movementMethod = TextViewMovementMethod(context)
                    setTextFuture(
                        PrecomputedTextCompat.getTextFuture(
                            StringUtils.ColorString(contentLineItem.text),
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

        fun onBind(imageItem: Item.ImageItem) {
            binding.apply {
                setImageView(articleReadItemPicture, StringUtils.notNullString(imageItem.url))
            }
        }
    }

    inner class ViewHolderCenterBar(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ArticleReadItemCenterBarBinding.bind(view)

        fun onBind(centerBarItem: Item.CenterBarItem) {
            binding.apply {
                articleReadItemCenterbarTextViewCommit.apply {
                    val floorString = StringUtils.sortDecimal(StringUtils.notNullString(centerBarItem.floor))
                    text = floorString.toString()
                    setNumberColor(this, floorString)
                }
                articleReadItemCenterbarTextViewLike.apply {
                    val likeString = StringUtils.sortDecimal(StringUtils.notNullString(centerBarItem.like))
                    text = likeString.toString()
                    setNumberColor(this, likeString)
                    val like = try {
                        StringUtils.notNullString(centerBarItem.like).toInt()
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

        fun onBind(commentItem: Item.CommentItem) {
            binding.apply {

                articleReadItemCommitTextViewText.apply {
                    StringUtils.TextViewAutoSplitFix(this)
                    movementMethod = TextViewMovementMethod(context)
                    val future = PrecomputedTextCompat.getTextFuture(
                        StringUtils.ColorString(commentItem.text),
                        textMetricsParamsCompat,
                        null
                    )
                    setTextFuture(future)
                }
                articleReadItemCommitTextviewAuth.text = StringUtils.ColorString(commentItem.auth)
            }
        }
    }

    inner class ViewHolderCommitBar(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ArticleReadItemCommitBarBinding.bind(view)

        fun onBind(commentBarItem: Item.CommentBarItem) {
            binding.apply {
                articleReadItemCommitTextViewTime.text = commentBarItem.time
                articleReadItemCommitTextViewFloor.text = commentBarItem.floor
                val colorString = StringUtils.sortDecimal(StringUtils.notNullString(commentBarItem.like))
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
            is Item.HeaderItem -> R.layout.article_read_item_header
            is Item.ContentLineItem -> R.layout.article_read_item_content
            is Item.ImageItem -> R.layout.article_read_item_image
            is Item.CenterBarItem -> R.layout.article_read_item_center_bar
            is Item.CommentItem -> R.layout.article_read_item_commit
            is Item.CommentBarItem -> R.layout.article_read_item_commit_bar
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val item = data[position]
        if (viewHolder is ViewHolderHeader && item is Item.HeaderItem) {
            viewHolder.onBind(item)
        } else if (viewHolder is ViewHolderContent && item is Item.ContentLineItem) {
            viewHolder.onBind(item)
        } else if (viewHolder is ViewHolderComment && item is Item.CommentItem) {
            viewHolder.onBind(item)
            updateBackgroundColor(viewHolder, item.index)
        } else if (viewHolder is ViewHolderCommitBar && item is Item.CommentBarItem) {
            viewHolder.onBind(item)
            updateBackgroundColor(viewHolder, item.index)
        } else if (viewHolder is ViewHolderCenterBar && item is Item.CenterBarItem) {
            viewHolder.onBind(item)
        } else if (viewHolder is ViewHolderContentImage && item is Item.ImageItem) {
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
            val builder = GenericDraweeHierarchyBuilder(draweeView.resources)
            val pf = PointF(0.5f, 0.5f)
            var hierarchy: GenericDraweeHierarchy? = null
            hierarchy = builder.setPressedStateOverlay(
                draweeView.resources.getDrawable(R.drawable.image_click)
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

    sealed class Item {
        data class HeaderItem(
            val title: String,
            val auth: String,
            val date: String,
            val type: String,
            val board: String
        ) : Item()

        data class ContentLineItem(val text: String) : Item()
        data class ImageItem(val index: Int, val url: String) : Item()
        data class CenterBarItem(val like: String, val floor: String) : Item()
        data class CommentItem(val index: Int, val text: String, val auth: String) : Item()
        data class CommentBarItem(val index: Int, val time: String, val floor: String, val like: String) : Item()
    }
}
