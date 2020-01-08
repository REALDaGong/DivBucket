package com.tj007.divbucketmvp.presenter;

import android.util.Log;

import com.tj007.divbucketmvp.contract.StatisticContract;
import com.tj007.divbucketmvp.utils.OkhttpUtil;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class StatisticPresenter implements StatisticContract.Presenter {
    private StatisticContract.View mView;

    public StatisticPresenter(@NotNull StatisticContract.View mView){
        this.mView=mView;
        mView.attachPresenter(this);
    }


    @Override
    public void start() {

    }

    @Override
    public void detachView() {

    }

    @Override
    public void requestRecomment() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id",mView.getEmail());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            Log.d("recomment", "message: "+jsonObject.toString());
            OkhttpUtil.requestByJson("http://58.41.204.52:25000/user/recommend", jsonObject, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.d("recomment", "onFailure: "+e.toString());
                    mView.recommentFail();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String rec = response.body().string();
                    mView.recommentSuccess(rec);
                    Log.d("recomment", "success: ");
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
