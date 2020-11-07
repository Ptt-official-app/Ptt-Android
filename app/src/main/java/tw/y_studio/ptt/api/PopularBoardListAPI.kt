package tw.y_studio.ptt.api

import okhttp3.Request
import org.json.JSONArray
import java.util.*

class PopularBoardListAPI : BaseAPIHelper(), IBaseAPI {
    private val _data: MutableList<Map<String, Any>> = mutableListOf()

    @Throws(Exception::class)
    fun refresh(page: Int, count: Int): MutableList<Map<String, Any>> {
        _data.clear()
        val request = Request.Builder()
            .url("$hostUrl/api/Board/Popular?page=$page&count=$count")
            .build()
        val call = okHttpClient.newCall(request)
        // TODO: 2020/11/5 refactor to enqueue()
        val response = call.execute()
        val code = response.code // can be any value
        if (!response.isSuccessful && code != 200) {
            // error
            throw Exception("Error Code : $code")
        } else {
            val mRb = response.body
            val cont = mRb!!.string()
            val list = JSONArray(cont)
            var i = 0
            while (!list.isNull(i)) {
                val m3 = list.getJSONObject(i)
                i++
                val item: MutableMap<String, Any> = HashMap()
                item["number"] = m3.getInt("sn")
                item["title"] = m3.getString("name")
                item["subtitle"] = m3.getString("title")
                item["boardType"] = m3.getInt("boardType")
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
