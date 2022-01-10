package cc.ptt.android.presentation.common.dragitemmove

import androidx.recyclerview.widget.RecyclerView

interface StartDragListener {
    fun requestDrag(viewHolder: RecyclerView.ViewHolder?)
}
