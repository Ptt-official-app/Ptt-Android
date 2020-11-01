package tw.y_studio.ptt.Ptt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AidConverter {
    private static final String DOMAIN_URL = "https://www.ptt.cc/bbs/";
    private static final String FILE_EXT = ".html";

    private static final String aidTable =
            "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz-_";
    // private static BiMap<Character, Long> table = tableInitializer();
    // private static List<String> table;
    // private static List<Long> inversTablr;

    private static HashMap<String, Long> table = tableInitializer();
    private static HashMap<Long, String> inverseTable = inversTableInitializer();

    /**
     * 建立文章編號字元 Map, 方便取得對應數值
     *
     * @return 文章編號字元 Map
     */
    public static HashMap<String, Long> tableInitializer() {
        HashMap<String, Long> table = new HashMap<>();

        // table=new ArrayList<>();
        // BiMap_Long=new ArrayList<>();
        long index = 0;
        int size = aidTable.length();

        for (int i = 0; i < size; i++) {
            table.put(aidTable.charAt(i) + "", index);
            // table.add(aidTable.charAt(i)+"");
            // table.forcePut( aidTable.charAt(i), index );
            index++;
        }

        return table;
    }

    public static HashMap<Long, String> inversTableInitializer() {
        HashMap<Long, String> inversTable = new HashMap<>();

        // table=new ArrayList<>();
        // BiMap_Long=new ArrayList<>();
        long index = 0;
        int size = aidTable.length();

        for (int i = 0; i < size; i++) {
            inversTable.put((long) i, aidTable.charAt(i) + "");
            // table.add(aidTable.charAt(i)+"");
            // table.forcePut( aidTable.charAt(i), index );
            // index ++;
        }

        return inversTable;
    }

    /**
     * 將檔案名稱轉換為˙數字型態的文章序號
     *
     * @param fn 檔案名稱
     * @return 數字型態的文章序號, 若檔案名稱格式不符將回傳 0<br>
     *     轉換後的文章編號將符合 [M|G].[unsigned_integer].A.[HEX{3}]<br>
     *     範例: M.1451100858.A.71E
     */
    public static long fn2aidu(String fn) {
        long aidu = 0;
        long type = 0;
        long v1 = 0;
        long v2 = 0;

        // Utils.LOG("AidConverter","fn2aidu fn="+fn);

        if (fn == null) return 0;

        List<String> fnList = new ArrayList<>();
        // fnList=SplitterString(".",fn);
        int lastEnd = 0;
        for (int k = 0; k < fn.length() + 1; k++) {
            if (k == fn.length()) {
                fnList.add(fn.substring(lastEnd, k));
                break;
            }

            if (fn.charAt(k) == '.') {
                // Utils.LOG("AidConverter","fn2aidufn.charAt(k)==(char)64");
                fnList.add(fn.substring(lastEnd, k));
                lastEnd = k + 1;
            }
        }

        /*String[] cmds=fn.split('.'+"");
        for(String cmd:cmds){
                        Utils.LOG("AidConverter","fn2aidu fnList.add(cmd);="+cmd);
                        fnList.add(cmd);
        }*/

        // Utils.LOG("AidConverter","fn2aidu fnList.size()="+fnList.size());
        if (fnList.size() != 4) return 0;

        String typeString = fnList.get(0);
        String v1String = fnList.get(1);
        String v2String = fnList.get(3);

        // Utils.LOG("AidConverter","fn2aidu fnList.get(2)="+fnList.get(2));
        if (!fnList.get(2).equals("A")) return 0;

        if (!isNumeric(v1String) || v1String.length() != 10) return 0;
        // Utils.LOG("AidConverter","fn2aidu     if( !isNumeric(v1String) || v1String.length() != 10
        // )
        // return 0;");

        switch (typeString) {
            case "M":
                type = 0;
                break;
            case "G":
                type = 1;
                break;
            default:
                return 0;
        }

        v1 = Long.parseLong(v1String);
        v2 = Long.parseLong(v2String, 16);

        aidu = ((type & 0xf) << 44) | ((v1 & 0xffffffffL) << 12) | (v2 & 0xfff);
        // Utils.LOG("AidConverter","fn2aidu aidu="+aidu);

        return aidu;
    }

    /**
     * 將數字型態的文章序號轉換為字串型態的文章編號
     *
     * @param aidu 數字型態之文章序號
     * @return 轉換後的文章編號將符合 [a-zA-Z-_]{8}<br>
     *     範例: 1MVWgwSU
     */
    private static String aidu2aidc(long aidu) {
        int size = table.size();
        // HashMap<String,Long> inverseTable=table.inverse;
        //   BiMap<Long, Character> inverseTable = table.inverse();

        StringBuffer stringBuffer = new StringBuffer();

        while (stringBuffer.length() < 8) {
            long v = aidu % size;
            //  Utils.LOG("AidConverter","aidu2aidc aidu="+aidu+" / size="+ size);
            if (!inverseTable.containsKey(v)) return null;

            stringBuffer.insert(0, inverseTable.get(v));
            aidu = aidu / size;
        }

        // Utils.LOG("AidConverter","aidu2aidc stringBuffer.toString()="+stringBuffer.toString());
        return stringBuffer.toString();
    }

    /**
     * 將文章編號轉換為數字型態的文章序號
     *
     * @param aid 文章編號
     * @return 數字型態的文章序號
     */
    private static long aidc2aidu(String aid) {
        // Utils.LOG("AidConverter","aidc2aidu aid="+aid);
        // char[] aidChars = aid.toCharArray();
        long aidu = 0;
        for (int i = 0; i < aid.length(); i++) {
            char aidChar = aid.charAt(i);
            // Utils.LOG("AidConverter","aidc2aidu aidChar="+aidChar);
            if (aidChar == '@') break;
            // Utils.LOG("AidConverter","aidc2aidu   if( aidChar == '@' ) break;");
            if (!table.containsKey(aidChar + "")) return 0;
            // Utils.LOG("AidConverter","aidc2aidu   if( !table.containsKey(aidChar) ) return 0;");
            long v = table.get(aidChar + "");
            // Utils.LOG("AidConverter","aidc2aidu long v ="+v);
            aidu = aidu << 6;
            aidu = aidu | (v & 0x3f);
        }

        return aidu;
    }

    /**
     * 將文章序號(數字型態)轉換為檔案名稱
     *
     * @param aidu 文章序號(數字型態)
     * @return 轉換後的檔案名稱, 格式將符合 [M|G].[unsigned_integer].A.[HEX{3}]<br>
     *     最後的16進位表示法若未滿3個字將以0從左邊開始補齊<br>
     *     範例: M.1451100858.A.71E
     */
    private static String aidu2fn(long aidu) {
        long type = ((aidu >> 44) & 0xf);
        long v1 = ((aidu >> 12) & 0xffffffffL);
        long v2 = (aidu & 0xfff);

        // casting to unsigned
        //        v1 = v1 & 0xffffffffL;
        String hex = Long.toHexString(v2).toUpperCase();

        return (((type == 0) ? "M" : "G") + "." + v1 + ".A." + StringleftPad(hex, 3, "0"));
    }

    /**
     * 將文章編號轉換為檔案名稱
     *
     * @param aid 文章編號
     * @return 轉換後的檔案名稱, 格式將符合 [M|G].[unsigned_integer].A.[HEX{3}]<br>
     *     最後的16進位表示法若未滿3個字將以0從左邊開始補齊<br>
     *     範例: M.1451100858.A.71E
     */
    public static String aidToFileName(String aid) {
        String out = "";

        try {
            return aidu2fn(aidc2aidu(aid));
        } catch (Exception e) {
            return "https://www.ptt.cc/bbs/index.html";
        }
    }

    /**
     * 將文章編號轉換為 WEB 版 URL
     *
     * @param boardTitle 文章所屬看板名稱
     * @param aid 文章編號
     * @return WEB 版的完整 URL
     */
    public static String aidToUrl(String boardTitle, String aid) {
        if (boardTitle.isEmpty() || aid.isEmpty()) return "";

        return DOMAIN_URL + boardTitle + "/" + aidToFileName(aid) + FILE_EXT;
    }

    /**
     * 將檔案名稱(也就是 URL 的最後一段 不包含副檔名)轉換為文章編號
     *
     * @param fileName 檔案名稱
     * @return 轉換後的文章編號, 若檔案名稱格式不符則將回傳 null
     */
    public static String fileNameToAid(String fileName) {
        // Utils.LOG("AidConverter","fileName="+fileName);
        long temp = fn2aidu(fileName);

        return aidu2aidc(temp);
    }

    /**
     * 將 URL 轉換為 AID 物件
     *
     * @param url PTT WEB 版的 URL
     * @return 物件內包含 文章編號 與 看板名
     * @see AidBean
     */
    public static AidBean urlToAid(String url) {
        try {
            url = url.replace("https:", "").replace("http:", "").replace(" ", "").replace("//", "");

            List<String> urlList = SplitterString("/", url);

            if (url.indexOf("www.ptt.cc") == -1)
                return AidBean.Builder.anAidBean()
                        .withBoardTitle("Gossiping")
                        .withAid("_0000000")
                        .build();

            int fileWhere = -1;
            if (urlList.size() > 1 && urlList.size() < 6) {
                for (int i = 0; i < urlList.size(); i++) {
                    if (urlList.get(i).indexOf(".htm") != -1
                            || urlList.get(i).indexOf(".html") != -1) {
                        fileWhere = i;
                    }
                }
                if (fileWhere > 1) {
                    String boardTitle = urlList.get(fileWhere - 1);
                    String fileName = urlList.get(fileWhere).replaceAll("\\.(html|htm)", "");
                    // Utils.LOG("AidConverter","urlToAid fileName="+fileName);
                    String aid = fileNameToAid(fileName);

                    return AidBean.Builder.anAidBean()
                            .withBoardTitle(boardTitle)
                            .withAid(aid)
                            .build();
                } else {
                    return AidBean.Builder.anAidBean()
                            .withBoardTitle("Gossiping")
                            .withAid("_0000000")
                            .build();
                }
            } else {
                return AidBean.Builder.anAidBean()
                        .withBoardTitle("Gossiping")
                        .withAid("_0000000")
                        .build();
            }
        } catch (Exception e) {
            // Utils.LOG("URLToAID",e.toString());
            return AidBean.Builder.anAidBean()
                    .withBoardTitle("Gossiping")
                    .withAid("_0000000")
                    .build();
        }
    }

    public static List<String> SplitterString(String cut, String input) {
        List<String> output = new ArrayList<>();

        String[] cmds = input.split(cut);
        for (String cmd : cmds) {
            output.add(cmd);
        }
        return output;
    }

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    public static String StringleftPad(String input, int num, String put) {
        // String output="";
        while (input.length() < num) {
            input = put + input;
        }

        return input;
    }
}
