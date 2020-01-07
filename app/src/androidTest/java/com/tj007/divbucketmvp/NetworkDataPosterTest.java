package com.tj007.divbucketmvp;

import android.util.Log;

import com.tj007.divbucketmvp.model.NetworkDataPoster;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class NetworkDataPosterTest {

    @Test
    public void postAddPath() throws IOException, JSONException {
        Log.d("TESTDEBUG","8899174");
        List<String> tag=new ArrayList<>();
        List<String> path=new ArrayList<>();
        tag.add("123");
        tag.add("456");
        path.add("pathpath");

        CountDownLatch countDownLatch=new CountDownLatch(1);

        Callback callback=new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("TESTDEBUG",e.getMessage());
                countDownLatch.countDown();
                assert false;
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Log.d("TESTDEBUG",response.body().string());
                countDownLatch.countDown();
            }
        };
        NetworkDataPoster.postAddPath("https://234.com","remark",tag,path,true,callback);
        try {
            Log.d("TESTDEBUG","8899174");
            countDownLatch.await();
        }catch (Exception e){

        }
    }

    @Test
    public void getWebInfo() throws JSONException,IOException {
        CountDownLatch countDownLatch=new CountDownLatch(1);

        Callback callback=new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("TESTDEBUG",e.getMessage());
                countDownLatch.countDown();
                assert false;
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Log.d("TESTDEBUG",response.body().string());
                countDownLatch.countDown();
            }
        };
        NetworkDataPoster.getWebInfo("https://baidu.com",callback);
        try {
            Log.d("TESTDEBUG","8899174");
            countDownLatch.await();
        }catch (Exception e){

        }
    }

    @Test
    public void getUserWebRule() {
    }

    @Test
    public void getNewRecord() {
    }
}