package tw.y_studio.ptt;

import android.app.ActivityManager;
import android.graphics.Bitmap;

import androidx.multidex.MultiDexApplication;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.memory.MemoryTrimType;
import com.facebook.common.memory.MemoryTrimmable;
import com.facebook.common.memory.NoOpMemoryTrimmableRegistry;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.core.ImagePipelineFactory;

import okhttp3.OkHttpClient;
import tw.y_studio.ptt.Fresco.LolipopBitmapMemoryCacheSupplier;
import tw.y_studio.ptt.Fresco.MyOkHttpNetworkFetcher;
import tw.y_studio.ptt.Utils.OkHttpUtils;


public class PttApplication extends MultiDexApplication {

    private OkHttpClient mOkHttpClient = null;
    private ImagePipelineConfig config = null;


    @Override
    public void onCreate() {
        super.onCreate();


        if( mOkHttpClient == null){
            try {
                mOkHttpClient=new OkHttpUtils().getCacheClient(this);
            }catch (Exception e){

            }
        }

        if( config == null ){
            NoOpMemoryTrimmableRegistry.getInstance().registerMemoryTrimmable(new MemoryTrimmable() {
                @Override
                public void trim(MemoryTrimType trimType) {
                    final double suggestedTrimRatio = trimType.getSuggestedTrimRatio();


                    if (MemoryTrimType.OnCloseToDalvikHeapLimit.getSuggestedTrimRatio() == suggestedTrimRatio
                            || MemoryTrimType.OnSystemLowMemoryWhileAppInBackground.getSuggestedTrimRatio() == suggestedTrimRatio
                            || MemoryTrimType.OnSystemLowMemoryWhileAppInForeground.getSuggestedTrimRatio() == suggestedTrimRatio
                    ) {
                        ImagePipelineFactory.getInstance().getImagePipeline().clearMemoryCaches();
                    }
                }
            });

            config = OkHttpImagePipelineConfigFactory
                    .newBuilder(this, mOkHttpClient)
                    .setMainDiskCacheConfig(DiskCacheConfig.newBuilder(this)
                            .setBaseDirectoryPath(getCacheDir())
                            .build())
                    .setNetworkFetcher(new MyOkHttpNetworkFetcher(mOkHttpClient))
                    .setDownsampleEnabled(true)
                    .experiment().setWebpSupportEnabled(false)
                    .setMemoryTrimmableRegistry(NoOpMemoryTrimmableRegistry.getInstance())
                    .experiment().setUseDownsampligRatioForResizing(true)
                    .setBitmapMemoryCacheParamsSupplier(new LolipopBitmapMemoryCacheSupplier((ActivityManager) getSystemService(ACTIVITY_SERVICE)))
                    .setResizeAndRotateEnabledForNetwork(true)
                    //.setMemoryChunkType(MemoryChunkType.BUFFER_MEMORY)
                    //.setImageTranscoderType(ImageTranscoderType.JAVA_TRANSCODER)
                    //.experiment().setNativeCodeDisabled(true)
                    .setBitmapsConfig(Bitmap.Config.RGB_565)
                    .build();

        }


        try {
            Fresco.initialize(getApplicationContext(),config);
        } catch (UnsatisfiedLinkError e) {
            Fresco.shutDown();
        }

    }




    @Override
    public void onLowMemory() {
        super.onLowMemory();
        try {
            ImagePipelineFactory.getInstance().getImagePipeline().clearMemoryCaches();
        }catch (Exception e){

        }
    }
    @Override
    public void onTrimMemory(int level){
        super.onTrimMemory(level);
    }

}

