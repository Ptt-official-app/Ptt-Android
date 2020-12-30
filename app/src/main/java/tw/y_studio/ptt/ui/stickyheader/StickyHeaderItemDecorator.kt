package tw.y_studio.ptt.ui.stickyheader

import android.graphics.Canvas
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class StickyHeaderItemDecorator(
    private val adapter: StickyAdapter<RecyclerView.ViewHolder?, RecyclerView.ViewHolder?>
) : ItemDecoration() {

    private var currentStickyPosition = RecyclerView.NO_POSITION
    private var recyclerView: RecyclerView? = null
    private var currentStickyHolder: RecyclerView.ViewHolder? = null
    private var lastViewOverlappedByHeader: View? = null

    var stickyHolderHeight = 0

    fun attachToRecyclerView(recyclerView: RecyclerView?) {
        if (this.recyclerView === recyclerView) {
            return
        }
        if (this.recyclerView != null) {
            destroyCallbacks(this.recyclerView)
        }
        this.recyclerView = recyclerView
        if (recyclerView != null) {
            currentStickyHolder = adapter.onCreateHeaderViewHolder(recyclerView)
            fixLayoutSize()
            setupCallbacks()
        }
    }

    private fun setupCallbacks() {
        recyclerView?.addItemDecoration(this)
    }

    private fun destroyCallbacks(recyclerView: RecyclerView?) {
        recyclerView?.removeItemDecoration(this)
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        val layoutManager = parent.layoutManager ?: return
        var topChildPosition = RecyclerView.NO_POSITION
        if (layoutManager is LinearLayoutManager) {
            topChildPosition = layoutManager.findFirstVisibleItemPosition()
        } else {
            val topChild = parent.getChildAt(0)
            if (topChild != null) {
                topChildPosition = parent.getChildAdapterPosition(topChild)
            }
        }
        if (topChildPosition == RecyclerView.NO_POSITION) {
            return
        }

        val viewOverlappedByHeader = getChildInContact(parent, currentStickyHolder!!.itemView.bottom)
        lastViewOverlappedByHeader =
            viewOverlappedByHeader ?: lastViewOverlappedByHeader ?: parent.getChildAt(topChildPosition)
        val overlappedByHeaderPosition = parent.getChildAdapterPosition(viewOverlappedByHeader!!)
        val overlappedHeaderPosition: Int
        val preOverlappedPosition: Int
        if (overlappedByHeaderPosition > 0) {
            preOverlappedPosition = adapter.getHeaderPositionForItem(overlappedByHeaderPosition - 1)
            overlappedHeaderPosition = adapter.getHeaderPositionForItem(overlappedByHeaderPosition)
        } else {
            preOverlappedPosition = adapter.getHeaderPositionForItem(topChildPosition)
            overlappedHeaderPosition = preOverlappedPosition
        }
        if (preOverlappedPosition == RecyclerView.NO_POSITION) {
            return
        }
        if (preOverlappedPosition != overlappedHeaderPosition && shouldMoveHeader(viewOverlappedByHeader)) {
            updateStickyHeader(topChildPosition, overlappedByHeaderPosition)
            moveHeader(c, viewOverlappedByHeader)
        } else {
            updateStickyHeader(topChildPosition, RecyclerView.NO_POSITION)
            drawHeader(c)
        }
    }

    // shouldMoveHeader returns the sticky header should move or not.
    // This method is for avoiding sinking/departing the sticky header into/from top of screen
    private fun shouldMoveHeader(viewOverlappedByHeader: View?): Boolean {
        return viewOverlappedByHeader?.let {
            val dy = it.top - it.height
            it.top >= 0 && dy <= 0
        } ?: false
    }

    private fun updateStickyHeader(
        topChildPosition: Int,
        @Suppress("UNUSED_PARAMETER") contactChildPosition: Int
    ) {
        val headerPositionForItem = adapter.getHeaderPositionForItem(topChildPosition)
        if (headerPositionForItem != currentStickyPosition && headerPositionForItem != RecyclerView.NO_POSITION) {
            adapter.onBindHeaderViewHolder(currentStickyHolder, headerPositionForItem)
            currentStickyPosition = headerPositionForItem
        } else if (headerPositionForItem != RecyclerView.NO_POSITION) {
            adapter.onBindHeaderViewHolder(currentStickyHolder, headerPositionForItem)
        }
    }

    private fun drawHeader(c: Canvas) {
        currentStickyHolder?.let { viewHolder ->
            c.save()
            c.translate(0f, 0f)
            viewHolder.itemView.draw(c)
            c.restore()
        }
    }

    private fun moveHeader(c: Canvas, nextHeader: View) {
        currentStickyHolder?.let { viewHolder ->
            c.save()
            c.translate(0f, (nextHeader.top - nextHeader.height).toFloat())
            viewHolder.itemView.draw(c)
            c.restore()
        }
    }

    private fun getChildInContact(parent: RecyclerView, contactPoint: Int): View? {
        var childInContact: View? = null
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            if (child.bottom > contactPoint) {
                if (child.top <= contactPoint) {
                    // This child overlaps the contactPoint
                    childInContact = child
                    break
                }
            }
        }
        return childInContact
    }

    private fun fixLayoutSize() {
        recyclerView
            ?.viewTreeObserver
            ?.addOnGlobalLayoutListener(
                object : OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        recyclerView!!
                            .viewTreeObserver
                            .removeOnGlobalLayoutListener(this)
                        // Specs for parent (RecyclerView)
                        val widthSpec = View.MeasureSpec.makeMeasureSpec(
                            recyclerView!!.width, View.MeasureSpec.EXACTLY
                        )
                        val heightSpec = View.MeasureSpec.makeMeasureSpec(
                            recyclerView!!.height,
                            View.MeasureSpec.UNSPECIFIED
                        )

                        // Specs for children (headers)
                        val childWidthSpec = ViewGroup.getChildMeasureSpec(
                            widthSpec,
                            recyclerView!!.paddingLeft +
                                recyclerView!!.paddingRight,
                            currentStickyHolder!!.itemView.layoutParams.width
                        )
                        val childHeightSpec = ViewGroup.getChildMeasureSpec(
                            heightSpec,
                            recyclerView!!.paddingTop +
                                recyclerView!!.paddingBottom,
                            currentStickyHolder!!.itemView.layoutParams.height
                        )
                        currentStickyHolder!!.itemView.measure(
                            childWidthSpec, childHeightSpec
                        )
                        currentStickyHolder!!.itemView.layout(
                            0,
                            0,
                            currentStickyHolder!!.itemView.measuredWidth,
                            currentStickyHolder!!.itemView.measuredHeight
                        )
                        stickyHolderHeight = currentStickyHolder!!.itemView.measuredHeight
                    }
                })
    }
}
