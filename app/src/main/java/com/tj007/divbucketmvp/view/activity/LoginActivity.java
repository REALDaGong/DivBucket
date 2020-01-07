package com.tj007.divbucketmvp.view.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.tj007.divbucketmvp.R;
import com.tj007.divbucketmvp.contract.LoginContract;
import com.tj007.divbucketmvp.presenter.LoginPresenter;
import com.tj007.divbucketmvp.utils.OnSwipeTouchListener;
import com.tj007.divbucketmvp.view.fragment.RegisteFragment;

import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {
    private LoginContract.Presenter mPresenter;
    private static String TAG = "LoginActivity";
    private ImageView imageView;
    private TextView textView;
    private Button signIn;
    private Button signUp;
    private EditText email;
    private EditText password;
    private TextInputLayout emailLayout;
    private TextInputLayout pwdLayout;
    private int emailLock;//for sign in, lock must be 1 then you can sign in.
    private int pwdLock;//for sign in, lock must be 1 then you can sign in.
    private int count = 0;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        emailLock = 0;
        pwdLock = 0;

        mPresenter = new LoginPresenter(this);
        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);
        signIn = findViewById(R.id.btn_signin);
        signUp = findViewById(R.id.btn_signup);
        password = findViewById(R.id.etx_pwd);
        email = findViewById(R.id.etx_email);
        emailLayout = findViewById(R.id.txtField_email);
        pwdLayout = findViewById(R.id.txtField_pwd);

        //share SharedPreferences
        sharedPreferences= getSharedPreferences("data", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        imageView.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSwipeRight() {
                if (count == 0) {
                    imageView.setImageResource(R.drawable.good_night_img);
                    textView.setText("Night");
                    count = 1;
                } else {
                    imageView.setImageResource(R.drawable.good_morning_img);
                    textView.setText("Morning");
                    count = 0;
                }
            }
            @SuppressLint("SetTextI18n")
            @Override
            public void onSwipeLeft() {
                if (count == 0) {
                    imageView.setImageResource(R.drawable.good_night_img);
                    textView.setText("Night");
                    count = 1;
                } else {
                    imageView.setImageResource(R.drawable.good_morning_img);
                    textView.setText("Morning");
                    count = 0;
                }
            }
        });
        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    testEmail();
                }
            }
        });
        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    testPWD();
                }
            }
        });
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testEmail();
                testPWD();
                if(pwdLock==1 && emailLock==1){
                    mPresenter.login();
                }else {
                    Toast.makeText(LoginActivity.this,"Please fix errors!",Toast.LENGTH_SHORT).show();
                }
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisteFragment registeFragment = new RegisteFragment();
                registeFragment.show(getSupportFragmentManager(),"Email");
            }
        });
    }

    private void testEmail(){
        if (email.getText().length()==0){
            emailLayout.setError("You must input the email address!");
            emailLock = 0;
        }else if(!email.getText().toString().matches("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*")) {
            emailLayout.setError("Wrong email format!");
            emailLock = 0;
        }else {
            emailLayout.setErrorEnabled(false);
            emailLock = 1;
        }
    }

    private void testPWD(){
        if(password.getText().length() == 0){
            pwdLayout.setError("You must enter the PassWord");
            pwdLock = 0;
        } else {
            pwdLayout.setErrorEnabled(false);
            pwdLock = 1;
        }
    }



    @Override
    public void loginSuccessfully() {
        editor.putString("email",getEmail());
        editor.putString("pwd",getUserPassWord());
        editor.commit();
        Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
        startActivity(intent);
    }

    @Override
    public void loginFailed() {
        Looper.prepare();
        Toast.makeText(LoginActivity.this,"Sign in failed!",Toast.LENGTH_SHORT).show();
        Looper.loop();
    }

    @Override
    public String getEmail() {
        return email.getText().toString();
    }

    @Override
    public String getUserPassWord() {
        return password.getText().toString();
    }

    @Override
    public void attachPresenter(LoginContract.Presenter presenter) {
        mPresenter=presenter;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
}
