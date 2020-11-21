package tw.y_studio.ptt.api

import okhttp3.Request
import org.json.JSONObject
import tw.y_studio.ptt.model.PartialPost
import tw.y_studio.ptt.utils.StringUtils.clearStart
import java.lang.Exception
import java.util.regex.Pattern
import kotlin.Throws

class PostListAPI() : BaseAPIHelper(), IBaseAPI {

    companion object {
        private val Title_class = Pattern.compile("\\[([\\s\\S]{1,4})\\]")
    }

    private val _data: MutableList<PartialPost> = mutableListOf()

    @Throws(Exception::class)
    fun loadBroadListData(broadName: String, page: Int): MutableList<PartialPost> {
        _data.clear()
        val request = Request.Builder()
            .url("$hostUrl/api/Article/$broadName?page=$page")
            .build()
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
            val boardInfo = all.getJSONObject("boardInfo")
            val boardTitle = boardInfo.getString("title")
            val postList = all.getJSONArray("postList")
            var i = 0
            while (!postList.isNull(i)) {
                val m3 = postList.getJSONObject(i)
                i++
                var title = m3.getString("title")
                var classs = ""
                val m23 = Title_class.matcher(title)
                if (m23.find()) {
                    classs = m23.group(1)
                    if (classs.length <= 6) {
                        val start = title.indexOf("[$classs]")
                        val end = start + classs.length + 2
                        if (start == 0) {
                            title = clearStart(title.substring(end))
                        } else {
                            try {
                                title = (
                                    title.substring(0, start) +
                                        clearStart(title.substring(end))
                                    )
                            } catch (E: Exception) {
                            }
                        }
                    } else {
                        classs = "無分類"
                    }
                }
                val post = PartialPost(
                    title,
                    m3.getString("date"),
                    classs,
                    0,
                    m3.getInt("goup"),
                    m3.getString("author"),
                    false,
                    false,
                    "https://www.ptt.cc" + m3.getString("href")
                )
                _data.add(post)
            }
        }
        return _data
    }
}
