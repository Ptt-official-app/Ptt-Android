package tw.y_studio.ptt.ui

import android.graphics.Color
import androidx.annotation.ColorInt

object StaticValue {
    var ScreenDensity = 3.0
    @JvmField
    var widthPixels = 1080.0
    @JvmField
    var highPixels = 1920.0
    var densityDpi = 400.0
    @JvmField
    var ThemMode = 0
    @JvmField
    var userDebugMode = true
    val densityRate: Double
        get() = densityDpi / ScreenDensity

    @ColorInt
    var webUrlColor = Color.parseColor("#1d7fe0")
    var ArticleFont_30 = Color.parseColor("#4F4F4F")
    var ArticleFont_31 = Color.parseColor("#990000") // 暗紅
    var ArticleFont_32 = Color.parseColor("#119911") // 深綠
    var ArticleFont_33 = Color.parseColor("#999900") // 土黃
    var ArticleFont_34 = Color.parseColor("#000099") // 深藍
    var ArticleFont_35 = Color.parseColor("#990099") // 紫
    var ArticleFont_36 = Color.parseColor("#009999") // 藍綠
    var ArticleFont_37 = Color.parseColor("#ffffff") // 藍綠
    var ArticleFont_130 = Color.parseColor("#666666") // 銀灰
    var ArticleFont_131 = Color.parseColor("#FF6666") // 大紅
    var ArticleFont_132 = Color.parseColor("#66ff66") // 淺綠
    var ArticleFont_133 = Color.parseColor("#ffff66") // 亮黃
    var ArticleFont_134 = Color.parseColor("#6666ff") // 正藍
    var ArticleFont_135 = Color.parseColor("#FF66FF") // 粉紅
    var ArticleFont_136 = Color.parseColor("#66ffff") //
    var ArticleFont_137 = Color.parseColor("#ffffff") // 藍綠
    var ArticleFont_ANSI_30 = Color.parseColor("#4F4F4F")
    var ArticleFont_ANSI_31 = Color.parseColor("#990000") // 暗紅
    var ArticleFont_ANSI_32 = Color.parseColor("#119911") // 深綠
    var ArticleFont_ANSI_33 = Color.parseColor("#999900") // 土黃
    var ArticleFont_ANSI_34 = Color.parseColor("#000099") // 深藍
    var ArticleFont_ANSI_35 = Color.parseColor("#990099") // 紫
    var ArticleFont_ANSI_36 = Color.parseColor("#009999") // 藍綠
    var ArticleFont_ANSI_37 = Color.parseColor("#ffffff") // 藍綠
    var ArticleFont_ANSI_130 = Color.parseColor("#666666") // 銀灰
    var ArticleFont_ANSI_131 = Color.parseColor("#FF6666") // 大紅
    var ArticleFont_ANSI_132 = Color.parseColor("#66ff66") // 淺綠
    var ArticleFont_ANSI_133 = Color.parseColor("#ffff66") // 亮黃
    var ArticleFont_ANSI_134 = Color.parseColor("#6666ff") // 正藍
    var ArticleFont_ANSI_135 = Color.parseColor("#FF66FF") // 粉紅
    var ArticleFont_ANSI_136 = Color.parseColor("#66ffff") //
    var ArticleFont_ANSI_137 = Color.parseColor("#ffffff") // 藍綠
    var ArticleBack_40 = Color.parseColor("#00ffffff")
    var ArticleBack_41 = Color.parseColor("#bb0000") // 暗紅
    var ArticleBack_42 = Color.parseColor("#00bb00") // 深綠
    var ArticleBack_43 = Color.parseColor("#bbbb00") // 土黃
    var ArticleBack_44 = Color.parseColor("#0000bb") // 深藍
    var ArticleBack_45 = Color.parseColor("#bb00bb") // 紫
    var ArticleBack_46 = Color.parseColor("#00bbbb") // 藍綠
    var ArticleBack_47 = Color.parseColor("#bbbbbb") // 藍綠
    var ArticleBack_ANSI_40 = Color.parseColor("#000000")
    var ArticleBack_ANSI_41 = Color.parseColor("#bb0000") // 暗紅
    var ArticleBack_ANSI_42 = Color.parseColor("#00bb00") // 深綠
    var ArticleBack_ANSI_43 = Color.parseColor("#bbbb00") // 土黃
    var ArticleBack_ANSI_44 = Color.parseColor("#0000bb") // 深藍
    var ArticleBack_ANSI_45 = Color.parseColor("#bb00bb") // 紫
    var ArticleBack_ANSI_46 = Color.parseColor("#00bbbb") // 藍綠
    var ArticleBack_ANSI_47 = Color.parseColor("#bbbbbb") // 藍綠
    var Article_reply_auth = Color.parseColor("#ffffff") // 藍綠
}
