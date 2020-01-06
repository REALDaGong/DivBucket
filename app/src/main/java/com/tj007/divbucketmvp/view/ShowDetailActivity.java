package com.tj007.divbucketmvp.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.tj007.divbucketmvp.model.DatabaseManager;

import java.util.List;
import java.util.zip.Inflater;

import butterknife.BindView;
import butterknife.ButterKnife;

//长按跳转这里。
public class ShowDetailActivity extends AppCompatActivity {
    @BindView(R.id.url)
    TextView url;
    @BindView(R.id.newmsg)
    TextView newmsg;
    @BindView(R.id.oldmsg)
    TextView oldmsg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detail);
        ButterKnife.bind(this);
        Intent i=getIntent();
        String surl=i.getStringExtra("url");
        String msg=i.getStringExtra("msg");
        url.setText(surl);
        newmsg.setText(msg);
        List<String> s=DatabaseManager.getInstance().getWatchingResult("defaultpath",i.getStringExtra("url"));
        if(s!=null) {
            oldmsg.setText(s.get(1));
        }else{
            oldmsg.setText("没有旧数据");
        }
    }
}
