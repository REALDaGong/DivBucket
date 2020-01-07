package com.tj007.divbucketmvp.mailboxsystem.activity;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.tj007.divbucketmvp.R;

@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark1));
        Looper.myQueue().addIdleHandler(() -> {
            initView();
            initData();
            return false;
        });
    }

    @Override
    public Resources getResources() {
        Resources resources = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        resources.updateConfiguration(config, resources.getDisplayMetrics() );
        return resources;
    }

    protected void initView() {

    }

    protected void initData() {

    }

}
