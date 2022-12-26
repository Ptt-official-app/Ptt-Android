package cc.ptt.android.common.api

import java.io.IOException

class ApiException(val code: Int, private val msg: String) : IOException(msg)
