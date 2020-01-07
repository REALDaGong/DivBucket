package com.tj007.divbucketmvp.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.tj007.divbucketmvp.adapter.TextWatcherAdapter;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tj007.divbucketmvp.R;
import com.tj007.divbucketmvp.contract.RegistInfoContract;
import com.tj007.divbucketmvp.presenter.RegistInfoPresenter;

import android.text.Editable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class RegistInfo extends DialogFragment implements
        View.OnClickListener,CompoundButton.OnCheckedChangeListener, RegistInfoContract.View {

    private RegistInfoContract.Presenter mPresenter;
    private EditText etPwdCon;
    private EditText etPwd;
    private Button tvRegister;
    private ImageButton btnBack;
    private ImageButton btnClose;
    private CheckBox ivDisplay;
    private ProgressBar loading;
    private TextView emailRegister;

    private String email;
    private boolean pwdStatus;
    private boolean pwdConStatus;

    private final static int HIDE_LOADING = 1;

    public static RegistInfo newInstance(String email) {
        RegistInfo mFragment = new RegistInfo();
        Bundle bundle = new Bundle();
        bundle.putString("email", email);
        mFragment.setArguments(bundle);
        return mFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_regist_info, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mPresenter = new RegistInfoPresenter(this);
        email = getArguments().getString("email");
        etPwd = view.findViewById(R.id.et_pwd);
        etPwdCon = view.findViewById(R.id.et_pwd_con);
        btnBack = view.findViewById(R.id.ib_back);
        btnClose = view.findViewById(R.id.ib_close);
        ivDisplay = view.findViewById(R.id.iv_display);
        tvRegister = view.findViewById(R.id.tv_register);
        loading = view.findViewById(R.id.loading);
        emailRegister = view.findViewById(R.id.email_register);

        initView();
        super.onViewCreated(view, savedInstanceState);
    }

    private void initView(){
        loading.setVisibility(View.GONE);
        etPwdCon.addTextChangedListener(adapterCon);
        etPwd.addTextChangedListener(adapter);
        btnBack.setOnClickListener(this);
        btnClose.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
        ivDisplay.setOnCheckedChangeListener(this);
        emailRegister.setText(email);
    }

    TextWatcherAdapter adapter = new TextWatcherAdapter() {
        @Override
        public void afterTextChanged(Editable s) {
            int len = s.length();
            if (len >= 6 && len <= 16 ){
                pwdStatus = true;
            } else{
                pwdStatus = false;
            }
        }
    };
    TextWatcherAdapter adapterCon = new TextWatcherAdapter() {
        @Override
        public void afterTextChanged(Editable s) {
            if (pwdStatus && s.toString().equals(etPwd.getText().toString())){
                tvRegister.setEnabled(true);
            } else if (!pwdStatus){
                Toast.makeText(getContext(),"Password Should between 6~16 word",Toast.LENGTH_SHORT).show();
                tvRegister.setEnabled(false);
            } else {
                Log.d("RegisterInfo:","a:"+etPwd.getText()+"b:"+s);
                tvRegister.setEnabled(false);
            }
        }
    };


    public void showLoading() {
        loading.setVisibility(View.VISIBLE);
    }
    public void hideLoading() {
        loading.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_register:
                //网络请求
                showLoading();
                mPresenter.register();
                hideLoading();
                break;
            case R.id.btn_close:
                dismiss();
                break;
            case R.id.ib_back:
                RegisteFragment registeFragment = new RegisteFragment();
                registeFragment.show(getFragmentManager(),"Email");
                dismiss();
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b){
            etPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            etPwdCon.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }else {
            etPwdCon.setTransformationMethod(PasswordTransformationMethod.getInstance());
            etPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }

    @Override
    public void registSuccessfully() {
        Looper.prepare();
        Toast.makeText(getContext(),"注册成功，请登录！",Toast.LENGTH_SHORT).show();
        dismiss();
        Looper.loop();
    }

    @Override
    public void registFailed() {
        Looper.prepare();
        Toast.makeText(getContext(),"网络错误，请重试！",Toast.LENGTH_SHORT).show();
        Looper.loop();
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getPwd() {
        return etPwd.getText().toString();
    }

    @Override
    public void attachPresenter(RegistInfoContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
