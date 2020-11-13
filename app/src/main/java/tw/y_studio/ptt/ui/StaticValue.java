package tw.y_studio.ptt.ui;

import android.graphics.Color;

import androidx.annotation.ColorInt;

public class StaticValue {
    public static double ScreenDensity = 3;
    public static double widthPixels = 1080;
    public static double highPixels = 1920;
    public static double densityDpi = 400;
    public static int ThemMode = 0;
    public static boolean userDebugMode = true;

    public static double getDensityRate() {
        return densityDpi / ScreenDensity;
    }

    @ColorInt public static int webUrlColor = Color.parseColor("#1d7fe0");

    public static int ArticleFont_30 = Color.parseColor("#4F4F4F");
    public static int ArticleFont_31 = Color.parseColor("#990000"); // 暗紅
    public static int ArticleFont_32 = Color.parseColor("#119911"); // 深綠
    public static int ArticleFont_33 = Color.parseColor("#999900"); // 土黃
    public static int ArticleFont_34 = Color.parseColor("#000099"); // 深藍
    public static int ArticleFont_35 = Color.parseColor("#990099"); // 紫
    public static int ArticleFont_36 = Color.parseColor("#009999"); // 藍綠
    public static int ArticleFont_37 = Color.parseColor("#ffffff"); // 藍綠

    public static int ArticleFont_130 = Color.parseColor("#666666"); // 銀灰
    public static int ArticleFont_131 = Color.parseColor("#FF6666"); // 大紅
    public static int ArticleFont_132 = Color.parseColor("#66ff66"); // 淺綠
    public static int ArticleFont_133 = Color.parseColor("#ffff66"); // 亮黃
    public static int ArticleFont_134 = Color.parseColor("#6666ff"); // 正藍
    public static int ArticleFont_135 = Color.parseColor("#FF66FF"); // 粉紅
    public static int ArticleFont_136 = Color.parseColor("#66ffff"); //
    public static int ArticleFont_137 = Color.parseColor("#ffffff"); // 藍綠

    public static int ArticleFont_ANSI_30 = Color.parseColor("#4F4F4F");
    public static int ArticleFont_ANSI_31 = Color.parseColor("#990000"); // 暗紅
    public static int ArticleFont_ANSI_32 = Color.parseColor("#119911"); // 深綠
    public static int ArticleFont_ANSI_33 = Color.parseColor("#999900"); // 土黃
    public static int ArticleFont_ANSI_34 = Color.parseColor("#000099"); // 深藍
    public static int ArticleFont_ANSI_35 = Color.parseColor("#990099"); // 紫
    public static int ArticleFont_ANSI_36 = Color.parseColor("#009999"); // 藍綠
    public static int ArticleFont_ANSI_37 = Color.parseColor("#ffffff"); // 藍綠

    public static int ArticleFont_ANSI_130 = Color.parseColor("#666666"); // 銀灰
    public static int ArticleFont_ANSI_131 = Color.parseColor("#FF6666"); // 大紅
    public static int ArticleFont_ANSI_132 = Color.parseColor("#66ff66"); // 淺綠
    public static int ArticleFont_ANSI_133 = Color.parseColor("#ffff66"); // 亮黃
    public static int ArticleFont_ANSI_134 = Color.parseColor("#6666ff"); // 正藍
    public static int ArticleFont_ANSI_135 = Color.parseColor("#FF66FF"); // 粉紅
    public static int ArticleFont_ANSI_136 = Color.parseColor("#66ffff"); //
    public static int ArticleFont_ANSI_137 = Color.parseColor("#ffffff"); // 藍綠

    public static int ArticleBack_40 = Color.parseColor("#00ffffff");
    public static int ArticleBack_41 = Color.parseColor("#bb0000"); // 暗紅
    public static int ArticleBack_42 = Color.parseColor("#00bb00"); // 深綠
    public static int ArticleBack_43 = Color.parseColor("#bbbb00"); // 土黃
    public static int ArticleBack_44 = Color.parseColor("#0000bb"); // 深藍
    public static int ArticleBack_45 = Color.parseColor("#bb00bb"); // 紫
    public static int ArticleBack_46 = Color.parseColor("#00bbbb"); // 藍綠
    public static int ArticleBack_47 = Color.parseColor("#bbbbbb"); // 藍綠

    public static int ArticleBack_ANSI_40 = Color.parseColor("#000000");
    public static int ArticleBack_ANSI_41 = Color.parseColor("#bb0000"); // 暗紅
    public static int ArticleBack_ANSI_42 = Color.parseColor("#00bb00"); // 深綠
    public static int ArticleBack_ANSI_43 = Color.parseColor("#bbbb00"); // 土黃
    public static int ArticleBack_ANSI_44 = Color.parseColor("#0000bb"); // 深藍
    public static int ArticleBack_ANSI_45 = Color.parseColor("#bb00bb"); // 紫
    public static int ArticleBack_ANSI_46 = Color.parseColor("#00bbbb"); // 藍綠
    public static int ArticleBack_ANSI_47 = Color.parseColor("#bbbbbb"); // 藍綠

    public static int Article_reply_auth = Color.parseColor("#ffffff"); // 藍綠
}
