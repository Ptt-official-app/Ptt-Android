package tw.y_studio.ptt.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

import tw.y_studio.ptt.Ptt.PttColor;
import tw.y_studio.ptt.R;
import tw.y_studio.ptt.Utils.StringUtils;

public class HotBoardsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener,View.OnLongClickListener{

    private List<Map<String, Object>> data;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView_title;
        private TextView mTextView_subtitle;
        private TextView mTextView_onlinePeople;
        //private TextView mTextView_color;
        private AppCompatImageButton person;
        public ViewHolder(View v) {
            super(v);
            mTextView_subtitle=v.findViewById(R.id.textView_hot_boards_subtitle);
            mTextView_title=v.findViewById(R.id.textView_hot_boards_title);
            mTextView_onlinePeople=v.findViewById(R.id.textView_hot_boards_online);
            //mTextView_color=v.findViewById(R.id.textView_hot_boards_color);
            person = v.findViewById(R.id.hot_boards_online_imageButton_person);
        }
    }

    public class ViewHolderEdit extends RecyclerView.ViewHolder {
        private TextView mTextView_title;
        private TextView mTextView_subtitle;
        private TextView mTextView_onlinePeople;
        private AppCompatImageButton unfav;
        private AppCompatImageButton drag;
        //private TextView mTextView_color;
        //private AppCompatImageButton person;
        public ViewHolderEdit(View v) {
            super(v);
            mTextView_subtitle=v.findViewById(R.id.textView_hot_boards_subtitle);
            mTextView_title=v.findViewById(R.id.textView_hot_boards_title);
            mTextView_onlinePeople=v.findViewById(R.id.textView_hot_boards_online);
            unfav = v.findViewById(R.id.hot_boards_online_imageButton_unfav);
            drag = v.findViewById(R.id.hot_boards_online_imageButton_drag);
            //mTextView_color=v.findViewById(R.id.textView_hot_boards_color);
            //person = v.findViewById(R.id.hot_boards_online_imageButton_person);
        }
    }

    private Context context;
    public HotBoardsListAdapter(Context context, List<Map<String, Object>> items) {
        this.data = items;
        this.context=context;
        inflater = LayoutInflater.from(context);
    }

    private LayoutInflater inflater;
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType){
            case 0:
                View view1 = inflater.inflate(R.layout.hot_boards_list_item, parent, false);
                view1.setOnClickListener(this);
                view1.setOnLongClickListener(this);
                ViewHolder holder1 = new ViewHolder(view1);
                return holder1;
            case 1:
                View view2 = inflater.inflate(R.layout.hot_boards_list_item_edit, parent, false);
                view2.setOnClickListener(this);
                view2.setOnLongClickListener(this);
                ViewHolder holder2 = new ViewHolder(view2);
                return holder2;
            default:

        }

        return null;
    }


    private boolean editMode = false;

    @Override
    public int getItemViewType(int position) {
        int pos=0;
        if(editMode){
            pos=1;
        }

        return pos;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holderO, final int position) {
        holderO.itemView.setTag(position);
        if(holderO instanceof ViewHolder){
            ViewHolder holder=(ViewHolder) holderO;

            holder.mTextView_title.setText(StringUtils.notNullString(data.get(position).get("title")));
            holder.mTextView_subtitle.setText(StringUtils.notNullString(data.get(position).get("subtitle")));
            holder.mTextView_onlinePeople.setText(StringUtils.notNullString(data.get(position).get("online")));
            //holder.mTextView_color.setTextColor(PttColor.ColorTrans(StringUtils.notNullString(data.get(position).get("onlineColor"))));
            //holder.mTextView_color.setTypeface(TypefaceUI.getInstance().setContext(((Activity)context).getApplication()).getBepttFont());
            holder.person.setColorFilter(PttColor.ColorTrans(StringUtils.notNullString(data.get(position).get("onlineColor"))));

        }else if(holderO instanceof ViewHolderEdit){
            ViewHolderEdit holder=(ViewHolderEdit) holderO;

            holder.mTextView_title.setText(StringUtils.notNullString(data.get(position).get("title")));
            holder.mTextView_subtitle.setText(StringUtils.notNullString(data.get(position).get("subtitle")));
            holder.mTextView_onlinePeople.setText(StringUtils.notNullString(data.get(position).get("online")));
            //holder.mTextView_color.setTextColor(PttColor.ColorTrans(StringUtils.notNullString(data.get(position).get("onlineColor"))));
            //holder.mTextView_color.setTypeface(TypefaceUI.getInstance().setContext(((Activity)context).getApplication()).getBepttFont());
            //holder.person.setColorFilter(PttColor.ColorTrans(StringUtils.notNullString(data.get(position).get("onlineColor"))));

        }



    }


    @Override
    public int getItemCount() {
        return data.size();
    }


    //define interface
    public interface OnItemClickListener {
        void onItemClick(View view , int position);
    }
    public interface OnItemLongClickListener {
        void onItemClick(View view , int position);
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
