package tw.y_studio.ptt.fresco

import com.facebook.common.internal.Supplier
import com.facebook.common.util.ByteConstants
import com.facebook.imagepipeline.cache.MemoryCacheParams
import kotlin.math.min

class BitmapMemoryCacheSupplier : Supplier<MemoryCacheParams> {
    override fun get(): MemoryCacheParams {
//        Log.d("onFresco", "MaxCache=${maxCacheSize}")
        return MemoryCacheParams(
            maxCacheSize,
            MAX_CACHE_ENTRIES,
            maxCacheSize,
            MAX_CACHE_EVICTION_SIZE,
            MAX_CACHE_EVICTION_ENTRIES
        )
    }

    private val maxCacheSize: Int
        get() {
            val maxMemory = min(MAX_HEAP_SIZE, Int.MAX_VALUE)
            return when {
                maxMemory < 32 * ByteConstants.MB -> 3 * ByteConstants.MB
                maxMemory < 64 * ByteConstants.MB -> 4 * ByteConstants.MB
                else -> maxMemory / 4
            }
        }

    companion object {
        private val MAX_HEAP_SIZE = Runtime.getRuntime().maxMemory().toInt()
//        private val MAX_MEMORY_CACHE_SIZE = MAX_HEAP_SIZE / 4;
        private const val MAX_CACHE_ENTRIES = 128
        private const val MAX_CACHE_ASHM_ENTRIES = Int.MAX_VALUE
        private const val MAX_CACHE_EVICTION_SIZE = Int.MAX_VALUE
        private const val MAX_CACHE_EVICTION_ENTRIES = Int.MAX_VALUE
    }
}
