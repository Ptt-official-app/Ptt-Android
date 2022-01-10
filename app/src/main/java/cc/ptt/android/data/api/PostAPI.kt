package cc.ptt.android.data.api

import cc.ptt.android.common.utils.Log
import cc.ptt.android.data.common.model.PostTypeAdapter
import cc.ptt.android.data.model.remote.Post
import cc.ptt.android.data.model.remote.PostRank
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.FormBody
import okhttp3.Request
import okhttp3.RequestBody
import java.io.IOException

class PostAPI : BaseAPIHelper() {

    @Throws(Exception::class)
    fun getPost(board: String, fileName: String): Post {
        Log("onGetPost", "$hostUrl/api/article/$board/$fileName")
        val request = Request.Builder()
            .url("$hostUrl/api/article/$board/$fileName")
            .build()
        val mcall = okHttpClient!!.newCall(request)
        val response = mcall.execute()
        val code = response.code // can be any value
        return if (!response.isSuccessful && code != 200) {
            // error
            throw Exception("Error Code : $code")
        } else {
            val mRb = response.body
            val cont = mRb!!.string()
            Log("onGetPost", "all = $cont")
            val gson = GsonBuilder()
                .registerTypeAdapter(Post::class.java, PostTypeAdapter())
                .create()
            gson.fromJson(cont, Post::class.java)
        }
    }

    @Throws(java.lang.Exception::class)
    fun getPostRank(board: String, aid: String): PostRank {
        val request = Request.Builder()
            .url("$hostUrl/api/Rank/$board/$aid")
            .build()
        val mcall = okHttpClient!!.newCall(request)
        val response = mcall.execute()
        val code = response.code // can be any value
        return if (!response.isSuccessful && code != 200) {
            throw java.lang.Exception("Error Code : $code")
        } else {
            val mRb = response.body
            val cont = mRb!!.string()
            Gson().fromJson(cont, PostRank::class.java)
        }
    }

    @Throws(Exception::class)
    fun setPostRank(
        boardName: String,
        aid: String,
        pttId: String,
        rank: PostRankMark
    ) {
        val body: RequestBody = FormBody.Builder().build()
        val request = Request.Builder()
            .post(body)
            .url(
                "$hostUrl/api/Rank/$boardName/$aid?pttid=$pttId&rank=${rank.value}"
            )
            .build()
        Log("SetPostRankAPIHelper", "" + request.toString())
        val call = okHttpClient!!.newCall(request)
        val response = call.execute()
        val code = response.code // can be any value
        if (!response.isSuccessful && code != 200) {
            // error
            throw Exception("Error Code : $code")
        } else {
            response.body ?: throw IOException("No response body!")
        }
    }
}

enum class PostRankMark(val value: Int) {
    Like(1), Dislike(-1), None(0)
}
