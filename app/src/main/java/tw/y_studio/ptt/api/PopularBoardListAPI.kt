package tw.y_studio.ptt.api

import okhttp3.Request
import org.json.JSONArray
import tw.y_studio.ptt.api.model.hot_board.HotBoardTemp

class PopularBoardListAPI : BaseAPIHelper(), IBaseAPI {
    private val _data: MutableList<HotBoardTemp> = mutableListOf()

    @Throws(Exception::class)
    fun refresh(page: Int, count: Int): MutableList<HotBoardTemp> {
        _data.clear()
        val request = Request.Builder()
            .url("$hostUrl/api/Board/Popular?page=$page&count=$count")
            .build()
        val call = okHttpClient?.newCall(request)
        // TODO: 2020/11/5 refactor to enqueue()
        val response = call?.execute()
        val code = response?.code // can be any value
        if (response?.isSuccessful != true && code != 200) {
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
                val hotboard = HotBoardTemp(
                    number = m3.getInt("sn"),
                    title = m3.getString("name"),
                    subtitle = m3.getString("title"),
                    boardType = m3.getInt("boardType"),
                    online = m3.getInt("onlineCount"),
                    onlineColor = "7"
                )
                _data.add(hotboard)
            }
        }
        return _data
    }
}
