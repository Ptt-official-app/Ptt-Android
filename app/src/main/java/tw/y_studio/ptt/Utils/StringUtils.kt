@file:JvmName("StringUtils2")
package tw.y_studio.ptt.Utils

import android.graphics.text.LineBreaker
import android.os.Build
import android.text.Layout
import android.widget.TextView
import java.util.regex.Pattern

// TODO use URLUtil.isValidUrl(url)?
private const val URL_PATTERN = "(http|https|line)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]"
private const val URL_IMG_PATTERN = "(http|https)://(([-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|].(jpg|png|jpeg|gif|webp|gifv))|(([im].){0,1}imgur.com/[a-zA-Z0-9]{7,10}[./]{0,1}))"
private const val ACCOUNT_PATTERN = "[a-zA-Z0-9]{2,}"

val UrlPattern = Pattern.compile(URL_PATTERN)
private val imageUriPattern = Pattern.compile(URL_IMG_PATTERN)

// TODO not need if all converted to Kotlin
fun notNullString(input: String?) = input ?: ""

// TODO param account should not be nullable
// FIXME whats the maximum length of account name?
fun isAccount(account: String?): Boolean {
    account?.apply {
        ACCOUNT_PATTERN.toRegex().matchEntire(account)?.apply {
            return true
        } ?: run {
            return false
        }
    } ?: run {
        return false
    }
    return false
}

// public static List<String> getImgUrl(String input)

// public static String notNullImageString(Object input)

// public static String notNullString(Object input)

fun TextViewAutoSplitFix(mText: TextView) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            mText.breakStrategy = LineBreaker.BREAK_STRATEGY_SIMPLE
        }
        mText.hyphenationFrequency = Layout.HYPHENATION_FREQUENCY_NONE
    }
}

// TODO not need if all converted to Kotlin
fun clearStart(input: String): String = input.trim()
