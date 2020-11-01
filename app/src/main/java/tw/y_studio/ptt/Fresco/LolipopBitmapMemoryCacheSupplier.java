package tw.y_studio.ptt.Fresco;

import android.app.ActivityManager;
import android.os.Build;

import com.facebook.common.internal.Supplier;
import com.facebook.common.util.ByteConstants;
import com.facebook.imagepipeline.cache.MemoryCacheParams;

public class LolipopBitmapMemoryCacheSupplier implements Supplier<MemoryCacheParams> {

    private static final int MAX_HEAP_SIZE = (int) Runtime.getRuntime().maxMemory();
    //private static final int MAX_MEMORY_CACHE_SIZE = MAX_HEAP_SIZE / 4;
    private ActivityManager activityManager;
    private static final int MAX_CACHE_ENTRIES = 128;
    private static final int MAX_CACHE_ASHM_ENTRIES = Integer.MAX_VALUE;
    private static final int MAX_CACHE_EVICTION_SIZE = Integer.MAX_VALUE;
    private static final int MAX_CACHE_EVICTION_ENTRIES = Integer.MAX_VALUE;

    public LolipopBitmapMemoryCacheSupplier(ActivityManager activityManager) {
        this.activityManager = activityManager;
    }

    @Override
    public MemoryCacheParams get() {
        // Log.d("onFresco","MaxCache="+getMaxCacheSize());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return new MemoryCacheParams(getMaxCacheSize(), MAX_CACHE_ENTRIES, getMaxCacheSize(), MAX_CACHE_EVICTION_SIZE, MAX_CACHE_EVICTION_ENTRIES);
        } else {
            return new MemoryCacheParams(
                    getMaxCacheSize(),
                    MAX_CACHE_ENTRIES,
                    getMaxCacheSize(),
                    MAX_CACHE_EVICTION_SIZE,
                    MAX_CACHE_EVICTION_ENTRIES);
        }
    }

    private int getMaxCacheSize() {
        final int maxMemory = Math.min(MAX_HEAP_SIZE, Integer.MAX_VALUE);


        if (maxMemory < 32 * ByteConstants.MB) {
            return 3 * ByteConstants.MB;
        } else if (maxMemory < 64 * ByteConstants.MB) {
            return 4 * ByteConstants.MB;
        } else {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD) {
                return 6 * ByteConstants.MB;
            } else {
                return maxMemory / 4;
            }
        }

    }



}