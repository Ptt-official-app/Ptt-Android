package tw.y_studio.ptt.utils;

import android.graphics.Color;
import android.graphics.text.LineBreaker;
import android.os.Build;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.widget.TextView;

import tw.y_studio.ptt.ui.StaticValue;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    public static final Pattern UrlPattern =
            Pattern.compile(
                    "(http|https|line)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");

    public static String notNullString(String input) {
        if (input == null) {
            return "";
        } else {
            return input;
        }
    }

    public static boolean isAccount(String account) {
        if (account == null) return false;
        if (account.length() < 2 && account.length() > 0) return false;
        account = account.toLowerCase();
        for (int i = 0; i < account.length(); i++) {
            // System.out.println(account.charAt(i));
            if (!((account.charAt(i) >= 'a' && account.charAt(i) <= 'z')
                    || (account.charAt(i) >= '0' && account.charAt(i) <= '9'))) {
                return false;
            }
        }
        return true;
    }

    public static final Pattern imageUriPattern =
            Pattern.compile(
                    "(http|https)://(([-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|].(jpg|png|jpeg|gif|webp|gifv))|(([im].){0,1}imgur.com/[a-zA-Z0-9]{7,10}[./]{0,1}))");
    public static final Pattern colorFlag = Pattern.compile("\\[\0\\d+\0\\]");

    public static List<String> getImgUrl(String input) {
        List<String> outPut = new ArrayList<>();
        if (input.length() == 0) return outPut;
        if (!input.contains("http")) return outPut;
        Matcher m5 = colorFlag.matcher(input);
        while (m5.find()) {
            input = input.replace(m5.group(), "");
        }

        String temp = input;
        Matcher m = imageUriPattern.matcher(temp);
        while (m.find()) {
            String uri = m.group();
            if (uri.contains("imgur.com")) {
                String subname = uri.toLowerCase();

                if (subname.contains(".jpg")
                        || subname.contains(".png")
                        || subname.contains(".gif")
                        || subname.contains(".jpeg")
                        || subname.contains(".webp")) {
                } else {
                    uri += ".jpg";
                }
                uri = uri.replace("gifv", "gif");

                if (uri.contains("..jpg") || uri.contains("/.jpg")) continue;
                if (uri.contains("imgur.com/a/")) continue;
            }
            outPut.add(uri);
        }

        return outPut;
    }

    public static String notNullImageString(Object input) {
        if (input == null) {
            return "";
        } else {
            String uri = input.toString();
            if (uri.contains("imgur.com")) {

                String subname = uri.toLowerCase();

                if (subname.contains(".jpg")
                        || subname.contains(".png")
                        || subname.contains(".gif")
                        || subname.contains(".jpeg")
                        || subname.contains(".webp")) {
                } else {
                    uri += ".jpg";
                }
                uri = uri.replace("gifv", "gif");

                if (uri.contains("..jpg") || uri.contains("/.jpg")) return input.toString();
                if (uri.contains("imgur.com/a/")) return input.toString();
                return uri;
            } else {
                return input.toString();
            }
        }
    }

    public static String notNullString(Object input) {
        if (input == null) {
            return "";
        } else {
            if (input instanceof Integer) {
                return (int) input + "";
            } else if (input instanceof ArrayList) {
                ArrayList al = (ArrayList) input;
                StringBuilder ns = new StringBuilder();
                int i = 0;
                for (Object mm : al) {
                    ns.append(notNullString(mm).replace("\n", "").replace(" ", ""));
                    if (++i > 10) break;
                }
                return ns.toString();
            } else {
                return input.toString();
            }
        }
    }

    public static void TextViewAutoSplitFix(TextView mText) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mText.setBreakStrategy(LineBreaker.BREAK_STRATEGY_SIMPLE);
            mText.setHyphenationFrequency(Layout.HYPHENATION_FREQUENCY_NONE);
        }
    }

    public static String clearStart(String input) {
        String outPut = "";
        if (input != null) {
            outPut = input;
            if (input.length() > 1) {
                for (int i = 0; i < input.length(); i++) {
                    if (input.charAt(i) != ' ') {
                        outPut = input.substring(i);
                        break;
                    }
                }
            }
        }
        return outPut;
    }

    public static SpannableStringBuilder ColorString(String input) {
        List<Integer> listStart = new ArrayList<>();
        List<Integer> listEnd = new ArrayList<>();
        List<String> listColor = new ArrayList<>();
        // Utils.LOG("ColorString","input="+input);
        String temp = input;

        final Pattern p23 = Pattern.compile("\\[\0(\\d+)\0\\]");
        Matcher m23 = p23.matcher(input);
        while (m23.find()) {
            // output=output.replace(,"");

            temp = temp.replace(m23.group(), "");
        }

        SpannableStringBuilder sp = new SpannableStringBuilder(temp);

        try {
            int start = -1, end = -1, lastEnd = -1, lastStart = -1, mine = 0;

            for (int i = 1; i < (input.length() - 1); i++) {
                if (input.charAt(i) == '\0') {
                    if (input.charAt(i + 1) == ']') {
                        end = i;
                    } else if (input.charAt(i + -1) == '[') {
                        start = i;
                    }
                    if (start != -1 && end != -1) {
                        listStart.add(start);
                        listEnd.add(end);
                        listColor.add(input.substring(start + 1, end));
                        end = -1;
                        start = -1;
                    }
                }
            }

            for (int j = 0; j < listColor.size() - 1; j++) {
                mine += listColor.get(j).length() + 4;
                sp.setSpan(
                        new ForegroundColorSpan(ColorTransFront(listColor.get(j))),
                        listEnd.get(j) + 2 - mine,
                        listStart.get(j + 1) - 1 - mine,
                        Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                String colorint = listColor.get(j).replace(" ", "");
                if (colorint.length() > 1) {
                    if (colorint.charAt(colorint.length() - 2) != '0') {
                        sp.setSpan(
                                new BackgroundColorSpan(ColorTransBack(colorint)),
                                listEnd.get(j) + 2 - mine,
                                listStart.get(j + 1) - 1 - mine,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }

            String temp2 = temp;

            Matcher m = UrlPattern.matcher(temp2);
            while (m.find()) {
                String urlTemp = m.group();
                int Start = temp.indexOf(urlTemp);
                int endd = Start + urlTemp.length();
                // System.out.println("matcher.group():\t"+m.group()+"\n");

                sp.setSpan(new URLSpan(urlTemp), Start, endd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                sp.setSpan(
                        new ForegroundColorSpan(StaticValue.webUrlColor),
                        Start,
                        endd,
                        Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            }
        } catch (Exception e) {
        }

        return sp;
    }

    public static int ColorTransFront(String input) {
        int oo = 0;
        input = input.replace(" ", "");
        input = input.replace("\0", "");
        input = input.replace("[", "");
        input = input.replace("]", "");
        if (input.length() > 2) {
            if (input.charAt(input.length() - 3) == '1') {
                switch (input.charAt(input.length() - 1)) {
                    case '0':
                        oo = StaticValue.ArticleFont_130;
                        break;
                    case '1':
                        oo = StaticValue.ArticleFont_131;
                        break;
                    case '2':
                        oo = StaticValue.ArticleFont_132;
                        break;
                    case '3':
                        oo = StaticValue.ArticleFont_133;
                        break;
                    case '4':
                        oo = StaticValue.ArticleFont_134;
                        break;
                    case '5':
                        oo = StaticValue.ArticleFont_135;
                        break;
                    case '6':
                        oo = StaticValue.ArticleFont_136;
                        break;
                    case '7':
                    default:
                        if (StaticValue.ThemMode == 1) {
                            // mainLayout.setBackgroundColor(Color.parseColor("#ffffff"));
                            if (StaticValue.ArticleFont_137 == Color.WHITE) {
                                oo = Color.BLACK;
                            } else {
                                oo = StaticValue.ArticleFont_137;
                            }
                        } else {
                            oo = StaticValue.ArticleFont_137;
                        }

                        break;
                }
            } else {
                if (StaticValue.ThemMode == 1) {
                    if (StaticValue.ArticleFont_137 == Color.WHITE) {
                        oo = Color.BLACK;
                    } else {
                        oo = StaticValue.ArticleFont_137;
                    }
                } else {
                    oo = StaticValue.ArticleFont_137;
                }
            }
        } else {
            switch (input.charAt(input.length() - 1)) {
                case '0':
                    oo = StaticValue.ArticleFont_30;
                    break;
                case '1':
                    oo = StaticValue.ArticleFont_31;
                    break;
                case '2':
                    oo = StaticValue.ArticleFont_32;
                    break;
                case '3':
                    oo = StaticValue.ArticleFont_33;
                    break;
                case '4':
                    oo = StaticValue.ArticleFont_34;
                    break;
                case '5':
                    oo = StaticValue.ArticleFont_35;
                    break;
                case '6':
                    oo = StaticValue.ArticleFont_36;
                    break;
                case '7':
                default:
                    if (StaticValue.ThemMode == 1) {
                        // mainLayout.setBackgroundColor(Color.parseColor("#ffffff"));
                        if (StaticValue.ArticleFont_37 == Color.WHITE) {
                            oo = Color.BLACK;
                        } else {
                            oo = StaticValue.ArticleFont_37;
                        }
                    } else {
                        oo = StaticValue.ArticleFont_37;
                    }

                    break;
            }
        }

        return oo;
    }

    public static int ColorTransBack(String input) {
        int oo = 0;
        if (input.length() > 1) {
            switch (input.charAt(input.length() - 2)) {
                case '0':
                default:
                    oo = StaticValue.ArticleBack_40;
                    break;
                case '1':
                    oo = StaticValue.ArticleBack_41;
                    break;
                case '2':
                    oo = StaticValue.ArticleBack_42;
                    break;
                case '3':
                    oo = StaticValue.ArticleBack_43;
                    break;
                case '4':
                    oo = StaticValue.ArticleBack_44;
                    break;
                case '5':
                    oo = StaticValue.ArticleBack_45;
                    break;
                case '6':
                    oo = StaticValue.ArticleBack_46;
                    break;
                case '7':
                    oo = StaticValue.ArticleBack_47;

                    break;
            }
        } else {
            switch (StaticValue.ThemMode) {
                case 0:
                    oo = Color.parseColor("#313131");
                    break;
                case 1:
                    oo = Color.parseColor("#000000");
                    break;
            }
        }

        return oo;
    }

    public static SortDecimal sortDecimal(String input) {
        int like = 0;
        try {
            like = Integer.parseInt(notNullString(input));
        } catch (Exception e) {
        }
        if (like > 1000) {
            DecimalFormat fnum = new DecimalFormat("##0.0");
            String dd = fnum.format((((double) like) / 1000d));
            return new SortDecimal(dd + "k", true, like);
        } else {
            return new SortDecimal(like + "", false, like);
        }
    }

    public static class SortDecimal {
        private boolean overDecimal = false;
        private int orgDecimal = 0;
        private String sortDecimal = "";

        public SortDecimal(String sortDecimal, boolean overDecimal, int orgDecimal) {
            this.sortDecimal = sortDecimal;
            this.orgDecimal = orgDecimal;
            this.overDecimal = overDecimal;
        }

        public boolean isOverDecimal() {
            return overDecimal;
        }

        public void setOverDecimal(boolean overDecimal) {
            this.overDecimal = overDecimal;
        }

        public int getOrgDecimal() {
            return orgDecimal;
        }

        public void setOrgDecimal(int orgDecimal) {
            this.overDecimal = overDecimal;
        }

        public String getSortDecimal() {
            return sortDecimal;
        }

        public void setSortDecimal(String sortDecimal) {
            this.sortDecimal = sortDecimal;
        }

        public String toString() {
            return sortDecimal;
        }
    }
}
