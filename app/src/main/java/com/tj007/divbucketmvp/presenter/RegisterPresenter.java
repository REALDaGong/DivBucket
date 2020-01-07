package com.tj007.divbucketmvp.presenter;

import android.util.Log;

import androidx.annotation.NonNull;

import com.tj007.divbucketmvp.contract.LoginContract;
import com.tj007.divbucketmvp.contract.RegisterContract;
import com.tj007.divbucketmvp.utils.OkhttpUtil;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RegisterPresenter implements RegisterContract.Presenter {

    private RegisterContract.View mView = null;
    public RegisterPresenter(@NotNull RegisterContract.View view){
        mView=view;
        mView.attachPresenter(this);
    }
    @Override
    public void verifyEmail() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id",mView.getEmail());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            Log.d("regrequest", "message: "+jsonObject.toString());
            OkhttpUtil.requestByJson("http://58.41.204.52:28080/user/getUserInfo", jsonObject, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.d("regrequest", "onFailure: "+e.toString());
                    mView.verifyFailed();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    Log.d("regrequest", "success: ");
                    JSONObject resp = null;
                    try {
                        resp = new JSONObject(response.body().string());
                        if ("fail".equals(resp.getString("result"))){
                            mView.verifySuccessfully();
                        }else{
                            mView.verifyFailed();
                        }
                    } catch (JSONException e) {
                        mView.verifyFailed();
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
