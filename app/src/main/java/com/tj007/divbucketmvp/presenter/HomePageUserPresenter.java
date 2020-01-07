package com.tj007.divbucketmvp.presenter;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.tj007.divbucketmvp.contract.HomePageUserContract;
import com.tj007.divbucketmvp.utils.OkhttpUtil;
import com.tj007.divbucketmvp.view.activity.GoActivity;
import com.tj007.divbucketmvp.view.activity.HomePageActivity;
import com.tj007.divbucketmvp.view.activity.LoginActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class HomePageUserPresenter implements HomePageUserContract.Presenter {
    private HomePageUserContract.View mView=null;
    public HomePageUserPresenter(@NotNull HomePageUserContract.View view){
        mView=view;
        mView.attachPresenter(this);
    }
    @Override
    public void getUserInfo() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id",mView.getEmail());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            Log.d("loginrequest", "message: "+jsonObject.toString());
            OkhttpUtil.requestByJson("http://58.41.204.52:28080/user/getUserInfo", jsonObject, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.d("loginrequest", "onFailure: "+e.toString());
                    mView.getUserInfoFailed();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    Log.d("loginrequest", "success: ");
                    JSONObject resp = null;
                    try {
                        resp = new JSONObject(response.body().string());
                        if ("success".equals(resp.getString("result"))){
                            mView.getUserInfoSuccess(resp);
                        }else{
                            mView.getUserInfoFailed();
                        }
                    } catch (JSONException e) {
                        mView.getUserInfoFailed();
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
