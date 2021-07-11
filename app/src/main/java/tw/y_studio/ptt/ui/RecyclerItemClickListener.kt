package tw.y_studio.ptt.ui

import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SimpleOnItemTouchListener
import tw.y_studio.ptt.ui.stickyheader.StickyHeaderItemDecorator

class RecyclerItemClickListener : SimpleOnItemTouchListener {

    private var clickListener: OnItemClickListener? = null

    private var gestureDetector:
        GestureDetectorCompat? = null

    interface OnItemClickListener {
        fun onItemClick(view: View?, position: Int)
        fun onItemLongClick(view: View?, position: Int)
    }

    constructor(
        recyclerView: RecyclerView,
        listener: OnItemClickListener?
    ) {
        clickListener = listener
        gestureDetector = GestureDetectorCompat(
            recyclerView.context,
            object : SimpleOnGestureListener() {
                override fun onSingleTapUp(e: MotionEvent): Boolean {
                    return true
                }

                override fun onLongPress(e: MotionEvent) {
                    val childView = recyclerView.findChildViewUnder(e.x, e.y)
                    if (childView != null && clickListener != null) {
                        clickListener!!.onItemLongClick(
                            childView,
                            recyclerView.getChildAdapterPosition(childView)
                        )
                    }
                }
            }
        )
    }

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        if (decorator != null) {
            val childView = rv.findChildViewUnder(e.x, e.y)
            if (childView != null && clickListener != null && gestureDetector!!.onTouchEvent(e)) {
                if (e.y > 0 && e.y > decorator!!.stickyHolderHeight) {
                    clickListener!!.onItemClick(childView, rv.getChildAdapterPosition(childView))
                } else {
                    clickListener!!.onItemClick(childView, 0)
                }
                return true
            }
        } else {
            val childView = rv.findChildViewUnder(e.x, e.y)
            if (childView != null && clickListener != null && gestureDetector!!.onTouchEvent(e)) {
                clickListener!!.onItemClick(childView, rv.getChildAdapterPosition(childView))
                return true
            }
        }
        return super.onInterceptTouchEvent(rv, e)
    }

    private var decorator: StickyHeaderItemDecorator? = null
    fun setDecorator(decorator: StickyHeaderItemDecorator?) {
        this.decorator = decorator
    }
}
