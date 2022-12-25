package cc.ptt.android.common.api

@Target(AnnotationTarget.CLASS)
annotation class ApiMaxLogLevel(val level: MaxLogLevel)

enum class MaxLogLevel {
    NONE,
    BASIC,
    HEADERS,
    BODY
}
