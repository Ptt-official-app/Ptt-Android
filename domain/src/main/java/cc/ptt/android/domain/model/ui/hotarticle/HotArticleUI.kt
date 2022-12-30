package cc.ptt.android.domain.model.ui.hotarticle

import cc.ptt.android.data.model.remote.article.hotarticle.HotArticle
import cc.ptt.android.data.model.remote.board.article.Article
import java.util.*
import kotlin.math.abs

data class HotArticleUI(
    val type: HotArticleUIType,
    val title: String,
    val date: Long,
    val board: String,
    val `class`: String,
    val auth: String,
    val commit: String,
    val like: String,
    val url: String,
    var readed: Boolean,
    val image: String,
    val originData: HotArticle?
) {
    fun getDateText(): String {
        val timeL = date
        val nowTime = Date().time / 1000
        var showT = ""
        showT = if (abs(nowTime - timeL) < 60 * 60) {
            (abs(nowTime - timeL) / 60).toString() + "分鐘前"
        } else if (abs(nowTime - timeL) < 60 * 60 * 24) {
            (abs(nowTime - timeL) / 60 / 60).toString() + "小時前"
        } else {
            " " + abs(nowTime - timeL) / 60 / 24 / 60 + "天前"
        }
        if (showT.length <= 4) {
            showT = " $showT"
        }
        return showT
    }

    fun getClassText(): String {
        return "$board / $`class`"
    }

    fun toArticle(): Article? {
        return originData?.let {
            Article(
                articleId = it.aid,
                boardId = it.bid,
                classX = it.`class`,
                createTime = it.createTime,
                deleted = it.deleted,
                index = it.idx,
                mode = it.mode,
                modified = it.modified,
                money = it.money,
                nComments = it.nComments,
                owner = it.owner,
                read = it.read,
                recommend = it.recommend,
                title = it.title,
                url = it.url
            )
        }
    }
}

enum class HotArticleUIType(val value: Int) {
    NORMAL(0),
    TITLE(1),
    MORE(2)
}
