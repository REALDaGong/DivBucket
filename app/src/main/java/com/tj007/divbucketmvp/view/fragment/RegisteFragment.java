package com.tj007.divbucketmvp.view.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.tj007.divbucketmvp.adapter.TextWatcherAdapter;

import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;

import com.tj007.divbucketmvp.R;
import com.tj007.divbucketmvp.contract.RegisterContract;
import com.tj007.divbucketmvp.presenter.RegisterPresenter;
import com.tj007.divbucketmvp.view.activity.GoActivity;
import com.tj007.divbucketmvp.view.activity.HomePageActivity;
import com.tj007.divbucketmvp.view.activity.LoginActivity;

import android.os.Handler;
import android.text.Editable;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

public class RegisteFragment extends DialogFragment implements View.OnClickListener, RegisterContract.View {

    private RegisterContract.Presenter mPresenter;
    private EditText mEtEmail;
    private Button mBtnNext;
    private ImageButton mBtnClose;
    private ProgressBar loading;
    private String email;

    private final static int HIDE_LOADING = 1;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case HIDE_LOADING:
                    loading.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registe, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mPresenter = new RegisterPresenter(this);
        mEtEmail = view.findViewById(R.id.etx_email);
        mBtnNext = view.findViewById(R.id.btn_next);
        mBtnClose = view.findViewById(R.id.btn_close);
        loading = view.findViewById(R.id.loading);
        initView();
        super.onViewCreated(view, savedInstanceState);
    }

    private void initView() {
        mEtEmail.addTextChangedListener(adapter);
        mEtEmail.setText(email);
        mBtnNext.setOnClickListener(this);
        mBtnClose.setOnClickListener(this);
        loading.setVisibility(View.GONE);
    }

    TextWatcherAdapter adapter = new TextWatcherAdapter() {
        @Override
        public void afterTextChanged(Editable s) {
            if (s.toString().matches("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*")) {
                mBtnNext.setEnabled(true);
            } else {
                mBtnNext.setEnabled(false);
            }
        }
    };


    public void showLoading() {
        loading.setVisibility(View.VISIBLE);
    }
    public void hideLoading() {
        loading.setVisibility(View.GONE);
    }


    public void CheckPhoneSuccess(String msg) {
        //已经注册
        loading.setVisibility(View.GONE);
    }


    public void CheckPhoneFail(String msg) {
        //没有注册
        loading.setVisibility(View.GONE);
        toLoginPage();
    }


    public void onNetworkError(String msg) {
        toLoginPage();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                showLoading();
                //请求网络
                mPresenter.verifyEmail();
                break;
            case R.id.btn_close:
                dismiss();
                break;
        }
    }

    private void toLoginPage() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                email = mEtEmail.getText().toString();
                RegistInfo fragment = RegistInfo.newInstance(email);
                fragment.show(getFragmentManager(),"Password");
                dismiss();
            }
        }, 2000);

    }

    @Override
    public void verifySuccessfully() {
        Looper.prepare();
        Message message = new Message();
        message.what = HIDE_LOADING;
        handler.sendMessage(message);
        email = mEtEmail.getText().toString();
        RegistInfo fragment = RegistInfo.newInstance(email);
        fragment.show(getFragmentManager(),"Password");
        dismiss();
        Looper.loop();
    }

    @Override
    public void verifyFailed() {
        Message message = new Message();
        message.what = HIDE_LOADING;
        handler.sendMessage(message);
        Looper.prepare();
        Toast.makeText(getContext(),"邮箱号已存在！",Toast.LENGTH_SHORT).show();
        Looper.loop();
    }

    @Override
    public String getEmail() {
        return mEtEmail.getText().toString();
    }


    @Override
    public void attachPresenter(RegisterContract.Presenter presenter) {
        mPresenter=presenter;
    }
}
