package tw.y_studio.ptt.api

import okhttp3.Request
import org.json.JSONArray
import tw.y_studio.ptt.api.model.board.search_board.SearchBoardsItem
import java.net.URLEncoder
import java.util.*

class SearchBoardAPI() : BaseAPIHelper(), IBaseAPI {
    private val _data = mutableListOf<SearchBoardsItem>()

    @Throws(Exception::class)
    fun searchBoard(keyword: String): MutableList<SearchBoardsItem> {
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
                val item: SearchBoardsItem = SearchBoardsItem(
                    number = m3.getInt("sn"),
                    title = m3.getString("name"),
                    subtitle = m3.getString("title"),
                    boardType = m3.getInt("boardType"),
                    online = m3.getInt("onlineCount")
                )
                _data.add(item)
            }
        }
        return _data
    }
}
