package tw.y_studio.ptt.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.PointF;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.text.PrecomputedTextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.image.QualityInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import tw.y_studio.ptt.R;
import tw.y_studio.ptt.ui.ImageLoadingDrawable;
import tw.y_studio.ptt.ui.StaticValue;
import tw.y_studio.ptt.ui.TextViewMovementMethod;
import tw.y_studio.ptt.utils.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

public class ArticleReadAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements View.OnClickListener, View.OnLongClickListener {
    private List<Map<String, Object>> data;

    public class ViewHolderHeader extends RecyclerView.ViewHolder {
        private TextView mTextView_title;
        private TextView mTextView_board;
        private TextView mTextView_auth;
        private TextView mTextView_date;
        private AppCompatImageButton back;

        public ViewHolderHeader(View v) {
            super(v);
            mTextView_title = v.findViewById(R.id.article_read_item_header_textView_title);
            mTextView_board = v.findViewById(R.id.article_read_item_header_textView_board);
            mTextView_auth = v.findViewById(R.id.article_read_item_header_textView_auth);
            mTextView_date = v.findViewById(R.id.article_read_item_header_textView_time);
            back = v.findViewById(R.id.article_read_item_header_imageView_back);
        }
    }

    public class ViewHolderContent extends RecyclerView.ViewHolder {
        private AppCompatTextView mTextView_text;

        public ViewHolderContent(View v) {
            super(v);
            mTextView_text = v.findViewById(R.id.article_read_item_textView);
        }
    }

    public class ViewHolderContentImage extends RecyclerView.ViewHolder {
        private com.facebook.drawee.view.SimpleDraweeView image;
        private LinearLayout main;

        public ViewHolderContentImage(View v) {
            super(v);
            image = v.findViewById(R.id.article_read_item_picture);
            main = v.findViewById(R.id.article_read_item_picture_main);
        }
    }

    public class ViewHolderCenterBar extends RecyclerView.ViewHolder {
        private TextView textView_floor;
        private TextView textView_like;

        public ViewHolderCenterBar(View v) {
            super(v);
            textView_floor = v.findViewById(R.id.article_read_item_centerbar_textView_commit);
            textView_like = v.findViewById(R.id.article_read_item_centerbar_textView_like);
        }
    }

    public class ViewHolderComment extends RecyclerView.ViewHolder {
        private TextView textView_auth;
        private androidx.appcompat.widget.AppCompatTextView textView_text;
        private LinearLayout main;

        public ViewHolderComment(View v) {
            super(v);
            textView_auth = v.findViewById(R.id.article_read_item_commit_textview_auth);
            textView_text = v.findViewById(R.id.article_read_item_commit_textView_text);
            main = v.findViewById(R.id.article_read_item_commit_main);
        }
    }

    public class ViewHolderCommitBar extends RecyclerView.ViewHolder {
        private TextView textView_floor;
        private TextView textView_time;
        private TextView textView_like;
        private LinearLayout main;

        public ViewHolderCommitBar(View v) {
            super(v);
            textView_floor = v.findViewById(R.id.article_read_item_commit_textView_floor);
            textView_time = v.findViewById(R.id.article_read_item_commit_textView_time);
            textView_like = v.findViewById(R.id.aarticle_read_item_commit_textView_like);
            main = v.findViewById(R.id.article_read_item_commit_main);
        }
    }

    public class ViewHolderCommitSort extends RecyclerView.ViewHolder {

        public ViewHolderCommitSort(View v) {
            super(v);
        }
    }

    private Context context;

