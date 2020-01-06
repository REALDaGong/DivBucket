package com.tj007.divbucketmvp.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.R;
import com.tj007.divbucketmvp.presenter.ChooseWatchingTargetPresenter;

import java.util.List;

public class ChooseWatchingTargetActivity extends AppCompatActivity {

    ChooseWatchingTargetView mView=new ChooseWatchingTargetView();
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_watching_target);

        ChooseWatchingTargetPresenter mPresenter=new ChooseWatchingTargetPresenter(mView);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.host,mView).commit();
        getSupportFragmentManager().executePendingTransactions();
        Intent intent=getIntent();
        String uri=intent.getStringExtra("url");
        mView.setData(uri);

    }

    @Override
    public void onBackPressed() {
        mView.saveSelectedNodes();
        List<String> allPath=mView.getAllPath();
        String[] s=new String[allPath.size()];
        s=allPath.toArray(s);
        Intent intent=getIntent();
        intent.putExtra("path",s);
        setResult(0,intent);
        super.onBackPressed();
    }
}
