package com.tj007.divbucketmvp.presenter;

import android.util.Log;

import com.tj007.divbucketmvp.contract.UserInfoContract;
import com.tj007.divbucketmvp.utils.OkhttpUtil;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class UserInfoPresenter implements UserInfoContract.Presenter {

    private UserInfoContract.View mView = null;
    public UserInfoPresenter(@NotNull UserInfoContract.View view){
        mView=view;
        mView.attachPresenter(this);
    }
    @Override
    public void changeUserInfo() {
        JSONObject jsonObject = mView.getUserInfo();
        try {
            Log.d("regrequest", "message: "+jsonObject.toString());
            OkhttpUtil.requestByJson("http://58.41.204.52:28080/user/updateUserInfo", jsonObject, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.d("regrequest", "onFailure: "+e.toString());
                    mView.changeUserInfoFailed();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    Log.d("regrequest", "success: ");
                    JSONObject resp = null;
                    try {
                        resp = new JSONObject(response.body().string());
                        Log.d("regrequest", "success: "+resp.toString());
                        if ("success".equals(resp.getString("result"))){
                            mView.changeUserInfoSuccess();
                        }else{
                            mView.changeUserInfoFailed();
                        }
                    } catch (JSONException e) {
                        mView.changeUserInfoFailed();
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
