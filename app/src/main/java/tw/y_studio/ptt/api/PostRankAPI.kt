package tw.y_studio.ptt.api

import okhttp3.FormBody
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject
import tw.y_studio.ptt.utils.Log
import kotlin.jvm.Throws

class PostRankAPI() : BaseAPIHelper() {
    private val _data: MutableMap<String, Any> = mutableMapOf()

    enum class PostRank(val value: Int) {
        Like(1), Dislike(-1), None(0)
    }

    @Throws(Exception::class)
    fun setPostRank(
        boardName: String,
        aid: String,
        pttId: String,
        rank: PostRank
    ): MutableMap<String, Any> {
        _data.clear()
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
            val cont = responseBody!!.string()
            val all = JSONObject(cont)
            // Log.d("API","GetRankByPost = "+all.toString());
            _data["Rank"] = all.getInt("rank")
            _data["PTTID"] = all.getString("pttid")
            _data["no"] = all.getString("no")
            _data["Board"] = all.getString("board")
            _data["AID"] = all.getString("aid")
        }
        return _data
    }
}