    public ArticleReadAdapter(Context context, List<Map<String, Object>> items) {
        this.data = items;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    private LayoutInflater inflater;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                View view1 = inflater.inflate(R.layout.article_read_item_header, parent, false);
                // view1.setOnClickListener(this);
                // view1.setOnLongClickListener(this);
                ViewHolderHeader holder1 = new ViewHolderHeader(view1);
                return holder1;
            case 1:
                View view2 = inflater.inflate(R.layout.article_read_item_content, parent, false);
                // view1.setOnClickListener(this);
                // view1.setOnLongClickListener(this);
                ViewHolderContent holder2 = new ViewHolderContent(view2);
                return holder2;
            case 2:
                View view3 = inflater.inflate(R.layout.article_read_item_image, parent, false);
                // view1.setOnClickListener(this);
                // view1.setOnLongClickListener(this);
                ViewHolderContentImage holder3 = new ViewHolderContentImage(view3);
                return holder3;
            case 3:
                View view4 = inflater.inflate(R.layout.article_read_item_center_bar, parent, false);
                // view1.setOnClickListener(this);
                // view1.setOnLongClickListener(this);
                ViewHolderCenterBar holder5 = new ViewHolderCenterBar(view4);
                return holder5;
            case 4:
                View view5 = inflater.inflate(R.layout.article_read_item_commit, parent, false);
                // view1.setOnClickListener(this);
                // view1.setOnLongClickListener(this);
                ViewHolderComment holder6 = new ViewHolderComment(view5);
                return holder6;
            case 5:
                View view6 =
                        inflater.inflate(R.layout.article_read_item_commit_sort, parent, false);
                // view1.setOnClickListener(this);
                // view1.setOnLongClickListener(this);
                ViewHolderCommitSort holder7 = new ViewHolderCommitSort(view6);
                return holder7;
            case 6:
                View view7 = inflater.inflate(R.layout.article_read_item_commit_bar, parent, false);
                // view1.setOnClickListener(this);
                // view1.setOnLongClickListener(this);
                ViewHolderCommitBar holder8 = new ViewHolderCommitBar(view7);
                return holder8;
            default:
        }

