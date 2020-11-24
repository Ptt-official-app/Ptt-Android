package tw.y_studio.ptt.utils.model

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import tw.y_studio.ptt.model.Comment
import tw.y_studio.ptt.model.Post
import tw.y_studio.ptt.utils.StringUtils.clearStart
import java.util.regex.Matcher
import java.util.regex.Pattern

class PostTypeAdapter : TypeAdapter<Post>() {
    private val titlePattern = Pattern.compile("\\[([\\s\\S]{1,4})\\]")

    override fun write(out: JsonWriter, value: Post?) {
    }

    override fun read(input: JsonReader): Post {
        var title = ""
        var classString = ""
        var date = ""
        var author = ""
        var nickname = ""
        var content = ""
        val comments = mutableListOf<Comment>()
        val commentTypeAdapter = CommentTypeAdapter()
        input.beginObject()
        while (input.hasNext()) {
            when (input.nextName()) {
                "title" -> {
                    val titleAndClass = separateTitle(input.nextString())
                    title = titleAndClass.first
                    classString = titleAndClass.second
                }
                "author" -> author = input.nextString()
                "nickname" -> nickname = input.nextString()
                "date" -> date = input.nextString()
                "content" -> content = input.nextString()
                "comments" -> {
                    input.beginArray()
                    while (input.hasNext()) {
                        comments.add(commentTypeAdapter.read(input))
                    }
                    input.endArray()
                }
                else -> input.nextString()
            }
        }
        input.endObject()
        return Post(
            title,
            classString,
            date,
            author,
            nickname,
            content,
            comments
        )
    }

    private fun separateTitle(source: String): Pair<String, String> {
        var classs = ""
        var title = source
        val m23: Matcher = titlePattern.matcher(title)
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
        return Pair(title, classs)
    }
}
