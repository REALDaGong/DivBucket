package com.tj007.divbucketmvp.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.tj007.divbucketmvp.R;
import android.view.WindowManager;

public class GoActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                if(sharedPreferences.getString("email","")==""){
                    Intent intent = new Intent(GoActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(GoActivity.this, HomePageActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go);
        sharedPreferences= getSharedPreferences("data", Context.MODE_PRIVATE);
        init();
    }

    private void init(){
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        new CountDown().start();
    }

    class CountDown extends Thread {
        int count = 3;
        @Override
        public void run() {
            try {
                while (count >= 0) {
                    sleep(1000);
                    Message message = new Message();
                    message.what = count;
                    handler.sendMessage(message);
                    count--;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
