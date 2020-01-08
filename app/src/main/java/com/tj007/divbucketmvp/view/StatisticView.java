package com.tj007.divbucketmvp.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.tj007.divbucketmvp.R;
import com.tj007.divbucketmvp.contract.StatisticContract;
import com.tj007.divbucketmvp.presenter.StatisticPresenter;
import com.tj007.divbucketmvp.view.activity.HomePageActivity;

public class StatisticView extends Fragment implements StatisticContract.View {

    StatisticContract.Presenter mPresenter=null;
    SharedPreferences sharedPreferences;

    private TextView textView;

    private String url = "家徒四壁";

    private static final int LOAD_URL = 1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case LOAD_URL:
                    textView.setText(url);
            }
        }
    };

    @Override
    public void attachPresenter(StatisticContract.Presenter presenter) {
        mPresenter=presenter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistic,container,false);
        mPresenter = new StatisticPresenter(this);
        sharedPreferences= getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        initView(view);
        mPresenter.requestRecomment();
        return view;
    }

    private void initView(View view){
        textView = view.findViewById(R.id.textView2);
    }

    @Override
    public String getEmail() {
        return sharedPreferences.getString("email","");
    }

    @Override
    public void recommentSuccess(String recommentUrl) {
        url = recommentUrl;
        Message message = new Message();
        message.what = LOAD_URL;
        handler.sendMessage(message);
    }

    @Override
    public void recommentFail() {

    }
}
