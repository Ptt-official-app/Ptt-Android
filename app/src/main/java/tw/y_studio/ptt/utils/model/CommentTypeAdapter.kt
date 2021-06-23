package tw.y_studio.ptt.utils.model

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import tw.y_studio.ptt.api.model.Comment
import java.util.regex.Matcher
import java.util.regex.Pattern

class CommentTypeAdapter : TypeAdapter<Comment>() {
    private val ipTimePattern = Pattern.compile("(\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3}) (\\d+/\\d+ \\d+:\\d+)")
    private val contentPattern = Pattern.compile(": ([\\s\\S]*)")

    override fun write(out: JsonWriter, value: Comment?) {
    }

    override fun read(input: JsonReader): Comment {
        var userid = ""
        var tag = ""
        var content = ""
        var ip = ""
        var date = ""
        input.beginObject()
        while (input.hasNext()) {
            when (input.nextName()) {
                "iPdatetime" -> {
                    val pair = separateIpAndDate(input.nextString())
                    ip = pair.first
                    date = pair.second
                }
                "userid" -> userid = input.nextString()
                "tag" -> userid = input.nextString()
                "content" -> content = extractContent(input.nextString())
                else -> input.nextString()
            }
        }
        input.endObject()
        return Comment(
            userid,
            tag,
            content,
            ip,
            date
        )
    }

    private fun separateIpAndDate(source: String): Pair<String, String> {
        val m1: Matcher = ipTimePattern.matcher(source)
        return if (m1.find()) {
            Pair(m1.group(1), m1.group(2))
        } else {
            Pair("", source)
        }
    }

    private fun extractContent(source: String): String {
        val m = contentPattern.matcher(source)
        return if (m.find()) {
            m.group(1)
        } else {
            source
        }
    }
}
