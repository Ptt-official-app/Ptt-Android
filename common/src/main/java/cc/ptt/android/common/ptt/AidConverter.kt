package cc.ptt.android.common.ptt

import java.util.*
import java.util.regex.Pattern
import kotlin.Exception

object AidConverter {
    private const val DOMAIN_URL = "https://www.ptt.cc/bbs/"
    private const val FILE_EXT = ".html"
    private const val aidTable = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz-_"

    private val table = tableInitializer()
    private val inverseTable = inverseTableInitializer()

    private val boardNameChangeMapping: Map<String, String> = mapOf("iPhone" to "iOS")

    private fun checkBoardNameChange(boardName: String): String {
        return boardNameChangeMapping[boardName] ?: boardName
    }

    /**
     * 建立文章編號字元 Map, 方便取得對應數值
     *
     * @return 文章編號字元 Map
     */
    private fun tableInitializer(): HashMap<String, Long> {
        val table = HashMap<String, Long>()
        var index: Long = 0
        val size = aidTable.length
        for (i in 0 until size) {
            table[aidTable[i].toString() + ""] = index
            index++
        }
        return table
    }

    private fun inverseTableInitializer(): HashMap<Long, String> {
        val inverseTable = HashMap<Long, String>()
        val index: Long = 0
        val size = aidTable.length
        for (i in 0 until size) {
            inverseTable[i.toLong()] = aidTable[i].toString() + ""
        }
        return inverseTable
    }

    /**
     * 將檔案名稱轉換為˙數字型態的文章序號
     *
     * @param fn 檔案名稱
     * @return 數字型態的文章序號, 若檔案名稱格式不符將回傳 0<br></br>
     * 轉換後的文章編號將符合 [M|G].[unsigned_integer].A.[HEX{3}]<br></br>
     * 範例: M.1451100858.A.71E
     */
    private fun fn2aidu(fn: String?): Long {
        var aidu: Long = 0
        var type: Long = 0
        var v1: Long = 0
        var v2: Long = 0

        if (fn == null) return 0
        val fnList: MutableList<String> = ArrayList()
        var lastEnd = 0
        for (k in 0 until fn.length + 1) {
            if (k == fn.length) {
                fnList.add(fn.substring(lastEnd, k))
                break
            }
            if (fn[k] == '.') {
                fnList.add(fn.substring(lastEnd, k))
                lastEnd = k + 1
            }
        }

        if (fnList.size != 4) return 0
        val typeString = fnList[0]
        val v1String = fnList[1]
        val v2String = fnList[3]

        if (fnList[2] != "A") return 0
        if (!isNumeric(v1String) || v1String.length != 10) return 0
        type = when (typeString) {
            "M" -> 0
            "G" -> 1
            else -> return 0
        }
        v1 = v1String.toLong()
        v2 = v2String.toLong(16)
        aidu = type and 0xf shl 44 or (v1 and 0xffffffffL shl 12) or (v2 and 0xfff)

        return aidu
    }

    /**
     * 將數字型態的文章序號轉換為字串型態的文章編號
     *
     * @param aidU 數字型態之文章序號
     * @return 轉換後的文章編號將符合 [a-zA-Z-_]{8}<br></br>
     * 範例: 1MVWgwSU
     */
    private fun aidU2aidC(aidU: Long): String? {
        var aidu = aidU
        val size = table.size

        val stringBuffer = StringBuffer()
        while (stringBuffer.length < 8) {
            val v = aidu % size
            if (!inverseTable.containsKey(v)) return null
            stringBuffer.insert(0, inverseTable[v])
            aidu /= size
        }
        return stringBuffer.toString()
    }

    /**
     * 將文章編號轉換為數字型態的文章序號
     *
     * @param aid 文章編號
     * @return 數字型態的文章序號
     */
    private fun aidC2aidU(aid: String): Long {
        var aidu: Long = 0
        for (element in aid) {
            if (element == '@') break
            if (!table.containsKey(element.toString())) return 0
            val v = table[element.toString()]!!
            aidu = aidu shl 6
            aidu = aidu or (v and 0x3f)
        }
        return aidu
    }

