package tw.y_studio.ptt.Utils;

import android.util.Log;

import tw.y_studio.ptt.BuildConfig;

public class DebugUtils {
    public static final boolean Debug = BuildConfig.DEBUG;
    public static final boolean useApi = true;
    public static void Log(String title,String message){
        if(Debug){
            Log.d(title,message);
        }
    }
}
