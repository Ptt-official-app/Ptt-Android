package tw.y_studio.ptt.api

import com.google.gson.GsonBuilder
import okhttp3.Request
import tw.y_studio.ptt.model.Post
import tw.y_studio.ptt.utils.Log
import tw.y_studio.ptt.utils.model.PostTypeAdapter

class PostAPI : BaseAPIHelper() {

    @Throws(Exception::class)
    fun getPost(board: String, fileName: String): Post {
        Log("onGetPost", "$hostUrl/api/Article/$board/$fileName")
        val request = Request.Builder()
            .url("$hostUrl/api/Article/$board/$fileName")
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
}
