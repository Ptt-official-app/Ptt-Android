package tw.y_studio.ptt.ui.dragitemmove

import androidx.recyclerview.widget.RecyclerView

interface StartDragListener {
    fun requestDrag(viewHolder: RecyclerView.ViewHolder?)
}
