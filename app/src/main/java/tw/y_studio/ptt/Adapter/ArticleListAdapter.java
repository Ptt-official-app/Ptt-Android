package tw.y_studio.ptt.Adapter;

import android.app.Activity;
import android.content.Context;
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

import tw.y_studio.ptt.Ptt.PttColor;
import tw.y_studio.ptt.R;
import tw.y_studio.ptt.UI.TypefaceUI;
import tw.y_studio.ptt.Utils.StringUtils;

public class ArticleListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener,View.OnLongClickListener{

    private List<Map<String, Object>> data;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView_title;
        private TextView mTextView_class;
        private TextView mTextView_auth;
        private TextView mTextView_like;
        private TextView mTextView_commit;
        private TextView mTextView_date;
        private AppCompatImageButton like;
        private AppCompatImageButton dislike;
        private AppCompatImageButton more;
        private LinearLayout main;
        public ViewHolder(View v) {
            super(v);

            mTextView_title=v.findViewById(R.id.article_list_item_textView_title);
            mTextView_class=v.findViewById(R.id.article_list_item_textView_class);
            mTextView_auth=v.findViewById(R.id.article_list_item_textView_auth);
            mTextView_like=v.findViewById(R.id.article_list_item_textView_like);
            mTextView_commit=v.findViewById(R.id.article_list_item_textView_commit);
            mTextView_date=v.findViewById(R.id.article_list_item_textView_date);
            like=v.findViewById(R.id.article_list_item_imageButton_like);
            dislike=v.findViewById(R.id.article_list_item_imageButton_dislike);
            more=v.findViewById(R.id.article_list_item_imageButton_more);
            main=v.findViewById(R.id.article_list_item_main);
        }
    }
    public class ViewHolderDele extends RecyclerView.ViewHolder {
        private TextView mTextView_title;
        private LinearLayout main;
        public ViewHolderDele(View v) {
            super(v);
            mTextView_title=v.findViewById(R.id.article_list_item_textView_title);
            main=v.findViewById(R.id.article_list_item_main);
        }
    }
    private Context context;
    public ArticleListAdapter(Context context, List<Map<String, Object>> items) {
        this.data = items;
        this.context=context;
        inflater = LayoutInflater.from(context);
    }

    private String highLightUrl = "";
    public void setHighLightUrl(String url){
        highLightUrl=url;
    }
    private LayoutInflater inflater;
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType){
            case 0:
                View view1 = inflater.inflate(R.layout.article_list_item, parent, false);
                view1.setOnClickListener(this);
                view1.setOnLongClickListener(this);
                ViewHolder holder1 = new ViewHolder(view1);
                return holder1;
            case 1:
                View view2 = inflater.inflate(R.layout.article_list_item_delete, parent, false);
                //view1.setOnClickListener(this);
                //view1.setOnLongClickListener(this);
                ViewHolderDele holder2 = new ViewHolderDele(view2);
                return holder2;
            default:

        }

        return null;
    }



    @Override
    public int getItemViewType(int position) {
        int pos=0;
        if((boolean)data.get(position).get("deleted")){
            pos=1;
        }

        return pos;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holderO, final int position) {
        holderO.itemView.setTag(position);
        if(holderO instanceof ViewHolder){
            ViewHolder holder=(ViewHolder) holderO;

            StringUtils.TextViewAutoSplitFix(holder.mTextView_title);
            holder.mTextView_title.setText(StringUtils.notNullString(data.get(position).get("title")));
            holder.mTextView_date.setText(StringUtils.notNullString(data.get(position).get("date")));
            holder.mTextView_class.setText(StringUtils.notNullString(data.get(position).get("class")));
            holder.mTextView_commit.setText(StringUtils.notNullString(data.get(position).get("commit")));
            holder.mTextView_like.setText(StringUtils.notNullString(data.get(position).get("like")));
            holder.mTextView_auth.setText(StringUtils.notNullString(data.get(position).get("auth")));

            if(position%2==0){
                TypedValue typedValue = new TypedValue();
                Resources.Theme theme = context.getTheme();
                theme.resolveAttribute(R.attr.darkGreyTwo, typedValue, true);
                @ColorInt int color = typedValue.data;
                holder.main.setBackgroundColor(color);
            }else{
                //holder.main.setBackgroundResource(R.color.black);
                TypedValue typedValue = new TypedValue();
                Resources.Theme theme = context.getTheme();
                theme.resolveAttribute(R.attr.black, typedValue, true);
                @ColorInt int color = typedValue.data;
                holder.main.setBackgroundColor(color);
            }
            if(StringUtils.notNullString(data.get(position).get("url")).equals(highLightUrl)){
                TypedValue typedValue = new TypedValue();
                Resources.Theme theme = context.getTheme();
                theme.resolveAttribute(R.attr.tangerine, typedValue, true);
                @ColorInt int color = typedValue.data;
                holder.mTextView_title.setTextColor(color);
            } else {
                if((boolean)data.get(position).get("readed")){
                    TypedValue typedValue = new TypedValue();
                    Resources.Theme theme = context.getTheme();
                    theme.resolveAttribute(R.attr.blueGrey, typedValue, true);
                    @ColorInt int color = typedValue.data;
                    holder.mTextView_title.setTextColor(color);
                }else {
                    TypedValue typedValue = new TypedValue();
                    Resources.Theme theme = context.getTheme();
                    theme.resolveAttribute(R.attr.paleGrey, typedValue, true);
                    @ColorInt int color = typedValue.data;
                    holder.mTextView_title.setTextColor(color);
                }

            }




        }else if(holderO instanceof ViewHolderDele){
            ViewHolderDele holder=(ViewHolderDele) holderO;

            holder.mTextView_title.setText(StringUtils.notNullString(data.get(position).get("title")));
            if(position%2==0){
                TypedValue typedValue = new TypedValue();
                Resources.Theme theme = context.getTheme();
                theme.resolveAttribute(R.attr.darkGreyTwo, typedValue, true);
                @ColorInt int color = typedValue.data;
                holder.main.setBackgroundColor(color);
            }else{
                //holder.main.setBackgroundResource(R.color.black);
                TypedValue typedValue = new TypedValue();
                Resources.Theme theme = context.getTheme();
                theme.resolveAttribute(R.attr.black, typedValue, true);
                @ColorInt int color = typedValue.data;
                holder.main.setBackgroundColor(color);
            }





        }



    }


    @Override
    public int getItemCount() {
        return data.size();
    }


    //define interface
    public interface OnItemClickListener {
        void onItemClick(View view, int position) throws Exception;
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
            try {
                mOnItemClickListener.onItemClick(v,(int)v.getTag());
            } catch (Exception e) {
                e.printStackTrace();
            }
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
