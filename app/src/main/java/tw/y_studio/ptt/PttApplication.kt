package tw.y_studio.ptt

import android.graphics.Bitmap
import androidx.multidex.MultiDexApplication
import com.facebook.cache.disk.DiskCacheConfig
import com.facebook.common.memory.MemoryTrimType
import com.facebook.common.memory.NoOpMemoryTrimmableRegistry
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory
import com.facebook.imagepipeline.core.ImagePipelineConfig
import com.facebook.imagepipeline.core.ImagePipelineFactory
import okhttp3.OkHttpClient
import tw.y_studio.ptt.fresco.BitmapMemoryCacheSupplier
import tw.y_studio.ptt.fresco.OkHttpNetworkFetcher
import tw.y_studio.ptt.utils.OkHttpUtils

class PttApplication : MultiDexApplication() {
    private var mOkHttpClient: OkHttpClient? = null
    private var config: ImagePipelineConfig? = null

    override fun onCreate() {
        super.onCreate()
        if (mOkHttpClient == null) {
            try {
                mOkHttpClient = OkHttpUtils().getCacheClient(this)
            } catch (e: Exception) {
            }
        }
        if (config == null && mOkHttpClient != null) {
            NoOpMemoryTrimmableRegistry.getInstance()
                .registerMemoryTrimmable { trimType ->
                    val suggestedTrimRatio = trimType.suggestedTrimRatio
                    if ((
                        MemoryTrimType.OnCloseToDalvikHeapLimit
                            .suggestedTrimRatio
                            == suggestedTrimRatio
                        ) || (
                            MemoryTrimType.OnSystemLowMemoryWhileAppInBackground
                                .suggestedTrimRatio
                                == suggestedTrimRatio
                            ) || (
                            MemoryTrimType.OnSystemLowMemoryWhileAppInForeground
                                .suggestedTrimRatio
                                == suggestedTrimRatio
                            )
                    ) {
                        ImagePipelineFactory.getInstance()
                            .imagePipeline
                            .clearMemoryCaches()
                    }
                }
            config = OkHttpImagePipelineConfigFactory.newBuilder(this, mOkHttpClient)
                .setMainDiskCacheConfig(
                    DiskCacheConfig.newBuilder(this)
                        .setBaseDirectoryPath(cacheDir)
                        .build()
                )
                .setNetworkFetcher(OkHttpNetworkFetcher(mOkHttpClient!!))
                .setDownsampleEnabled(true)
                .experiment()
                .setWebpSupportEnabled(false)
                .setMemoryTrimmableRegistry(NoOpMemoryTrimmableRegistry.getInstance())
                .experiment()
                .setUseDownsampligRatioForResizing(true)
                .setBitmapMemoryCacheParamsSupplier(BitmapMemoryCacheSupplier())
                .setResizeAndRotateEnabledForNetwork(true)
                // .setMemoryChunkType(MemoryChunkType.BUFFER_MEMORY)
                // .setImageTranscoderType(ImageTranscoderType.JAVA_TRANSCODER)
                // .experiment().setNativeCodeDisabled(true)
                .setBitmapsConfig(Bitmap.Config.RGB_565)
                .build()
        }
        try {
            Fresco.initialize(applicationContext, config)
        } catch (e: UnsatisfiedLinkError) {
            Fresco.shutDown()
        }
    }

    override fun onLowMemory() {
        super.onLowMemory()
        try {
            ImagePipelineFactory.getInstance().imagePipeline.clearMemoryCaches()
        } catch (e: Exception) {
        }
    }
}
