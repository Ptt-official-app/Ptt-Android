package tw.y_studio.ptt.UI;

import java.util.Date;

public class ClickFix {

    public ClickFix(long defaultTime) {
        this.defaultTime = defaultTime;
    }

    public ClickFix() {}

    private long lastClickTime = 0;
    private long defaultTime = 500L;

    public boolean isFastDoubleClick() {
        return isFastDoubleClick(defaultTime);
    }

    public boolean isFastDoubleClick(long time2) {
        long time = new Date().getTime();
        // long timeD = time - lastClickTime;
        if (Math.abs(time - lastClickTime) < time2) {
            // lastClickTime = time;
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
