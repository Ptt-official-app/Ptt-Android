package cc.ptt.android.common

import android.graphics.Color
import android.graphics.text.LineBreaker
import android.os.Build
import android.text.Layout
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.text.style.URLSpan
import android.widget.TextView
import java.text.DecimalFormat
import java.util.*
import java.util.regex.Pattern

private const val REGEX_URL = "(http|https|line)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]"
private const val REGEX_IMG_URL = "(http|https)://(([-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|].(jpg|png|jpeg|gif|webp|gifv))|(([im].){0,1}imgur.com/[a-zA-Z0-9]{7,10}[./]{0,1}))"
private const val REGEX_COLOR = "\\[\u0000\\d+\u0000\\]"
private const val REGEX_ACCOUNT = "[a-zA-Z0-9]{2,}"

private val imageUriPattern = Pattern.compile(REGEX_IMG_URL)
private val colorPattern = Pattern.compile(REGEX_COLOR)

// TODO the maximum length of account name?
@Deprecated("Refactor this")
object StringUtils {

    @JvmField
    val UrlPattern: Pattern = Pattern.compile(REGEX_URL)

    @JvmStatic
    fun isAccount(account: String?): Boolean {
        account?.apply {
            REGEX_ACCOUNT.toRegex().matchEntire(account)?.apply {
                return true
            } ?: run {
                return false
            }
        } ?: run {
            return false
        }
        return false
    }

    @JvmStatic
    fun getImgUrl(data: String): MutableList<String> {
        var input = data
        val result: MutableList<String> = mutableListOf()
        if (input.isEmpty()) return result
        if (!input.contains("http")) return result
        val colorMatcher = colorPattern.matcher(input)
        while (colorMatcher.find()) {
            input = input.replace(colorMatcher.group(), "")
        }
        val imgurMatcher = imageUriPattern.matcher(input)
        while (imgurMatcher.find()) {
            var uri = imgurMatcher.group()
            if (uri.contains("imgur.com")) {
                if (uri.contains(".jpg", true) ||
                    uri.contains(".png", true) ||
                    uri.contains(".gif", true) ||
                    uri.contains(".jpeg", true) ||
                    uri.contains(".webp", true)
                ) {
                } else {
                    uri += ".jpg"
                }
                uri = uri.replace("gifv", "gif")
                if (uri.contains("..jpg") || uri.contains("/.jpg")) continue
                if (uri.contains("imgur.com/a/")) continue
            }
            result.add(uri)
        }
        return result
    }

    @JvmStatic
    fun notNullImageString(input: Any?): String {
        return if (input == null) "" else {
            var uri = input.toString()
            if (uri.contains("imgur.com")) {
                val subname = uri.toLowerCase(Locale.getDefault())
                if (subname.contains(".jpg") ||
                    subname.contains(".png") ||
                    subname.contains(".gif") ||
                    subname.contains(".jpeg") ||
                    subname.contains(".webp")
                ) {
                } else {
                    uri += ".jpg"
                }
                uri = uri.replace("gifv", "gif")
                if (uri.contains("..jpg") || uri.contains("/.jpg")) return input.toString()
                if (uri.contains("imgur.com/a/")) input.toString() else uri
            } else {
                input.toString()
            }
        }
    }

    @JvmStatic
    fun notNullString(input: Any?): String {
        return if (input == null) "" else {
            if (input is Int) {
                input.toString()
            } else if (input is ArrayList<*>) {
                val ns = StringBuilder()
                for ((i, mm) in input.withIndex()) {
                    ns.append(notNullString(mm).replace("\n", "").replace(" ", ""))
                    if (i + 1 > 10) break
                }
                ns.toString()
            } else {
                input.toString()
            }
        }
    }

