package tw.y_studio.ptt.api;

import android.content.Context;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import org.json.JSONArray;
import org.json.JSONObject;

import tw.y_studio.ptt.utils.DebugUtils;
import tw.y_studio.ptt.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PostAPIHelper extends BaseAPIHelper {

    public PostAPIHelper(Context context, String board, String filename) {
        super(context);
        this.board = board;
        this.fileName = filename;
        this.pushData = new ArrayList<>();
    }

    private List<Map<String, Object>> pushData;
    private String board = "";
    private String fileName = "";
    private String title = "";
    private String classString = "";
    private String date = "";
    private String auth = "";
    private String auth_nickName = "";
    private String content = "";
    private Pattern Title_class = Pattern.compile("\\[([\\s\\S]{1,4})\\]");
    private Pattern contentPattern = Pattern.compile(": ([\\s\\S]*)");
    private final Pattern ip_time =
            Pattern.compile("(\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3}) (\\d+/\\d+ \\d+:\\d+)");

    public List<Map<String, Object>> getPushData() {
        return pushData;
    }

    public String getTitle() {
        return title;
    }

    public String getBoard() {
        return board;
    }

    public String getClassString() {
        return classString;
    }

    public String getDate() {
        return date;
    }

    public String getAuth() {
        return auth;
    }

    public String getAuth_nickName() {
        return auth_nickName;
    }

    public String getContent() {
        return content;
    }

    private int pushCount = 0;

    public int getPushCount() {
        return pushCount;
    }

    private int floorNum = 0;

    public int getFloorNum() {
        return floorNum;
    }

    public PostAPIHelper get() throws Exception {
        pushData.clear();
        pushCount = 0;
        floorNum = 0;
        DebugUtils.Log("onGetPost", hostUrl + "/api/Article/" + board + "/" + fileName);
        Request request =
                new Request.Builder()
                        .url(hostUrl + "/api/Article/" + board + "/" + fileName)
                        .build();
        Call mcall = getOkHttpClient().newCall(request);

        Response response = mcall.execute();
        final int code = response.code(); // can be any value
        if (!response.isSuccessful() && code != 200) {
            // error
            throw new Exception("Error Code : " + code);
        } else {
            ResponseBody mRb = response.body();
            String cont = mRb.string();
            DebugUtils.Log("onGetPost", "all = " + cont);
            JSONObject all = new JSONObject(cont);

            String title = all.getString("title");
            String classs = "";
            Matcher m23 = Title_class.matcher(title);
            if (m23.find()) {
                classs = m23.group(1);
                if (classs.length() <= 6) {
                    int start = title.indexOf("[" + classs + "]");
                    int end = start + classs.length() + 2;
                    if (start == 0) {
                        title = StringUtils.clearStart(title.substring(end));
                    } else {
                        try {
                            title =
                                    title.substring(0, start)
                                            + StringUtils.clearStart(title.substring(end));
                        } catch (Exception E) {
                        }
                    }
                } else {
                    classs = "無分類";
                }
            }
            this.title = title;
            this.classString = classs;
            this.auth = all.getString("author");
            this.auth_nickName = all.getString("nickname");
            this.date = all.getString("date");
            this.content = all.getString("content");

            JSONArray PostList = all.getJSONArray("comments");
            int i = 0;
            while (!PostList.isNull(i)) {
                JSONObject m3 = PostList.getJSONObject(i);
                i++;

                Map<String, Object> item = new HashMap<>();
                item.put("type", "commit");

                item.put("auth", m3.getString("userid"));
                String type = m3.getString("tag");
                item.put("commitType", type);
                if (type.equals("推")) {
                    pushCount++;
                }
                if (type.equals("噓")) {
                    pushCount--;
                }
                item.put("index", floorNum++);
                item.put("floor", floorNum + "F");
                item.put("like", "0");

                // data.add(item);

                String ip = "";
                String time_ = m3.getString("iPdatetime");
                Matcher m1 = ip_time.matcher(m3.getString("iPdatetime"));
                if (m1.find()) {
                    ip = m1.group(1);
                    time_ = m1.group(2);
                }
                item.put("ip", ip);
                item.put("time", time_);
                Matcher m = contentPattern.matcher(m3.getString("content"));
                if (m.find()) {
                    item.put("text", m.group(1));
                } else {
                    item.put("text", m3.getString("content"));
                }

                pushData.add(item);
                if (true) {
                    List<String> imageUrl =
                            StringUtils.getImgUrl(StringUtils.notNullString(item.get("text")));
                    for (String urlString : imageUrl) {
                        if (true) {
                            Map<String, Object> item2 = new HashMap<>();
                            item2.put("type", "content_image");
                            item2.put("url", urlString);
                            item2.put("index", floorNum);
                            pushData.add(item2);
                        }
                    }
                }
                Map<String, Object> item2 = new HashMap<>();
                item2.putAll(item);
                item2.put("type", "commit_bar");
                pushData.add(item2);
            }
        }

        return this;
    }
}
