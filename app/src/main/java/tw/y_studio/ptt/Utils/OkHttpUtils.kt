@file:JvmName("OkHttpUtils")
package tw.y_studio.ptt.utils

import android.annotation.SuppressLint
import android.content.Context
import okhttp3.*
import okio.BufferedSink
import okio.GzipSink
import okio.buffer
import java.io.IOException
import java.lang.ref.SoftReference
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.X509TrustManager

class OkHttpUtils {
    class MyTrustManager : X509TrustManager {
        @SuppressLint("TrustAllX509TrustManager")
        @Throws(CertificateException::class)
        override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
        }

        @SuppressLint("TrustAllX509TrustManager")
        @Throws(CertificateException::class)
        override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
        }

        override fun getAcceptedIssuers(): Array<X509Certificate> {
            return arrayOf()
        }
    }

    private fun provideCache(mContext: Context): Cache {
        return Cache(mContext.cacheDir, 10240 * 1024 * 100)
    }

    class CacheInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val response = chain.proceed(request)
            return response.newBuilder()
                .removeHeader("Pragma")
                .removeHeader("Cache-Control") // cache for 3 days
                .header("Cache-Control", "max-age=" + 3600 * 24 * 3)
                .build()
        }
    }

    val trustAllClient: OkHttpClient?
        get() {
            val mBuilder = OkHttpClient.Builder()
            mBuilder.connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(GzipRequestInterceptor())
            return SoftReference(mBuilder.build()).get()
        }

    fun getCacheClient(mContext: Context): OkHttpClient? {
        val mBuilder = OkHttpClient.Builder()
        mBuilder.addNetworkInterceptor(CacheInterceptor())
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(GzipRequestInterceptor()) // Gzip
            .cache(provideCache(mContext))
        return SoftReference(mBuilder.build()).get()
    }

    class GzipRequestInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()
            if (originalRequest.body == null ||
                originalRequest.header("Content-Encoding") != null
            ) {
                return chain.proceed(originalRequest)
            }
            val compressedRequest = originalRequest
                .newBuilder()
                .header("Content-Encoding", "gzip, deflate")
                .method(originalRequest.method, gzip(originalRequest.body))
                .build()
            return chain.proceed(compressedRequest)
        }

        private fun gzip(body: RequestBody?): RequestBody {
            return object : RequestBody() {
                override fun contentType(): MediaType? {
                    return body!!.contentType()
                }

                override fun contentLength(): Long {
                    return -1
                }

                @Throws(IOException::class)
                override fun writeTo(sink: BufferedSink) {
                    val gzipSink = GzipSink(sink).buffer()
                    body!!.writeTo(gzipSink)
                    gzipSink.close()
                }
            }
        }
    }
}
