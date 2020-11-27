package tw.y_studio.ptt.api

import android.content.Context
import okhttp3.FormBody
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject
import tw.y_studio.ptt.utils.Log
import java.util.*
import kotlin.jvm.Throws

class SetPostRankAPIHelper() : BaseAPIHelper() {
    private val data: MutableMap<String, Any> = mutableMapOf()
    private val board = ""
    private val aid = ""
    fun getData(): Map<String, Any> {
        return data
    }

    enum class iRank {
        like, dislike, non
    }

    private fun iRank2Int(rank: iRank): Int {
        return when (rank) {
            iRank.like -> 1
            iRank.dislike -> -1
            iRank.non -> 0
            else -> 0
        }
    }

    @Throws(Exception::class)
    fun get(pttid: String, rank: iRank): SetPostRankAPIHelper {
        data.clear()
        val body: RequestBody = FormBody.Builder().build()
        val request = Request.Builder()
                .post(body)
                .url(
                        hostUrl
                                + "/api/Rank/"
                                + board
                                + "/"
                                + aid
                                + "?pttid="
                                + pttid
                                + "&rank="
                                + iRank2Int(rank))
                .build()
        Log("SetPostRankAPIHelper", "" + request.toString())
        val mcall = okHttpClient!!.newCall(request)
        val response = mcall.execute()
        val code = response.code // can be any value
        if (!response.isSuccessful && code != 200) {
            // error
            throw Exception("Error Code : $code")
        } else {
            val mRb = response.body
            val cont = mRb!!.string()
            val all = JSONObject(cont)
            // Log.d("API","GetRankByPost = "+all.toString());
            data["Rank"] = all.getInt("rank")
            data["PTTID"] = all.getString("pttid")
            data["no"] = all.getString("no")
            data["Board"] = all.getString("board")
            data["AID"] = all.getString("aid")
        }
        return this
    }
}