    /**
     * 將文章序號(數字型態)轉換為檔案名稱
     *
     * @param aidU 文章序號(數字型態)
     * @return 轉換後的檔案名稱, 格式將符合 [M|G].[unsigned_integer].A.[HEX{3}]<br></br>
     * 最後的16進位表示法若未滿3個字將以0從左邊開始補齊<br></br>
     * 範例: M.1451100858.A.71E
     */
    private fun aidU2fn(aidU: Long): String {
        val type = aidU shr 44 and 0xf
        val v1 = aidU shr 12 and 0xffffffffL
        val v2 = aidU and 0xfff

        val hex = java.lang.Long.toHexString(v2).uppercase(Locale.getDefault())
        return (if (type == 0L) "M" else "G") + "." + v1 + ".A." + stringLeftPad(hex, 3, "0")
    }

    /**
     * 將文章編號轉換為檔案名稱
     *
     * @param aid 文章編號
     * @return 轉換後的檔案名稱, 格式將符合 [M|G].[unsigned_integer].A.[HEX{3}]<br></br>
     * 最後的16進位表示法若未滿3個字將以0從左邊開始補齊<br></br>
     * 範例: M.1451100858.A.71E
     */
    private fun aidToFileName(aid: String): String {
        return try {
            aidU2fn(aidC2aidU(aid))
        } catch (e: Exception) {
            throw Exception("Not correct aid")
        }
    }

    /**
     * 將文章編號轉換為 WEB 版 URL
     *
     * @param boardTitle 文章所屬看板名稱
     * @param aid 文章編號
     * @return WEB 版的完整 URL
     */
    fun aidToUrl(aidBean: AidBean): String {
        return if (aidBean.isEmpty()) {
            ""
        } else {
            "$DOMAIN_URL${checkBoardNameChange(aidBean.boardName!!)}/" + aidToFileName(
                aidBean.aid!!
            ) + FILE_EXT
        }
    }

    /**
     * 將檔案名稱(也就是 URL 的最後一段 不包含副檔名)轉換為文章編號
     *
     * @param fileName 檔案名稱
     * @return 轉換後的文章編號, 若檔案名稱格式不符則將回傳 null
     */
    private fun fileNameToAid(fileName: String?): String? {
        val temp = fn2aidu(fileName)
        return aidU2aidC(temp)
    }

    /**
     * 將 URL 轉換為 AID 物件
     *
     * @param url PTT WEB 版的 URL
     * @return 物件內包含 文章編號 與 看板名
     * @see AidBean
     */
    fun urlToAid(url: String): AidBean {
        var url = url
        try {
            url = url.replace("https:", "").replace("http:", "").replace(" ", "").replace("//", "")
            val urlList = splitterString("/", url)
            if (url.indexOf("www.ptt.cc") == -1) return AidBean()
            var fileWhere = -1
            if (urlList.size in 2..5) {
                for (i in urlList.indices) {
                    if (urlList[i].indexOf(".htm") != -1 ||
                        urlList[i].indexOf(".html") != -1
                    ) {
                        fileWhere = i
                    }
                }
                if (fileWhere > 1) {
                    val boardName = urlList[fileWhere - 1]
                    val fileName = urlList[fileWhere].replace("\\.(html|htm)".toRegex(), "")
                    val aid = fileNameToAid(fileName)
                    return AidBean(checkBoardNameChange(boardName), aid)
                }
            }
        } catch (e: Exception) {
        }
        return AidBean()
    }

    private fun splitterString(cut: String, input: String): List<String> {
        val output: MutableList<String> = ArrayList()
        val cmds = input.split(cut.toRegex()).toTypedArray()
        for (cmd in cmds) {
            output.add(cmd)
        }
        return output
    }

    private fun isNumeric(str: String): Boolean {
        val pattern = Pattern.compile("[0-9]*")
        val isNum = pattern.matcher(str)
        return isNum.matches()
    }

    private fun stringLeftPad(input: String, num: Int, put: String): String {
        var input = input
        while (input.length < num) {
            input = put + input
        }
        return input
    }
}
