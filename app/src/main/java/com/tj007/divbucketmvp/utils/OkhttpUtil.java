package com.tj007.divbucketmvp.utils;

import android.net.Uri;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkhttpUtil {
    private static JSONObject result;
    private static boolean loginStatus;
    private static String avatarURL;

    public static void requestByJson(String url, JSONObject jsonObject,Callback callback) throws IOException, JSONException {
        result = null;
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody jsonBody = RequestBody.create(String.valueOf(jsonObject),MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(url)
                .addHeader(
                        "User-Agent",
                        "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)")
                .addHeader("Content-Type","application/json")
                .post(jsonBody)
                .build();
        Call call = okHttpClient.newCall(request);
        Log.d("requestJson", "here");
        call.enqueue(callback);
    }

    public static void uploadImage(String url, String imagePath,Callback callback) throws IOException, JSONException {
        result = null;
        OkHttpClient okHttpClient = new OkHttpClient();
        Log.d("imagePath", imagePath);
        File file = new File(imagePath);
        RequestBody image = RequestBody.create(file,MediaType.parse("image/png"));
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("smfile", imagePath,image)
                .build();
        final Request request = new Request.Builder()
                .url(url)
                .addHeader(
                        "User-Agent",
                        "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)")
                .addHeader("Authorization","YwuGXPC7h7E01RdwJRQW5q5LXFuDjjOX")
                .addHeader("Content-Type","multipart/form-data")
                .post(requestBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);
    }

    public static String uploadImage(String url, String imagePath) throws IOException, JSONException {
        result = null;
        OkHttpClient okHttpClient = new OkHttpClient();
        Log.d("imagePath", imagePath);
        File file = new File(imagePath);
        RequestBody image = RequestBody.create(file,MediaType.parse("image/png"));
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("smfile", imagePath,image)
                .build();
        final Request request = new Request.Builder()
                .url(url)
                .addHeader(
                        "User-Agent",
                        "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)")
                .addHeader("Authorization","YwuGXPC7h7E01RdwJRQW5q5LXFuDjjOX")
                .addHeader("Content-Type","multipart/form-data")
                .post(requestBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("request", "onFailure: ");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    result = new JSONObject(response.body().string());
                    if(result.getBoolean("success")==true){
                        avatarURL = result.getJSONObject("data").getString("url");
                        Log.d("request", "onResponse: " + result.getJSONObject("data").get("url"));
                    }else if(result.getBoolean("success") == false && result.getString("success")=="image_repeated") {
                        avatarURL = result.getString("images");
                        Log.d("request", "onResponse: " + result.get("images"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return avatarURL;
    }
}
