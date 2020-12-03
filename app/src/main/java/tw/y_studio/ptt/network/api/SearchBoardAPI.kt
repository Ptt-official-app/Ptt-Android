package tw.y_studio.ptt.network.api

import okhttp3.Request
import org.json.JSONArray
import java.net.URLEncoder
import java.util.*

class SearchBoardAPI() : BaseAPIHelper(), IBaseAPI {
    private val _data = mutableListOf<Map<String, Any>>()

    @Throws(Exception::class)
    fun searchBoard(keyword: String): MutableList<Map<String, Any>> {
        _data.clear()
        val text = URLEncoder.encode(keyword, "UTF-8").toString()
        val request = Request.Builder().url("$hostUrl/api/Board/Search?keyword=$text").build()
        val mcall = okHttpClient?.newCall(request)
        val response = mcall?.execute()
        val code = response?.code // can be any value
        if (response?.isSuccessful != true && code != 200) {
            // error
            throw Exception("Error Code : $code")
        } else {
            val mRb = response.body
            val cont = mRb!!.string()
            val List = JSONArray(cont)
            var i = 0
            while (!List.isNull(i)) {
                val m3 = List.getJSONObject(i)
                i++
                val item: MutableMap<String, Any> = HashMap()
                item["number"] = m3.getInt("sn")
                item["title"] = m3.getString("name")
                item["subtitle"] = m3.getString("title")
                item["boardType"] = m3.getInt("boardType")
                item["like"] = false
                item["moderators"] = ""
                item["class"] = ""
                item["online"] = m3.getInt("onlineCount")
                item["onlineColor"] = 7
                _data.add(item)
            }
        }
        return _data
    }
}
