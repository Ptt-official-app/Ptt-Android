package tw.y_studio.ptt.Utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import androidx.browser.customtabs.CustomTabsIntent;

import tw.y_studio.ptt.R;

public class WebUtils {
    public static void turnOnUrl(Context context, String url){

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, url);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, url);
        //PendingIntent pi = PendingIntent.getActivity(context, 0, sharingIntent, 0);
        //Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_share_black_24dp);

        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        //builder.addMenuItem("分享", pi);
        //builder.setActionButton(icon, "Share", pi,true);

        builder.addDefaultShareMenuItem();
        builder.setToolbarColor(context.getResources().getColor(android.R.color.black));
        builder.setShowTitle(true);


        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(context, Uri.parse(url));
        //icon.recycle();
    }
    public static boolean isConnected(Context context) {

        if (context != null) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnectedOrConnecting();
        } return false;
    }
}
