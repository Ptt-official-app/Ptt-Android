package cc.ptt.android.common.date

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

object DateFormatUtils {

    @SuppressLint("SimpleDateFormat")
    fun secondsToDateTime(
        seconds: Long,
        pattern: String,
        timeZone: TimeZone = TimeZone.getDefault()
    ): String {
        val dateFormat = SimpleDateFormat(pattern).also {
            it.timeZone = timeZone
        }
        return dateFormat.format(Date(seconds * 1000))
    }
}
