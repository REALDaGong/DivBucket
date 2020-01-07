package com.tj007.divbucketmvp.presenter;

import android.util.Log;

import com.tj007.divbucketmvp.contract.LoginContract;
import com.tj007.divbucketmvp.utils.OkhttpUtil;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View mView=null;
    public LoginPresenter(@NotNull LoginContract.View view){
        mView=view;
        mView.attachPresenter(this);
    }
    @Override
    public void login() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id",mView.getEmail());
            jsonObject.put("psw",mView.getUserPassWord());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            Log.d("loginrequest", "message: "+jsonObject.toString());
            OkhttpUtil.requestByJson("http://58.41.204.52:28080/user/login", jsonObject, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.d("loginrequest", "onFailure: "+e.toString());
                    mView.loginFailed();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    Log.d("loginrequest", "success: ");
                    JSONObject resp = null;
                    try {
                        resp = new JSONObject(response.body().string());
                        if ("success".equals(resp.getString("result"))){
                            mView.loginSuccessfully();
                        }else{
                            mView.loginFailed();
                        }
                    } catch (JSONException e) {
                        mView.loginFailed();
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
