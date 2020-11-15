package tw.y_studio.ptt.ui;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.RecyclerView;

import tw.y_studio.ptt.ui.stickyheader.StickyHeaderItemDecorator;

public class RecyclerItemClickListener extends RecyclerView.SimpleOnItemTouchListener {
    // public class RecyclerItemClickListener extends RecyclerView.OnItemTouchListener {

    private OnItemClickListener clickListener;
    //    private GestureDetector gestureDetector;
    private GestureDetectorCompat gestureDetector; // v4 兼容包中

    public RecyclerItemClickListener() {}

    public interface OnItemClickListener {
        /**
         * 点击时回调
         *
         * @param view 点击的View
         * @param position 点击的位置
         */
        void onItemClick(View view, int position);

        /**
         * 长点击时回调
         *
         * @param view 点击的View
         * @param position 点击的位置
         */
        void onItemLongClick(View view, int position);
    }

    public RecyclerItemClickListener(
            final RecyclerView recyclerView, OnItemClickListener listener) {
        this.clickListener = listener;
        gestureDetector =
                new GestureDetectorCompat(
                        recyclerView.getContext(),
                        new GestureDetector.SimpleOnGestureListener() {

                            @Override
                            public boolean onSingleTapUp(MotionEvent e) {
                                return true;
                            }

                            @Override
                            public void onLongPress(MotionEvent e) {
                                View childView =
                                        recyclerView.findChildViewUnder(e.getX(), e.getY());
                                if (childView != null && clickListener != null) {
                                    clickListener.onItemLongClick(
                                            childView,
                                            recyclerView.getChildAdapterPosition(childView));
                                }
                            }
                        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        // gestureDetector.setIsLongpressEnabled(true);
        // if(e.getAction() == MotionEvent.ACTION_DOWN || e.getAction() == MotionEvent.ACTION_UP)
        if (decorator != null) {
            // Log.d("onRecyclerClick","decorator!=null");
            // Log.d("onRecyclerClick","decorator.getStickyHolderHight() =
            // "+decorator.getStickyHolderHight());
            // Log.d("onRecyclerClick","e.getY() = "+e.getY());

            View childView = rv.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                if (e.getY() > 0 && e.getY() > decorator.getStickyHolderHight()) {
                    clickListener.onItemClick(childView, rv.getChildAdapterPosition(childView));
                } else {
                    clickListener.onItemClick(childView, 0);
                }
                return true;
            }
        } else {
            // Log.d("onRecyclerClick","decorator==null");
            View childView = rv.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onItemClick(childView, rv.getChildAdapterPosition(childView));
                return true;
            }
        }

        return super.onInterceptTouchEvent(rv, e);
    }

    private StickyHeaderItemDecorator decorator;

    public void setDecorator(StickyHeaderItemDecorator decorator) {
        this.decorator = decorator;
    }
}
