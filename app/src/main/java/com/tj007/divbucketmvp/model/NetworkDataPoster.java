package com.tj007.divbucketmvp.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class NetworkDataPoster {
    private static String serverURL="http://58.41.204.52:28080";
    private static JSONObject result;

    private static String getUserId(){
        return "1";
    }

    public static void postAddPath(String URL,String remark, List<String> tag,List<String> path,boolean isActive, Callback callback) throws IOException, JSONException {
        result = null;

        String goal="/web/add";
        JSONObject jsonObject=new JSONObject();
        //构建数据
        JSONArray tagjsonArray=new JSONArray();
        for(int i=0;i<tag.size();i++){
            tagjsonArray.put(tag.get(i));
        }
        JSONArray pathjsonArray=new JSONArray();
        for(int i=0;i<tag.size();i++){
            pathjsonArray.put(tag.get(i));
        }

        jsonObject.put("tag",tagjsonArray);
        jsonObject.put("path",pathjsonArray);
        jsonObject.put("user_id",getUserId());
        jsonObject.put("url",URL);
        jsonObject.put("remark",remark);
        jsonObject.put("isActive",isActive);

        String s=jsonObject.toString();


        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = getRequest(jsonObject,goal);
        Call call = okHttpClient.newCall(request);
        Log.d("requestJson", "here");
        call.enqueue(callback);
    }

    public static void getWebInfo(String url, Callback callback) throws IOException, JSONException {
        result = null;

        String goal="/web/getWebInfo";

        //构建数据
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("user_id",getUserId());

        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = getRequest(jsonObject,goal);
        Call call = okHttpClient.newCall(request);
        Log.d("requestJson", "here");
        call.enqueue(callback);
    }

    public static void getUserWebRule(String url, Callback callback) throws IOException, JSONException {
        result = null;

        String goal="/web/getUserWebRule";

        //构建数据
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("user_id",getUserId());

        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = getRequest(jsonObject,goal);
        Call call = okHttpClient.newCall(request);
        Log.d("requestJson", "here");
        call.enqueue(callback);
    }

    public static void getNewRecord(String url, String id, String msg, Date date, Callback callback) throws IOException, JSONException {
        result = null;

        String goal="/web/getNewRecord";

        //构建数据
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("user_id",getUserId());
        jsonObject.put("url",url);
        jsonObject.put("id",id);
        jsonObject.put("value",msg);
        jsonObject.put("date",date.toString());

        OkHttpClient okHttpClient = new OkHttpClient();

        Request request = getRequest(jsonObject,goal);
        Call call = okHttpClient.newCall(request);
        Log.d("requestJson", "here");
        call.enqueue(callback);
    }
    private static Request getRequest(JSONObject json,String goal){
        RequestBody jsonBody = RequestBody.create(String.valueOf(json), MediaType.parse("application/json; charset=utf-8"));
        return new Request.Builder()
                .url(serverURL+goal)
                .addHeader(
                        "User-Agent",
                        "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)")
                .addHeader("Content-Type","application/json")
                .post(jsonBody)
                .build();
    }
}
