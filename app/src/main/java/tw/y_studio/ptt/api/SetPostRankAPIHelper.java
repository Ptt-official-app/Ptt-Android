package tw.y_studio.ptt.api;

import android.content.Context;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import org.json.JSONObject;

import tw.y_studio.ptt.utils.DebugUtils;

import java.util.HashMap;
import java.util.Map;

public class SetPostRankAPIHelper extends BaseAPIHelper {

    public SetPostRankAPIHelper(Context context, String board, String aid) {
        super(context);
        this.board = board;
        this.aid = aid;
        this.data = new HashMap<>();
    }

    private Map<String, Object> data;
    private String board = "";
    private String aid = "";

    public Map<String, Object> getData() {
        return data;
    }

    public enum iRank {
        like,
        dislike,
        non,
    }

    private int iRank2Int(iRank rank) {
        switch (rank) {
            case like:
                return 1;
            case dislike:
                return -1;
            case non:
            default:
                return 0;
        }
    }

    public SetPostRankAPIHelper get(String pttid, iRank rank) throws Exception {
        data.clear();

        RequestBody body = new FormBody.Builder().build();
        Request request =
                new Request.Builder()
                        .post(body)
                        .url(
                                hostUrl
                                        + "/api/Rank/"
                                        + board
                                        + "/"
                                        + aid
                                        + "?pttid="
                                        + pttid
                                        + "&rank="
                                        + iRank2Int(rank))
                        .build();
        DebugUtils.Log("SetPostRankAPIHelper", "" + request.toString());
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
            // Log.d("API","GetRankByPost = "+all.toString());
            data.put("Rank", all.getInt("rank"));
            data.put("PTTID", all.getString("pttid"));
            data.put("no", all.getString("no"));
            data.put("Board", all.getString("board"));
            data.put("AID", all.getString("aid"));
        }

        return this;
    }
}
