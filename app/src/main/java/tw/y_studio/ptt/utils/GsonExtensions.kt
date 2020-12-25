package tw.y_studio.ptt.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Created by Michael.Lien
 * on 2020/12/25
 */
inline fun <reified T> Gson.fromJson(json: String): T = fromJson(json, object : TypeToken<T>() {}.type)
