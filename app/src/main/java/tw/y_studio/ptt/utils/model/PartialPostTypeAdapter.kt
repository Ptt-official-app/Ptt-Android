package tw.y_studio.ptt.utils.model

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import tw.y_studio.ptt.api.model.PartialPost
import java.util.regex.Pattern

class PartialPostTypeAdapter : TypeAdapter<PartialPost>() {
    private val titleClass = Pattern.compile("\\[([\\s\\S]{1,4})\\]")

    override fun write(out: JsonWriter, value: PartialPost?) {
    }

    override fun read(input: JsonReader): PartialPost {
        var title = ""
        var classs = ""
        var date = ""
        var href = ""
        var author = ""
        var board = ""
        var aid = ""
        var goup = 0
        var down = 0
        input.beginObject()
        while (input.hasNext()) {
            when (input.nextName()) {
                "title" -> {
                    val pair = separateTitleAndClass(input.nextString())
                    title = pair.first
                    classs = pair.second
                }
                "date" -> date = input.nextString()
                "href" -> href = input.nextString()
                "author" -> author = input.nextString()
                "board" -> board = input.nextString()
                "aid" -> aid = input.nextString()
                "goup" -> goup = input.nextInt()
                "down" -> down = input.nextInt()
                else -> input.nextString()
            }
        }
        input.endObject()
        return PartialPost(
            title,
            date,
            classs,
            0,
            goup,
            down,
            author,
            board,
            aid,
            read = false,
            deleted = false,
            url = "https://www.ptt.cc$href"
        )
    }

    private fun separateTitleAndClass(source: String): Pair<String, String> {
        val matcher = titleClass.matcher(source)
        return if (matcher.find()) {
            var classs = matcher.group(1) ?: ""
            var title = source
            if (classs.length <= 6) {
                val start = source.indexOf("[$classs]")
                val end = start + classs.length + 2
                title = if (start == 0) {
                    source.substring(end).trim()
                } else {
                    try {
                        source.substring(0, start) + source.substring(end).trim()
                    } catch (E: Exception) {
                        source
                    }
                }
            } else {
                classs = "無分類"
            }
            Pair(title, classs)
        } else {
            Pair(source, "")
        }
    }
}
