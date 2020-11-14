package tw.y_studio.ptt.api;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import org.json.JSONArray;
import org.json.JSONObject;

import tw.y_studio.ptt.Utils.StringUtils;
import tw.y_studio.ptt.model.PartialPost;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PostListAPIHelper extends BaseAPIHelper {

    public PostListAPIHelper(String board) {
        super();
        this.board = board;
        this.data = new ArrayList<>();
    }

    private List<PartialPost> data;
    private String board = "";
    private String boardTitle = "";
    private Pattern Title_class = Pattern.compile("\\[([\\s\\S]{1,4})\\]");

    public List<PartialPost> getData() {
        return data;
    }

    public PostListAPIHelper get(int page) throws Exception {
        data.clear();
        Request request =
                new Request.Builder()
                        .url(hostUrl + "/api/Article/" + board + "?page=" + page)
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
            JSONObject all = new JSONObject(cont);
            JSONObject BoardInfo = all.getJSONObject("boardInfo");
            boardTitle = BoardInfo.getString("title");
            JSONArray PostList = all.getJSONArray("postList");
            int i = 0;
            while (!PostList.isNull(i)) {
                JSONObject m3 = PostList.getJSONObject(i);
                i++;

                String title = m3.getString("title");
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

                PartialPost post =
                        new PartialPost(
                                title,
                                m3.getString("date"),
                                classs,
                                0,
                                m3.getInt("goup"),
                                m3.getString("author"),
                                false,
                                false,
                                "https://www.ptt.cc" + m3.getString("href"));

                data.add(post);
            }
        }

        return this;
    }

    public String getBoardTitle() {
        return boardTitle;
    }
}