    @JvmStatic
    fun TextViewAutoSplitFix(mText: TextView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                mText.breakStrategy = LineBreaker.BREAK_STRATEGY_SIMPLE
            }
            mText.hyphenationFrequency = Layout.HYPHENATION_FREQUENCY_NONE
        }
    }

    // TODO not need if all converted to Kotlin
    @JvmStatic
    fun clearStart(input: String): String = input.trim()

    @JvmStatic
    fun ColorString(input: String): SpannableStringBuilder {
        val listStart: MutableList<Int> = ArrayList()
        val listEnd: MutableList<Int> = ArrayList()
        val listColor: MutableList<String> = ArrayList()
        var temp = input
        val p23 = Pattern.compile(REGEX_COLOR)
        val m23 = p23.matcher(input)
        while (m23.find()) {
            temp = temp.replace(m23.group(), "")
        }
        val sp = SpannableStringBuilder(temp)
        try {
            var start = -1
            var end = -1
            val lastEnd = -1
            val lastStart = -1
            var mine = 0
            for (i in 1 until input.length - 1) {
                if (input[i] == '\u0000') {
                    if (input[i + 1] == ']') {
                        end = i
                    } else if (input[i + -1] == '[') {
                        start = i
                    }
                    if (start != -1 && end != -1) {
                        listStart.add(start)
                        listEnd.add(end)
                        listColor.add(input.substring(start + 1, end))
                        end = -1
                        start = -1
                    }
                }
            }
            for (j in 0 until listColor.size - 1) {
                mine += listColor[j].length + 4
                sp.setSpan(
                    ForegroundColorSpan(ColorTransFront(listColor[j])),
                    listEnd[j] + 2 - mine,
                    listStart[j + 1] - 1 - mine,
                    Spannable.SPAN_EXCLUSIVE_INCLUSIVE
                )
                val colorint = listColor[j].replace(" ", "")
                if (colorint.length > 1) {
                    if (colorint[colorint.length - 2] != '0') {
                        sp.setSpan(
                            BackgroundColorSpan(ColorTransBack(colorint)),
                            listEnd[j] + 2 - mine,
                            listStart[j + 1] - 1 - mine,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                }
            }
            val temp2 = temp
            val m = UrlPattern.matcher(temp2)
            while (m.find()) {
                val urlTemp = m.group()
                val Start = temp.indexOf(urlTemp)
                val endd = Start + urlTemp.length
                sp.setSpan(URLSpan(urlTemp), Start, endd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                sp.setSpan(
                    ForegroundColorSpan(StaticValue.webUrlColor),
                    Start,
                    endd,
                    Spannable.SPAN_EXCLUSIVE_INCLUSIVE
                )
            }
        } catch (e: Exception) {
        }
        return sp
    }

    @JvmStatic
    fun ColorTransFront(input: String): Int {
        var input = input
        var oo = 0
        input = input.replace(" ", "")
        input = input.replace("\u0000", "")
        input = input.replace("[", "")
        input = input.replace("]", "")
        oo = if (input.length > 2) {
            if (input[input.length - 3] == '1') {
                when (input[input.length - 1]) {
                    '0' -> StaticValue.ArticleFont_130
                    '1' -> StaticValue.ArticleFont_131
                    '2' -> StaticValue.ArticleFont_132
                    '3' -> StaticValue.ArticleFont_133
                    '4' -> StaticValue.ArticleFont_134
                    '5' -> StaticValue.ArticleFont_135
                    '6' -> StaticValue.ArticleFont_136
                    '7' ->
                        if (StaticValue.ThemMode == 1) {
                            // mainLayout.setBackgroundColor(Color.parseColor("#ffffff"));
                            if (StaticValue.ArticleFont_137 == Color.WHITE) {
                                Color.BLACK
                            } else {
                                StaticValue.ArticleFont_137
                            }
                        } else {
                            StaticValue.ArticleFont_137
                        }
                    else ->
                        if (StaticValue.ThemMode == 1) {
                            if (StaticValue.ArticleFont_137 == Color.WHITE) {
                                Color.BLACK
                            } else {
                                StaticValue.ArticleFont_137
                            }
                        } else {
                            StaticValue.ArticleFont_137
                        }
                }
            } else {
                if (StaticValue.ThemMode == 1) {
                    if (StaticValue.ArticleFont_137 == Color.WHITE) {
                        Color.BLACK
                    } else {
                        StaticValue.ArticleFont_137
                    }
                } else {
                    StaticValue.ArticleFont_137
                }
            }
        } else {
            when (input[input.length - 1]) {
                '0' -> StaticValue.ArticleFont_30
                '1' -> StaticValue.ArticleFont_31
                '2' -> StaticValue.ArticleFont_32
                '3' -> StaticValue.ArticleFont_33
                '4' -> StaticValue.ArticleFont_34
                '5' -> StaticValue.ArticleFont_35
                '6' -> StaticValue.ArticleFont_36
                '7' ->
                    if (StaticValue.ThemMode == 1) {
                        // mainLayout.setBackgroundColor(Color.parseColor("#ffffff"));
                        if (StaticValue.ArticleFont_37 == Color.WHITE) {
                            Color.BLACK
                        } else {
                            StaticValue.ArticleFont_37
                        }
                    } else {
                        StaticValue.ArticleFont_37
                    }
                else ->
                    if (StaticValue.ThemMode == 1) {
                        if (StaticValue.ArticleFont_37 == Color.WHITE) {
                            Color.BLACK
                        } else {
                            StaticValue.ArticleFont_37
                        }
                    } else {
                        StaticValue.ArticleFont_37
                    }
            }
        }
        return oo
    }

    @JvmStatic
    fun ColorTransBack(input: String): Int {
        var oo = 0
        if (input.length > 1) {
            oo = when (input[input.length - 2]) {
                '0' -> StaticValue.ArticleBack_40
                '1' -> StaticValue.ArticleBack_41
                '2' -> StaticValue.ArticleBack_42
                '3' -> StaticValue.ArticleBack_43
                '4' -> StaticValue.ArticleBack_44
                '5' -> StaticValue.ArticleBack_45
                '6' -> StaticValue.ArticleBack_46
                '7' -> StaticValue.ArticleBack_47
                else -> StaticValue.ArticleBack_40
            }
        } else {
            when (StaticValue.ThemMode) {
                0 -> oo = Color.parseColor("#313131")
                1 -> oo = Color.parseColor("#000000")
            }
        }
        return oo
    }

    @JvmStatic
    fun sortDecimal(input: String?): SortDecimal {
        var like = 0
        try {
            like = notNullString(input).toInt()
        } catch (e: java.lang.Exception) {
        }
        return if (like > 1000) {
            val fnum = DecimalFormat("##0.0")
            val dd = fnum.format(like.toDouble() / 1000.0)
            SortDecimal(dd + "k", true, like)
        } else {
            SortDecimal(like.toString() + "", false, like)
        }
    }

    class SortDecimal(
        var sortDecimal: String = "",
        private var overDecimal: Boolean = false,
        var orgDecimal: Int = 0
    ) {

        fun isOverDecimal(): Boolean = overDecimal

        fun setOverDecimal(overDecimal: Boolean) {
            this.overDecimal = overDecimal
        }

        override fun toString(): String {
            return sortDecimal
        }
    }
}
