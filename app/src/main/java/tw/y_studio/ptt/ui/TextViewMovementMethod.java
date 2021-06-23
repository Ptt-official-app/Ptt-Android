package tw.y_studio.ptt.ui;

import android.content.Context;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.ArrowKeyMovementMethod;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.MotionEvent;
import android.widget.TextView;

import tw.y_studio.ptt.utils.WebUtils;

public class TextViewMovementMethod extends ArrowKeyMovementMethod {
    private TextViewMovementMethod sInstance;
    private MovementMethod cMethod;
    private Context mContext;

    public TextViewMovementMethod(Context mContext) {
        this.cMethod = LinkMovementMethod.getInstance();
        this.mContext = mContext;
    }

    public void initialize(TextView widget, Spannable text) {
        this.cMethod.initialize(widget, text);
    }

    public boolean onTouchEvent(TextView widget, Spannable text, MotionEvent event) {
        // super.onTouchEvent(widget, text, event);
        int action = event.getAction();
        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN) {
            int x = (((int) event.getX()) - widget.getTotalPaddingLeft()) + widget.getScrollX();
            int y = (((int) event.getY()) - widget.getTotalPaddingTop()) + widget.getScrollY();
            Layout layout = widget.getLayout();
            int off = layout.getOffsetForHorizontal(layout.getLineForVertical(y), (float) x);
            ClickableSpan[] link = text.getSpans(off, off, ClickableSpan.class);
            if (link.length > 0 && link[0] != null) {
                if (action == MotionEvent.ACTION_UP) {
                    try {
                        URLSpan span = (URLSpan) link[0];
                        span.getURL();
                        String url = span.getURL();
                        // Log.d("onTVMM","click="+url);
                        WebUtils.turnOnUrl(mContext, url);
                    } catch (Exception e) {
                    }
                }
                if (action == MotionEvent.ACTION_DOWN) {
                    Selection.setSelection(
                            text, text.getSpanStart(link[0]), text.getSpanEnd(link[0]));
                }
                return true;
            }
        }

        return false;
    }

    public MovementMethod getInstance(Context mContext) {
        // if (sInstance == null) {
        sInstance = new TextViewMovementMethod(mContext);
        // }
        return sInstance;
    }
}
