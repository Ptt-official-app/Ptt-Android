@file:JvmName("GsonExtension")
package tw.y_studio.ptt.extension

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken

/**
 * Created by Michael.Lien
 * on 2020/11/27
 */
inline fun <reified T> Gson.fromJson(json: String): T = fromJson(json, object : TypeToken<T>() {}.type)

inline fun <reified T> fromJsonObject(json: String, clazz: Class<T>): T {
    val obj = JsonParser.parseString(json).asJsonObject
    return Gson().fromJson(json, clazz)
}

inline fun <reified T> fromJsonArray(json: String, clazz: Class<T>): List<T> {
    val list = mutableListOf<T>()
    val array = JsonParser.parseString(json).asJsonArray
    array.forEach {
        list.add(Gson().fromJson(it, clazz))
    }
    return list
}
