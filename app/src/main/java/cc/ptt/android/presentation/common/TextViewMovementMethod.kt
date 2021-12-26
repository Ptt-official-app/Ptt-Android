package cc.ptt.android.presentation.common

import android.content.Context
import android.text.Selection
import android.text.Spannable
import android.text.method.ArrowKeyMovementMethod
import android.text.method.LinkMovementMethod
import android.text.method.MovementMethod
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.view.MotionEvent
import android.widget.TextView
import cc.ptt.android.utils.turnOnUrl
import java.lang.Exception

class TextViewMovementMethod(mContext: Context?) : ArrowKeyMovementMethod() {
    private var sInstance: TextViewMovementMethod? = null
    private val cMethod: MovementMethod = LinkMovementMethod.getInstance()
    private val mContext: Context? = mContext
    override fun initialize(widget: TextView, text: Spannable) {
        cMethod.initialize(widget, text)
    }

    override fun onTouchEvent(widget: TextView, text: Spannable, event: MotionEvent): Boolean {
        // super.onTouchEvent(widget, text, event);
        val action = event.action
        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN) {
            val x = event.x.toInt() - widget.totalPaddingLeft + widget.scrollX
            val y = event.y.toInt() - widget.totalPaddingTop + widget.scrollY
            val layout = widget.layout
            val off = layout.getOffsetForHorizontal(layout.getLineForVertical(y), x.toFloat())
            val link = text.getSpans(off, off, ClickableSpan::class.java)
            if (link.isNotEmpty() && link[0] != null) {
                if (action == MotionEvent.ACTION_UP) {
                    try {
                        val span = link[0] as URLSpan
                        span.url
                        val url = span.url
                        // Log.d("onTVMM","click="+url);
                        turnOnUrl(mContext!!, url)
                    } catch (e: Exception) {
                    }
                }
                if (action == MotionEvent.ACTION_DOWN) {
                    Selection.setSelection(
                        text, text.getSpanStart(link[0]), text.getSpanEnd(link[0])
                    )
                }
                return true
            }
        }
        return false
    }

    fun getInstance(mContext: Context?): MovementMethod {
        sInstance = TextViewMovementMethod(mContext)
        return sInstance!!
    }
}
