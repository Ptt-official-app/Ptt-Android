package cc.ptt.android

import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Created by Michael.Lien
 * on 2021/2/1
 */
object TestJsonFileUtils {
    fun loadJsonFile(fileName: String): String {
        val classloader = javaClass.classLoader
        val inputStream = classloader.getResourceAsStream(fileName)
        val builder = StringBuilder()
        val buffer = CharArray(1024)
        val reader = BufferedReader(InputStreamReader(inputStream))
        var n: Int
        while (true) {
            n = reader.read(buffer)
            if (n < 0) break
            builder.append(buffer, 0, n)
        }
        inputStream.close()

        return builder.toString()
    }
}
