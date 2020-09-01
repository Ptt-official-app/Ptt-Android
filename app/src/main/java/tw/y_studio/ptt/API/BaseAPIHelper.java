package tw.y_studio.ptt.API;

import android.content.Context;

import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import tw.y_studio.ptt.BuildConfig;
import tw.y_studio.ptt.Utils.OkHttpUtils;

public class BaseAPIHelper {
    protected String hostUrl = BuildConfig.API_Domain;
    protected OkHttpClient mOkHttpClient;

    public BaseAPIHelper(Context context){
        mOkHttpClient = new OkHttpUtils().getTrustlAllClient(context);
    }

    public void close(){
        mOkHttpClient = null;
    }
}
