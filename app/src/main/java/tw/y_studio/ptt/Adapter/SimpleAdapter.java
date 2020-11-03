package tw.y_studio.ptt.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import tw.y_studio.ptt.R;

import java.util.List;
import java.util.Map;

public class SimpleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements View.OnClickListener, View.OnLongClickListener {
    private List<Map<String, Object>> Data;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv11;
        private TextView tv12;

        public ViewHolder(View convertView) {
            super(convertView);
            tv11 = (TextView) convertView.findViewById(R.id.textView);
            tv12 = (TextView) convertView.findViewById(R.id.textView2);
        }
    }

    private LayoutInflater inflater;

    public SimpleAdapter(Context context, List<Map<String, Object>> Data) {
        this.Data = Data;
        inflater = LayoutInflater.from(context);
        // li = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            default:
            case 0:
                View view1 = inflater.inflate(R.layout.item_simple, parent, false);
                view1.setOnClickListener(this);
                view1.setOnLongClickListener(this);
                ViewHolder holder1 = new ViewHolder(view1);
                return holder1;
        }
    }

    @Override
    public int getItemViewType(int position) {
        int pos = 0;
        return pos;
    }

    private String checkNull(String in) {
        if (in == null) return "";
        return in;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holderO, final int position) {
        holderO.itemView.setTag(position);

        if (holderO instanceof ViewHolder) {
            ViewHolder holder = (ViewHolder) holderO;

            holder.tv11.setText(checkNull((String) Data.get(position).get("title")));
            // holder.tv11.setTextColor(ColorSave.Color_Title);
            String mess = (String) Data.get(position).get("subtitle");
            if (mess == null) {
                holder.tv12.setVisibility(View.GONE);
            } else {
                holder.tv12.setVisibility(View.VISIBLE);
            }
            holder.tv12.setText(checkNull((String) Data.get(position).get("subtitle")));
            // holder.tv12.setTextColor(ColorSave.Color_subTitle);

        }
    }

    @Override
    public int getItemCount() {
        return Data.size();
    }

    // define interface
    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public static interface OnItemLongClickListener {
        boolean onItemClick(View view, int position);
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
            // 注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (mOnItemLongClickListener != null) {
            // 注意这里使用getTag方法获取position

            return mOnItemLongClickListener.onItemClick(v, (int) v.getTag());
        }
        return false;
    }
}
