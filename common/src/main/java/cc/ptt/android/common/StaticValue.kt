package cc.ptt.android.common

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

    var backgroundColor: Int = 0

    @ColorInt
    var webUrlColor = Color.parseColor("#1d7fe0")

    val ArticleFont_30 = Color.parseColor("#4F4F4F")
    val ArticleFont_31 = Color.parseColor("#990000") // 暗紅
    val ArticleFont_32 = Color.parseColor("#119911") // 深綠
    val ArticleFont_33 = Color.parseColor("#999900") // 土黃
    val ArticleFont_34 = Color.parseColor("#000099") // 深藍
    val ArticleFont_35 = Color.parseColor("#990099") // 紫
    val ArticleFont_36 = Color.parseColor("#009999") // 藍綠
    val ArticleFont_37 = Color.parseColor("#ffffff") // 藍綠

    val ArticleFont_130 = Color.parseColor("#666666") // 銀灰
    val ArticleFont_131 = Color.parseColor("#FF6666") // 大紅
    val ArticleFont_132 = Color.parseColor("#66ff66") // 淺綠
    val ArticleFont_133 = Color.parseColor("#ffff66") // 亮黃
    val ArticleFont_134 = Color.parseColor("#6666ff") // 正藍
    val ArticleFont_135 = Color.parseColor("#FF66FF") // 粉紅
    val ArticleFont_136 = Color.parseColor("#66ffff") //
    val ArticleFont_137 = Color.parseColor("#ffffff") // 藍綠

    val ArticleBack_40 = Color.parseColor("#00ffffff")
    val ArticleBack_41 = Color.parseColor("#bb0000") // 暗紅
    val ArticleBack_42 = Color.parseColor("#00bb00") // 深綠
    val ArticleBack_43 = Color.parseColor("#bbbb00") // 土黃
    val ArticleBack_44 = Color.parseColor("#0000bb") // 深藍
    val ArticleBack_45 = Color.parseColor("#bb00bb") // 紫
    val ArticleBack_46 = Color.parseColor("#00bbbb") // 藍綠
    val ArticleBack_47 = Color.parseColor("#bbbbbb") // 藍綠
}
