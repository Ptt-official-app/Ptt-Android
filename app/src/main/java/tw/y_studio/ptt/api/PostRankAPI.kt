package tw.y_studio.ptt.api

import com.google.gson.Gson
import okhttp3.FormBody
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject
import tw.y_studio.ptt.extension.fromJson
import tw.y_studio.ptt.model.PostRankResponse
import tw.y_studio.ptt.utils.Log
import java.io.IOException
import kotlin.jvm.Throws

class PostRankAPI() : BaseAPIHelper() {

    enum class PostRank(val value: Int) {
        Like(1), Dislike(-1), None(0)
    }

    @Throws(Exception::class)
    fun setPostRank(
        boardName: String,
        aid: String,
        pttId: String,
        rank: PostRank
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
            val responseBody = response.body
            responseBody?.let {
                val postRank = Gson().fromJson<PostRankResponse>(it.string())
            } ?: throw IOException("No response body!")
        }
    }
}
