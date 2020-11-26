package tw.y_studio.ptt.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Request
import org.json.JSONObject
import tw.y_studio.ptt.model.PartialPost
import tw.y_studio.ptt.model.Post
import tw.y_studio.ptt.model.PostRank
import tw.y_studio.ptt.utils.Log
import tw.y_studio.ptt.utils.model.PartialPostTypeAdapter
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
    fun getPostList(broadName: String, page: Int): List<PartialPost> {
        val data = mutableListOf<PartialPost>()
        val request = Request.Builder()
            .url("$hostUrl/api/Article/$broadName?page=$page")
            .build()
        val call = okHttpClient!!.newCall(request)
        val response = call.execute()
        val code = response.code // can be any value
        val responseBody = response.body
        if (!response.isSuccessful && code != 200 || responseBody == null) {
            // error
            throw Exception("Error Code : $code")
        } else {
            val cont = responseBody.string()
            val all = JSONObject(cont)
            val postList = all.getJSONArray("postList")
            var i = 0
            val gson = GsonBuilder().registerTypeAdapter(PartialPost::class.java, PartialPostTypeAdapter()).create()
            while (!postList.isNull(i)) {
                data.add(gson.fromJson(postList.getString(i), PartialPost::class.java))
                i++
            }
        }
        return data
    }
}
