package cc.ptt.android.common.network.api

@Target(AnnotationTarget.CLASS)
annotation class ApiMaxLogLevel(val level: MaxLogLevel)

enum class MaxLogLevel {
    NONE,
    BASIC,
    HEADERS,
    BODY
}
