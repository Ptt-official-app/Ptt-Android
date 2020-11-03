package tw.y_studio.ptt.API;

import android.content.Context;

import okhttp3.OkHttpClient;
import tw.y_studio.ptt.BuildConfig;
import tw.y_studio.ptt.Utils.OkHttpUtils;

public class BaseAPIHelper {
    protected String hostUrl = BuildConfig.API_Domain;
    protected OkHttpClient mOkHttpClient;

    public BaseAPIHelper(Context context) {
        mOkHttpClient = new OkHttpUtils().getTrustlAllClient(context);
    }

    public void close() {
        if (mOkHttpClient != null) {
            mOkHttpClient.dispatcher().cancelAll();
        }
        mOkHttpClient = null;
    }
}
