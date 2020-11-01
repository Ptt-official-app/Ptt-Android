package tw.y_studio.ptt.UI;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import androidx.annotation.ColorInt;

import com.facebook.drawee.drawable.DrawableUtils;

public class ImageLoadingDrawable extends Drawable {
    private Paint mRingBackgroundPaint;
    private int mRingBackgroundColor;

    private Paint mRingPaint;

    private int mRingColor;

    private float mRadius;

    private float mRingRadius;

    private float mStrokeWidth;

    private int mXCenter;

    private int mYCenter;

    private int mTotalProgress = 10000;

    private int mProgress;

    public ImageLoadingDrawable() {
        initAttrs();
    }

    public ImageLoadingDrawable(@ColorInt int RingBackgroundColor, @ColorInt int RingColor) {
        initAttrs();
        mRingBackgroundColor = RingBackgroundColor;
        mRingColor = RingColor;
    }

    public ImageLoadingDrawable(float size) {
        this.size = size;
        initAttrs();
    }

    private float size = 1;

    private void initAttrs() {
        mRadius = (int) (StaticValue.widthPixels / 30d * size);
        mStrokeWidth = (int) (mRadius / 10);
        // mRingBackgroundColor = 0xFF4F4F4F;
        mRingBackgroundColor = Color.GRAY;
        // mRingColor = 0xFF4F4F4F;
        mRingColor = Color.WHITE;
        mRingRadius = mRadius + mStrokeWidth / 2;
        initVariable();
    }

    private void initVariable() {
        mRingBackgroundPaint = new Paint();
        mRingBackgroundPaint.setAntiAlias(true);
        mRingBackgroundPaint.setColor(mRingBackgroundColor);
        mRingBackgroundPaint.setStyle(Paint.Style.STROKE);
        mRingBackgroundPaint.setStrokeWidth(mStrokeWidth);

        mRingPaint = new Paint();
        mRingPaint.setAntiAlias(true);
        mRingPaint.setColor(mRingColor);
        mRingPaint.setStyle(Paint.Style.STROKE);
        mRingPaint.setStrokeWidth(mStrokeWidth);
    }

    @Override
    public void draw(Canvas canvas) {
        drawBar(canvas, mTotalProgress, mRingBackgroundPaint);
        drawBar(canvas, mProgress, mRingPaint);
    }

    private void drawBar(Canvas canvas, int level, Paint paint) {
        if (level >= 0) {
            Rect bound = getBounds();
            mXCenter = bound.centerX();
            mYCenter = bound.centerY();
            RectF oval = new RectF();
            oval.left = (mXCenter - mRingRadius);
            oval.top = (mYCenter - mRingRadius);
            oval.right = mRingRadius * 2 + (mXCenter - mRingRadius);
            oval.bottom = mRingRadius * 2 + (mYCenter - mRingRadius);
            canvas.drawArc(oval, -90, ((float) level / mTotalProgress) * 360, false, paint); //
        }
    }

    @Override
    protected boolean onLevelChange(int level) {
        mProgress = (int) ((double) level / (1));
        if (level > 0 && level < 10000) {
            invalidateSelf();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void setAlpha(int alpha) {
        mRingPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mRingPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return DrawableUtils.getOpacityFromColor(this.mRingPaint.getColor());
    }
}
