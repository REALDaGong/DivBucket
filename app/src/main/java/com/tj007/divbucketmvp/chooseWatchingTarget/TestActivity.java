package com.tj007.divbucketmvp.chooseWatchingTarget;

import android.os.Bundle;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.R;

//注：只是一个测试是否成功做到了mvp的acitivity
public class TestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);

        Toolbar mtoolBar=findViewById(R.id.toolbar);

        setSupportActionBar(mtoolBar);

        XMLViewFragment xmlViewFragment=(XMLViewFragment)getSupportFragmentManager().findFragmentById(R.id.fragment);

        new TreeViewDOMBuilder(xmlViewFragment);

    }


}
