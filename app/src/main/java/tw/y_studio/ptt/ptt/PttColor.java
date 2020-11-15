package tw.y_studio.ptt.ptt;

import android.graphics.Color;

import tw.y_studio.ptt.ui.StaticValue;

public class PttColor {

    public static int ColorTrans(String input) {
        int oo = 0;
        switch (input.charAt(input.length() - 1)) {
            case '0':
                oo = Color.parseColor("#666666"); // 銀灰
                break;
            case '1':
                oo = Color.parseColor("#FF6666"); // 大紅
                break;
            case '2':
                oo = Color.parseColor("#66ff66"); // 淺綠
                break;
            case '3':
                oo = Color.parseColor("#ffff66"); // 亮黃
                break;
            case '4':
                oo = Color.parseColor("#6666ff"); // 正藍
                break;
            case '5':
                oo = Color.parseColor("#FF66FF"); // 粉紅
                break;
            case '6':
                oo = Color.parseColor("#66ffff"); //
                break;
            case '7':
            default:
                if (StaticValue.ThemMode == 1) {
                    oo = Color.BLACK;
                } else {
                    oo = Color.WHITE;
                }

                break;
        }
        return oo;
    }
}
