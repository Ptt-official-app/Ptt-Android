package tw.y_studio.ptt.Utils;

import android.content.Context;
import android.content.Intent;

import org.jetbrains.annotations.NotNull;

public class UIUtils {


    /**
     * 使用Android分享選單分享
     * @param context
     * @param subject
     * @param body
     * @param chooserTitle
     */
    public static void shareTo(@NotNull Context context, String subject, String body, String chooserTitle) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);
        context.startActivity(Intent.createChooser(sharingIntent, chooserTitle));
    }
}
