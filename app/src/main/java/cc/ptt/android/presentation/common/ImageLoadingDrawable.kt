package cc.ptt.android.presentation.common

import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import com.facebook.drawee.drawable.DrawableUtils

class ImageLoadingDrawable : Drawable {
    private var mRingBackgroundPaint: Paint? = null
    private var mRingBackgroundColor = 0
    private var mRingPaint: Paint? = null
    private var mRingColor = 0
    private var mRadius = 0f
    private var mRingRadius = 0f
    private var mStrokeWidth = 0f
    private var mXCenter = 0
    private var mYCenter = 0
    private val mTotalProgress = 10000
    private var mProgress = 0

    constructor() {
        initAttrs()
    }

    constructor(@ColorInt RingBackgroundColor: Int, @ColorInt RingColor: Int) {
        initAttrs()
        mRingBackgroundColor = RingBackgroundColor
        mRingColor = RingColor
    }

    constructor(size: Float) {
        this.size = size
        initAttrs()
    }

    private var size = 1f
    private fun initAttrs() {
        mRadius = (StaticValue.widthPixels / 30.0 * size).toFloat()
        mStrokeWidth = (mRadius / 10)
        // mRingBackgroundColor = 0xFF4F4F4F;
        mRingBackgroundColor = Color.GRAY
        // mRingColor = 0xFF4F4F4F;
        mRingColor = Color.WHITE
        mRingRadius = mRadius + mStrokeWidth / 2
        initVariable()
    }

    private fun initVariable() {
        mRingBackgroundPaint = Paint()
        mRingBackgroundPaint!!.isAntiAlias = true
        mRingBackgroundPaint!!.color = mRingBackgroundColor
        mRingBackgroundPaint!!.style = Paint.Style.STROKE
        mRingBackgroundPaint!!.strokeWidth = mStrokeWidth
        mRingPaint = Paint()
        mRingPaint!!.isAntiAlias = true
        mRingPaint!!.color = mRingColor
        mRingPaint!!.style = Paint.Style.STROKE
        mRingPaint!!.strokeWidth = mStrokeWidth
    }

    override fun draw(canvas: Canvas) {
        drawBar(canvas, mTotalProgress, mRingBackgroundPaint)
        drawBar(canvas, mProgress, mRingPaint)
    }

    private fun drawBar(canvas: Canvas, level: Int, paint: Paint?) {
        if (level >= 0) {
            val bound = bounds
            mXCenter = bound.centerX()
            mYCenter = bound.centerY()
            val oval = RectF()
            oval.left = mXCenter - mRingRadius
            oval.top = mYCenter - mRingRadius
            oval.right = mRingRadius * 2 + (mXCenter - mRingRadius)
            oval.bottom = mRingRadius * 2 + (mYCenter - mRingRadius)
            canvas.drawArc(oval, -90f, level.toFloat() / mTotalProgress * 360, false, paint!!) //
        }
    }

    override fun onLevelChange(level: Int): Boolean {
        mProgress = (level.toDouble() / 1).toInt()
        return if (level in 1..9999) {
            invalidateSelf()
            true
        } else {
            false
        }
    }

    override fun setAlpha(alpha: Int) {
        mRingPaint!!.alpha = alpha
    }

    override fun setColorFilter(cf: ColorFilter?) {
        mRingPaint!!.colorFilter = cf
    }

    override fun getOpacity(): Int {
        return DrawableUtils.getOpacityFromColor(mRingPaint!!.color)
    }
}
