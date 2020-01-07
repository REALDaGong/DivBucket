package com.tj007.divbucketmvp.presenter;

import android.util.Log;

import com.tj007.divbucketmvp.contract.UploadAvatarContract;
import com.tj007.divbucketmvp.utils.OkhttpUtil;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class UploadAvatarPresenter implements UploadAvatarContract.Presenter {
    private UploadAvatarContract.View mView = null;

    public UploadAvatarPresenter(@NotNull UploadAvatarContract.View view){
        mView=view;
        mView.attachPresenter(this);
    }

    private  void uploadUrl(final String avatarUrl){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id",mView.getEmail());
            jsonObject.put("avatar",avatarUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            Log.d("loginrequest", "message: "+jsonObject.toString());
            OkhttpUtil.requestByJson("http://58.41.204.52:28080/user/updateAvatar", jsonObject, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.d("loginrequest", "onFailure: "+e.toString());
                    mView.uploadAvatarFailed();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    Log.d("loginrequest", "success: ");
                    JSONObject resp = null;
                    try {
                        resp = new JSONObject(response.body().string());
                        if ("success".equals(resp.getString("result"))){
                            mView.uploadAvatarSuccess(avatarUrl);
                        }else{
                            mView.uploadAvatarFailed();
                        }
                    } catch (JSONException e) {
                        mView.uploadAvatarFailed();
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void uploadAvatar() {
        try {
            OkhttpUtil.uploadImage("https://sm.ms/api/v2/upload",mView.getImagePath(),new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("uploadImage", "onFailure: ");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        JSONObject result = new JSONObject(response.body().string());
                        String avatarURL;
                        if(result.getBoolean("success")==true){
                            avatarURL = result.getJSONObject("data").getString("url");
                            Log.d("request", "onResponse: " + result.getJSONObject("data").get("url"));
                            uploadUrl(avatarURL);
                        }else if(result.getBoolean("success") == false && "image_repeated".equals(result.getString("code"))) {
                            avatarURL = result.getString("images");
                            Log.d("request", "onResponse: " + result.get("images"));
                            uploadUrl(avatarURL);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start() {

    }

    @Override
    public void detachView() {

    }
}
