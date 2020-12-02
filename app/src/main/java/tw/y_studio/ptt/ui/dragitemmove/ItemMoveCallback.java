package tw.y_studio.ptt.ui.dragitemmove;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import tw.y_studio.ptt.adapter.FavoriteBoardsListAdapter;

public class ItemMoveCallback extends ItemTouchHelper.Callback {

    private final ItemTouchHelperContract mAdapter;

    public ItemMoveCallback(ItemTouchHelperContract adapter) {
        mAdapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {}

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        return makeMovementFlags(dragFlags, 0);
    }

    private void changeTag(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof FavoriteBoardsListAdapter.ViewHolderEdit) {
            FavoriteBoardsListAdapter.ViewHolderEdit view =
                    (FavoriteBoardsListAdapter.ViewHolderEdit) viewHolder;
            view.getUnfav().setTag(position);
        }
    }

    @Override
    public boolean onMove(
            RecyclerView recyclerView,
            RecyclerView.ViewHolder viewHolder,
            RecyclerView.ViewHolder target) {
        viewHolder.itemView.setTag(target.getAdapterPosition());
        target.itemView.setTag(viewHolder.getAdapterPosition());
        // changeTag(viewHolder,target.getAdapterPosition());
        // changeTag(target,viewHolder.getAdapterPosition());

        mAdapter.onRowMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        // viewHolder.itemView.setTag(target.getAdapterPosition());
        // target.itemView.setTag(viewHolder.getAdapterPosition());
        return true;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {

        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder instanceof FavoriteBoardsListAdapter.ViewHolderEdit) {
                FavoriteBoardsListAdapter.ViewHolderEdit myViewHolder =
                        (FavoriteBoardsListAdapter.ViewHolderEdit) viewHolder;
                mAdapter.onRowSelected(myViewHolder);
            }
        }

        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        if (viewHolder instanceof FavoriteBoardsListAdapter.ViewHolderEdit) {
            FavoriteBoardsListAdapter.ViewHolderEdit myViewHolder =
                    (FavoriteBoardsListAdapter.ViewHolderEdit) viewHolder;
            mAdapter.onRowClear(myViewHolder);
        }
    }

    public interface ItemTouchHelperContract {

        void onRowMoved(int fromPosition, int toPosition);

        void onRowSelected(FavoriteBoardsListAdapter.ViewHolderEdit myViewHolder);

        void onRowClear(FavoriteBoardsListAdapter.ViewHolderEdit myViewHolder);
    }
}
