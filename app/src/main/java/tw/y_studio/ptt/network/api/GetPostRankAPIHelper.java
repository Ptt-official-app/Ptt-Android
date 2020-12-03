package tw.y_studio.ptt.network.api;

import android.content.Context;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GetPostRankAPIHelper extends BaseAPIHelper {

    public GetPostRankAPIHelper(Context context, String board, String aid) {
        super(context);
        this.board = board;
        this.aid = aid;
        this.data = new HashMap<>();
    }

    private Map<String, Object> data;
    private String board = "";
    private String aid = "";
    private int Down = 0;
    private int Goup = 0;

    public Map<String, Object> getData() {
        return data;
    }

    public GetPostRankAPIHelper get() throws Exception {
        data.clear();
        Request request =
                new Request.Builder()
                        /// api/Rank/{board}/{aid}
                        .url(hostUrl + "/api/Rank/" + board + "/" + aid)
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
            Down = all.getInt("down");
            Goup = all.getInt("goup");
            data.put("Goup", all.getInt("goup"));
            data.put("Down", all.getInt("down"));
            data.put("Board", all.getString("board"));
            data.put("AID", all.getString("aid"));
        }

        return this;
    }

    public int getDown() {
        return Down;
    }

    public int getGoup() {
        return Goup;
    }

    public int getLike() {
        return Goup - Down;
    }
}