        return null;
    }

    @Override
    public int getItemViewType(int position) {
        int pos = 5;
        if (StringUtils.notNullString(data.get(position).get("type")).equalsIgnoreCase("header")) {
            pos = 0;
        } else if (StringUtils.notNullString(data.get(position).get("type"))
                .equalsIgnoreCase("content")) {
            pos = 1;
        } else if (StringUtils.notNullString(data.get(position).get("type"))
                .equalsIgnoreCase("content_image")) {
            pos = 2;
        } else if (StringUtils.notNullString(data.get(position).get("type"))
                .equalsIgnoreCase("center_bar")) {
            pos = 3;
        } else if (StringUtils.notNullString(data.get(position).get("type"))
                .equalsIgnoreCase("commit")) {
            pos = 4;
        } else if (StringUtils.notNullString(data.get(position).get("type"))
                .equalsIgnoreCase("commit_sort")) {
            pos = 5;
        } else if (StringUtils.notNullString(data.get(position).get("type"))
                .equalsIgnoreCase("commit_bar")) {
            pos = 6;
        }

        return pos;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holderO, final int position) {
        holderO.itemView.setTag(position);
        if (holderO instanceof ViewHolderHeader) {
            ViewHolderHeader holder = (ViewHolderHeader) holderO;

            StringUtils.TextViewAutoSplitFix(holder.mTextView_title);
            holder.mTextView_title.setText(
                    StringUtils.notNullString(data.get(position).get("title")));
            holder.mTextView_date.setText(
                    StringUtils.notNullString(data.get(position).get("date")));
            holder.mTextView_auth.setText(
                    StringUtils.notNullString(data.get(position).get("auth")));
            holder.mTextView_board.setText(
                    StringUtils.notNullString(data.get(position).get("board"))
                            + " / "
                            + StringUtils.notNullString(data.get(position).get("class")));

            holder.back.setOnClickListener(
                    new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            ((Activity) context).onBackPressed();
                        }
                    });
        } else if (holderO instanceof ViewHolderContent) {
            ViewHolderContent holder = (ViewHolderContent) holderO;

            // holder.mTextView_text.setText(StringUtils.ColorString(StringUtils.notNullString(data.get(position).get("text"))));
            // holder.mTextView_text.setText((StringUtils.notNullString(data.get(position).get("text"))));
            StringUtils.TextViewAutoSplitFix(holder.mTextView_text);
            // Log.d(this.getClass().getName(),"mText.getLineHeight() =
            // "+holder.mTextView_text.getLineHeight());
            // holder.mTextView_text.setLineSpacing(0, (float) (400d/StaticValue.densityDpi));
            holder.mTextView_text.setMovementMethod(new TextViewMovementMethod(context));
            Future<PrecomputedTextCompat> future =
                    PrecomputedTextCompat.getTextFuture(
                            StringUtils.ColorString(
                                    StringUtils.notNullString(data.get(position).get("text"))),
                            holder.mTextView_text.getTextMetricsParamsCompat(),
                            null);

            holder.mTextView_text.setTextFuture(future);
        } else if (holderO instanceof ViewHolderComment) {
            ViewHolderComment holder = (ViewHolderComment) holderO;

            // holder.textView_text.setText(StringUtils.ColorString(StringUtils.notNullString(data.get(position).get("text"))));
            // holder.mTextView_text.setText((StringUtils.notNullString(data.get(position).get("text"))));
            StringUtils.TextViewAutoSplitFix(holder.textView_text);
            holder.textView_text.setMovementMethod(new TextViewMovementMethod(context));
            // holder.textView_text.setLineSpacing(0, (float) (400d/StaticValue.densityDpi));

            holder.textView_auth.setText(
                    StringUtils.ColorString(
                            StringUtils.notNullString(data.get(position).get("auth"))));
            // holder.textView_time.setText(StringUtils.ColorString(StringUtils.notNullString(data.get(position).get("time"))));
            // holder.textView_floor.setText(StringUtils.ColorString(StringUtils.notNullString(data.get(position).get("floor"))));
            // holder.textView_like.setText(StringUtils.ColorString(StringUtils.notNullString(data.get(position).get("like"))));
            int index = (int) data.get(position).get("index");
            if (index % 2 == 0) {
                TypedValue typedValue = new TypedValue();
                Resources.Theme theme = context.getTheme();
                theme.resolveAttribute(R.attr.darkGreyTwo, typedValue, true);
                @ColorInt int color = typedValue.data;
                holder.main.setBackgroundColor(color);
            } else {
                // holder.main.setBackgroundResource(R.color.black);
                TypedValue typedValue = new TypedValue();
                Resources.Theme theme = context.getTheme();
                theme.resolveAttribute(R.attr.article_header, typedValue, true);
                @ColorInt int color = typedValue.data;
                holder.main.setBackgroundColor(color);
            }

            Future<PrecomputedTextCompat> future =
                    PrecomputedTextCompat.getTextFuture(
                            StringUtils.ColorString(
                                    StringUtils.notNullString(data.get(position).get("text"))),
                            holder.textView_text.getTextMetricsParamsCompat(),
                            null);

            holder.textView_text.setTextFuture(future);
        } else if (holderO instanceof ViewHolderCommitBar) {
            ViewHolderCommitBar holder = (ViewHolderCommitBar) holderO;

            // holder.textView_text.setText(StringUtils.ColorString(StringUtils.notNullString(data.get(position).get("text"))));
            // holder.mTextView_text.setText((StringUtils.notNullString(data.get(position).get("text"))));
            // StringUtils.TextViewAutoSplitFix(holder.textView_text);
            // holder.textView_text.setMovementMethod(new TextViewMovementMethod(context));
            // holder.textView_auth.setText(StringUtils.ColorString(StringUtils.notNullString(data.get(position).get("auth"))));

            holder.textView_time.setText(
                    StringUtils.ColorString(
                            StringUtils.notNullString(data.get(position).get("time"))));

            StringUtils.SortDecimal floor_ =
                    StringUtils.sortDecimal(
                            StringUtils.notNullString(data.get(position).get("floor")));

            holder.textView_floor.setText(
                    StringUtils.notNullString(data.get(position).get("floor")));
            // setNumberColor(holder.textView_floor,floor_);

            StringUtils.SortDecimal like_ =
                    StringUtils.sortDecimal(
                            StringUtils.notNullString(data.get(position).get("like")));
            // holder.textView_floor.setText((StringUtils.notNullString(data.get(position).get("floor"))));
            holder.textView_like.setText(like_.toString());
            setNumberColor(holder.textView_like, like_);

            // holder.textView_floor.setText(StringUtils.ColorString(StringUtils.notNullString(data.get(position).get("floor"))));
            // holder.textView_like.setText(StringUtils.ColorString(StringUtils.notNullString(data.get(position).get("like"))));
            int index = (int) data.get(position).get("index");
            if (index % 2 == 0) {
                TypedValue typedValue = new TypedValue();
                Resources.Theme theme = context.getTheme();
                theme.resolveAttribute(R.attr.darkGreyTwo, typedValue, true);
                @ColorInt int color = typedValue.data;
                holder.main.setBackgroundColor(color);
            } else {
                // holder.main.setBackgroundResource(R.color.black);
                TypedValue typedValue = new TypedValue();
                Resources.Theme theme = context.getTheme();
                theme.resolveAttribute(R.attr.article_header, typedValue, true);
                @ColorInt int color = typedValue.data;
                holder.main.setBackgroundColor(color);
            }
        } else if (holderO instanceof ViewHolderCommitSort) {
            ViewHolderCommitSort holder = (ViewHolderCommitSort) holderO;
        } else if (holderO instanceof ViewHolderCenterBar) {
            ViewHolderCenterBar holder = (ViewHolderCenterBar) holderO;

            StringUtils.SortDecimal floor_ =
                    StringUtils.sortDecimal(
                            StringUtils.notNullString(data.get(position).get("floor")));

            holder.textView_floor.setText(floor_.toString());
            setNumberColor(holder.textView_floor, floor_);

            StringUtils.SortDecimal like_ =
                    StringUtils.sortDecimal(
                            StringUtils.notNullString(data.get(position).get("like")));
            // holder.textView_floor.setText((StringUtils.notNullString(data.get(position).get("floor"))));
            holder.textView_like.setText(like_.toString());
            setNumberColor(holder.textView_like, like_);

            int like = 0;
            try {
                like =
                        Integer.parseInt(
                                (StringUtils.notNullString(data.get(position).get("like"))));
            } catch (Exception e) {
            }
            if (like > 1000) {
                TypedValue typedValue = new TypedValue();
                Resources.Theme theme = context.getTheme();
                theme.resolveAttribute(R.attr.tangerine, typedValue, true);
                @ColorInt int color = typedValue.data;
                holder.textView_like.setTextColor(color);
            } else {
                TypedValue typedValue = new TypedValue();
                Resources.Theme theme = context.getTheme();
                theme.resolveAttribute(R.attr.lightBlueGrey, typedValue, true);
                @ColorInt int color = typedValue.data;
                holder.textView_like.setTextColor(color);
            }
            // StringUtils.TextViewAutoSplitFix(holder.mTextView_text);

        } else if (holderO instanceof ViewHolderContentImage) {
            ViewHolderContentImage holder = (ViewHolderContentImage) holderO;
            setImageView(holder.image, StringUtils.notNullString(data.get(position).get("url")));
            int index = (int) data.get(position).get("index");
            if (Math.abs(index % 2) == 1) {
                TypedValue typedValue = new TypedValue();
                Resources.Theme theme = context.getTheme();
                theme.resolveAttribute(R.attr.darkGreyTwo, typedValue, true);
                @ColorInt int color = typedValue.data;
                holder.main.setBackgroundColor(color);
            } else {
                // holder.main.setBackgroundResource(R.color.black);
                TypedValue typedValue = new TypedValue();
                Resources.Theme theme = context.getTheme();
                theme.resolveAttribute(R.attr.article_header, typedValue, true);
                @ColorInt int color = typedValue.data;
                holder.main.setBackgroundColor(color);
            }
        }
    }

    private void setNumberColor(TextView tv, StringUtils.SortDecimal sd) {
        if (sd.isOverDecimal()) {
            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = context.getTheme();
            theme.resolveAttribute(R.attr.tangerine, typedValue, true);
            @ColorInt int color = typedValue.data;
            tv.setTextColor(color);
        } else {
            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = context.getTheme();
            theme.resolveAttribute(R.attr.lightBlueGrey, typedValue, true);
            @ColorInt int color = typedValue.data;
            tv.setTextColor(color);
        }
    }

    private void setImageView(final SimpleDraweeView draweeView, final String Url) {
        if (draweeView.getTag() != null) {
            if (draweeView.getTag().toString().equals(Url)) {
                return;
            }
        }
        draweeView.setTag(Url);

        try {
            ControllerListener controllerListener =
                    new BaseControllerListener<ImageInfo>() {

                        @Override
                        public void onFinalImageSet(
                                String id,
                                @Nullable ImageInfo imageInfo,
                                @Nullable Animatable anim) {
                            if (imageInfo == null) {
                                return;
                            }
                            QualityInfo qualityInfo = imageInfo.getQualityInfo();
                            double caculor =
                                    (StaticValue.widthPixels
                                            / imageInfo.getWidth()
                                            * imageInfo.getHeight());
                            if (caculor > StaticValue.widthPixels) {
                                caculor = StaticValue.widthPixels;
                            }
                            /*if (!imageHight.containsKey(Url)) {
                                            imageHight.put(Url, (int) caculor);
                            }*/

                            if (caculor < StaticValue.widthPixels * 0.3)
                                caculor = StaticValue.widthPixels * 0.3;

                            draweeView.getLayoutParams().height = (int) caculor;
                            draweeView.requestLayout();
                            // mtv.setVisibility(View.GONE);

                        }

                        @Override
                        public void onIntermediateImageSet(
                                String id, @Nullable ImageInfo imageInfo) {
                            // mtv.setText(id);
                        }

                        @Override
                        public void onFailure(String id, Throwable throwable) {
                            // mtv.setText("載入失敗("+throwable.getMessage()+")");
                        }
                    };

            final Uri uri = Uri.parse(Url);
            ImageRequest request =
                    ImageRequestBuilder.newBuilderWithSource(uri)
                            .setLocalThumbnailPreviewsEnabled(true)
                            .setProgressiveRenderingEnabled(false)
                            .setLowestPermittedRequestLevel(ImageRequest.RequestLevel.FULL_FETCH)
                            .setResizeOptions(new ResizeOptions(2048, 2048))
                            .build();

            DraweeController controller =
                    Fresco.newDraweeControllerBuilder()
                            .setImageRequest(request)
                            .setTapToRetryEnabled(true)
                            .setAutoPlayAnimations(true)
                            .setControllerListener(controllerListener)
                            .setOldController(draweeView.getController())
                            .build();
            GenericDraweeHierarchyBuilder builder =
                    new GenericDraweeHierarchyBuilder(context.getResources());
            PointF pf = new PointF(0.5f, 0.5f);
            GenericDraweeHierarchy hierarchy = null;
            hierarchy =
                    builder.setPressedStateOverlay(
                                    context.getResources().getDrawable(R.drawable.image_click))
                            .setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER)
                            .setActualImageFocusPoint(pf)
                            // .setBackground(context.getResources().getDrawable(R.drawable.image_backgroung))
                            .setFadeDuration(0)
                            .setProgressBarImage(new ImageLoadingDrawable(2f))
                            // .setRoundingParams(roundingParams)
                            .build();
            draweeView.setController(controller);
            draweeView.setHierarchy(hierarchy);
        } catch (Exception e) {
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    // define interface
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
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (mOnItemLongClickListener != null) {
            mOnItemLongClickListener.onItemClick(v, (int) v.getTag());
            return true;
        }
        return false;
    }
}
