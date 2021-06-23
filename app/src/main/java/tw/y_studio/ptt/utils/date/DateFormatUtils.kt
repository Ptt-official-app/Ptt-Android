package tw.y_studio.ptt.utils.date

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object DateFormatUtils {

    fun secondsToDateTime(
        seconds: Long,
        pattern: String,
        zoneId: ZoneId = ZoneId.systemDefault()
    ): String {
        val time = LocalDateTime.ofInstant(
            Instant.ofEpochSecond(seconds),
            zoneId
        )

        return time.format(DateTimeFormatter.ofPattern(pattern)) ?: throw NullPointerException("Date format failed.")
    }

    fun milliSecondsToDateTime(
        milliSeconds: Long,
        pattern: String,
        zoneId: ZoneId = ZoneId.systemDefault()
    ): String {
        val time = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(milliSeconds),
            ZoneId.systemDefault()
        )

        return time.format(DateTimeFormatter.ofPattern(pattern)) ?: throw NullPointerException("Date format failed.")
    }

    fun dateStringToTimeSeconds(
        dateString: String,
        pattern: String,
        zoneId: ZoneId = ZoneId.systemDefault()
    ): Long {
        val dateTime = ZonedDateTime.of(
            LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern(pattern)),
            zoneId
        )

        return dateTime.toEpochSecond()
    }
}
