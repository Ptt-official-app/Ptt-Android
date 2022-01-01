package cc.ptt.android.common.ptt

import android.graphics.Color
import androidx.annotation.ColorInt
import cc.ptt.android.presentation.common.StaticValue

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
}
