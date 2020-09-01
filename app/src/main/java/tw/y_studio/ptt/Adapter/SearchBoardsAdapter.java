package tw.y_studio.ptt.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

import tw.y_studio.ptt.R;
import tw.y_studio.ptt.Utils.StringUtils;

import static android.content.Context.MODE_PRIVATE;

public class SearchBoardsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener,View.OnLongClickListener{

    private List<Map<String, Object>> data;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView_title;
        private AppCompatImageButton like;
        public ViewHolder(View v) {
            super(v);

            mTextView_title=v.findViewById(R.id.search_boards_item_textView_title);
            like=v.findViewById(R.id.search_boards_item_imageView_like);
        }
    }
    public class ViewHolder2 extends RecyclerView.ViewHolder {
        private TextView mTextView_title;
        private TextView mTextView_subtitle;
        private AppCompatImageButton like;
        public ViewHolder2(View v) {
            super(v);

            mTextView_title=v.findViewById(R.id.textView_hot_boards_title);
            mTextView_subtitle=v.findViewById(R.id.textView_hot_boards_subtitle);
            like=v.findViewById(R.id.search_boards_item_imageView_like);
        }
    }

    private Context context;
    public SearchBoardsAdapter(Context context, List<Map<String, Object>> items) {
        this.data = items;
        this.context=context;
        inflater = LayoutInflater.from(context);
    }

    private LayoutInflater inflater;
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType){
            case 0:
                View view1 = inflater.inflate(R.layout.search_boards_item, parent, false);
                view1.setOnClickListener(this);
                view1.setOnLongClickListener(this);
                ViewHolder holder1 = new ViewHolder(view1);
                return holder1;
            case 1:
                View view2 = inflater.inflate(R.layout.search_boards_item_2, parent, false);
                view2.setOnClickListener(this);
                view2.setOnLongClickListener(this);
                ViewHolder2 holder2 = new ViewHolder2(view2);
                return holder2;
            default:

        }

        return null;
    }



    @Override
    public int getItemViewType(int position) {
        int pos=context.getSharedPreferences(
                "MainSetting", MODE_PRIVATE).getInt("SEARCHSTYLE",0);
        return pos;
    }
    private View.OnClickListener likeOnClickListener;
    public void setLikeOnClickListener(View.OnClickListener likeOnClickListener){
        this.likeOnClickListener = likeOnClickListener;

    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holderO, final int position) {
        holderO.itemView.setTag(position);
        if(holderO instanceof ViewHolder){
            ViewHolder holder=(ViewHolder) holderO;
            holder.mTextView_title.setText(StringUtils.notNullString(data.get(position).get("title")));
            StringUtils.TextViewAutoSplitFix(holder.mTextView_title);
            holder.like.setTag(position);
            boolean isLike  = (boolean)data.get(position).get("like");
            if(isLike){
                holder.like.setColorFilter(context.getResources().getColor(R.color.tangerine));
            }else {
                holder.like.setColorFilter(context.getResources().getColor(R.color.slateGrey));
            }
            holder.like.setOnClickListener(likeOnClickListener);
        }else if(holderO instanceof ViewHolder2){
            ViewHolder2 holder=(ViewHolder2) holderO;
            holder.mTextView_title.setText(StringUtils.notNullString(data.get(position).get("title")));
            holder.mTextView_subtitle.setText(StringUtils.notNullString(data.get(position).get("subtitle")));
            StringUtils.TextViewAutoSplitFix(holder.mTextView_title);
            holder.like.setTag(position);
            boolean isLike  = (boolean)data.get(position).get("like");
            if(isLike){
                holder.like.setColorFilter(context.getResources().getColor(R.color.tangerine));
            }else {
                holder.like.setColorFilter(context.getResources().getColor(R.color.slateGrey));
            }
            holder.like.setOnClickListener(likeOnClickListener);
        }



    }


    @Override
    public int getItemCount() {
        return data.size();
    }


    //define interface
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    public interface OnItemLongClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener = null;
    private OnItemLongClickListener mOnItemLongClickListener = null;
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.mOnItemLongClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v,(int)v.getTag());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (mOnItemLongClickListener != null) {
            mOnItemLongClickListener.onItemClick(v,(int)v.getTag());
            return true;
        }
        return false;
    }



}
