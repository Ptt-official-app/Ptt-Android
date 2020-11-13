package tw.y_studio.ptt.ui;

import android.content.Context;
import android.graphics.Typeface;

public class TypefaceUI {
    public static TypefaceUI inst;
    private Typeface type;
    private Typeface type2;
    private Context appCont;

    public static TypefaceUI getInstance() {
        if (inst == null) {
            inst = new TypefaceUI();
        }
        return inst;
    }

    public TypefaceUI setContext(Context appCont) {
        this.appCont = appCont;
        return inst;
    }

    public Typeface getTaipeiSansTC() {
        if (type2 == null) {
            try {
                // type2 =
                // Typeface.createFromAsset(appCont.getAssets(),"fonts/TaipeiSansTCBeta-Regular.ttf");
            } catch (Exception e) {
            }
        }
        return type2;
    }
}
