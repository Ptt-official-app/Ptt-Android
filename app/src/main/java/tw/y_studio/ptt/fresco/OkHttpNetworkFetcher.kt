package tw.y_studio.ptt.fresco

import android.os.Looper
import android.os.SystemClock
import com.facebook.imagepipeline.image.EncodedImage
import com.facebook.imagepipeline.producers.*
import okhttp3.*
import java.io.IOException
import java.util.concurrent.Executor

class OkHttpNetworkFetcher(
    private val mOkHttpClient: OkHttpClient
) : BaseNetworkFetcher<OkHttpNetworkFetcher.OkHttpNetworkFetchState>() {
    class OkHttpNetworkFetchState(
        consumer: Consumer<EncodedImage>?,
        producerContext: ProducerContext?
    ) : FetchState(consumer, producerContext) {
        var submitTime = 0L
        var responseTime = 0L
        var fetchCompleteTime = 0L
    }

    private val mCancellationExecutor: Executor

    override fun createFetchState(
        consumer: Consumer<EncodedImage>,
        context: ProducerContext
    ): OkHttpNetworkFetchState {
        return OkHttpNetworkFetchState(consumer, context)
    }

    override fun fetch(fetchState: OkHttpNetworkFetchState, callback: NetworkFetcher.Callback) {
        fetchState.submitTime = SystemClock.elapsedRealtime()

        val uri = fetchState.uri
        val request = Request.Builder()
            .cacheControl(CacheControl.Builder().noStore().build())
            .url(uri.toString())
            .get()
            .build()
        val call = mOkHttpClient.newCall(request)
        val baseProducerContextCallbacks = object : BaseProducerContextCallbacks() {
            override fun onCancellationRequested() {
                if (Looper.myLooper() != Looper.getMainLooper()) {
                    call.cancel()
                } else {
                    mCancellationExecutor.execute { call.cancel() }
                }
            }
        }
        val fetchCallback = object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                handleException(call, e, callback)
            }

            override fun onResponse(call: Call, response: Response) {
                fetchState.responseTime = SystemClock.elapsedRealtime()

                try {
                    response.use {
                        if (!response.isSuccessful) throw IOException("Unexpected code $response")

                        val body = response.body!!
                        var contentLength = body.contentLength()
                        if (contentLength < 0) {
                            contentLength = 0
                        }
//                        val contentLengthInMB = contentLength / 1024 / 1024
//                        if (contentLengthInMB > FETCH_SIZE_LIMIT_MB) {
//                            throw Exception(
//                                "檔案超過${FETCH_SIZE_LIMIT_MB}MB限制" +
//                                    "(${contentLengthInMB}MB)\n請自行開啟連結"
//                            )
//                        }
                        callback.onResponse(body.byteStream(), contentLength.toInt())
                    }
                } catch (e: Exception) {
                    handleException(call, e, callback)
                }
            }
        }

        fetchState.context.addCallbacks(baseProducerContextCallbacks)
        call.enqueue(fetchCallback)
    }

    private fun handleException(call: Call, e: Exception, callback: NetworkFetcher.Callback) {
        if (call.isCanceled()) {
            callback.onCancellation()
        } else {
            callback.onFailure(e)
        }
    }

    companion object {
        private const val TAG = "OkHttpNetworkFetchProducer"
        private const val QUEUE_TIME = "queue_time"
        private const val FETCH_TIME = "fetch_time"
        private const val TOTAL_TIME = "total_time"
        private const val IMAGE_SIZE = "image_size"
        private const val FETCH_SIZE_LIMIT_MB = 12
    }

    init {
        mCancellationExecutor = mOkHttpClient.dispatcher.executorService
    }
}
