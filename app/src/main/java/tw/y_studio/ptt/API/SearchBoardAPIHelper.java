package tw.y_studio.ptt.API;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class SearchBoardAPIHelper extends BaseAPIHelper {
    public SearchBoardAPIHelper(Context context){
        super(context);

        this.data = new ArrayList<>();
    }
    private List<Map<String,Object>> data;

    public List<Map<String,Object>> getData(){
        return data;
    }
    public SearchBoardAPIHelper get(String keyword) throws Exception{
        data.clear();
        String text = URLEncoder.encode(keyword, "UTF-8").toString();
        Request request = new Request.Builder()
                .url(hostUrl+"/api/Board/Search?keyword="+text)
                .build();
        Call mcall=mOkHttpClient.newCall(request);

        Response response = mcall.execute();
        final int code =response.code(); // can be any value
        if (!response.isSuccessful()&&code!=200) {
            //error
            throw new Exception("Error Code : "+code);
        }else {
            ResponseBody mRb = response.body();
            String cont = mRb.string();

            JSONArray List = new JSONArray(cont);
            int i=0;
            while (!List.isNull(i)){
                JSONObject m3 = List.getJSONObject(i);
                i++;

                Map<String, Object> item = new HashMap<>();
                item.put("number", m3.getInt("sn"));
                item.put("title", m3.getString("name"));
                item.put("subtitle",m3.getString("title"));
                item.put("boardType", m3.getInt("boardType"));
                item.put("like",false);
                item.put("moderators", "");
                item.put("class","");
                item.put("online", m3.getInt("onlineCount"));

                item.put("onlineColor", 7);

                data.add(item);
            }

        }

        return this;
    }


}
