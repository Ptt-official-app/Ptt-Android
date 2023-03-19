package cc.ptt.android.common.ptt

import android.graphics.Color
import androidx.annotation.ColorInt
import cc.ptt.android.common.StaticValue

object PttColor {

    @ColorInt
    fun colorTrans(input: String): Int {
        return when (input[input.length - 1]) {
            '0' -> Color.parseColor("#666666") // 銀灰
            '1' -> Color.parseColor("#FF6666") // 大紅
            '2' -> Color.parseColor("#66ff66") // 淺綠
            '3' -> Color.parseColor("#ffff66") // 亮黃
            '4' -> Color.parseColor("#6666ff") // 正藍
            '5' -> Color.parseColor("#FF66FF") // 粉紅
            '6' -> Color.parseColor("#66ffff") //
            '7' ->
                if (StaticValue.ThemMode == 1) {
                    Color.BLACK
                } else {
                    Color.WHITE
                }
            else ->
                if (StaticValue.ThemMode == 1) {
                    Color.BLACK
                } else {
                    Color.WHITE
                }
        }
    }

    @ColorInt
    fun foregroundColor(int: Int, isHighlight: Boolean): Int {
        return if (isHighlight) {
            when (int) {
                30 -> StaticValue.ArticleFont_130
                31 -> StaticValue.ArticleFont_131
                32 -> StaticValue.ArticleFont_132
                33 -> StaticValue.ArticleFont_133
                34 -> StaticValue.ArticleFont_134
                35 -> StaticValue.ArticleFont_135
                36 -> StaticValue.ArticleFont_136
                37 ->
                    if (StaticValue.ThemMode == 1) {
                        Color.BLACK
                    } else {
                        Color.WHITE
                    }
                else -> throw IllegalStateException("unknown foreground color: $int")
            }
        } else {
            when (int) {
                30 -> StaticValue.ArticleFont_30
                31 -> StaticValue.ArticleFont_31
                32 -> StaticValue.ArticleFont_32
                33 -> StaticValue.ArticleFont_33
                34 -> StaticValue.ArticleFont_34
                35 -> StaticValue.ArticleFont_35
                36 -> StaticValue.ArticleFont_36
                37 ->
                    if (StaticValue.ThemMode == 1) {
                        Color.BLACK
                    } else {
                        Color.WHITE
                    }
                else -> throw IllegalStateException("unknown foreground color: $int")
            }
        }
    }

    @ColorInt
    fun backgroundColor(int: Int): Int {
        return when (int) {
            40 -> Color.TRANSPARENT
            41 -> StaticValue.ArticleBack_41
            42 -> StaticValue.ArticleBack_42
            43 -> StaticValue.ArticleBack_43
            44 -> StaticValue.ArticleBack_44
            45 -> StaticValue.ArticleBack_45
            46 -> StaticValue.ArticleBack_46
            47 -> StaticValue.ArticleBack_47
            else -> throw IllegalStateException("unknown background color: $int")
        }
    }
}
