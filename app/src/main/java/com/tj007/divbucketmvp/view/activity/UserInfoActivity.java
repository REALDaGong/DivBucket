package com.tj007.divbucketmvp.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import com.tj007.divbucketmvp.R;
import com.tj007.divbucketmvp.adapter.TextWatcherAdapter;
import com.tj007.divbucketmvp.contract.UserInfoContract;
import com.tj007.divbucketmvp.presenter.UserInfoPresenter;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


public class UserInfoActivity extends AppCompatActivity implements UserInfoContract.View {
    Toolbar toolbar;
    private UserInfoContract.Presenter mPresenter;
    private EditText email;
    private EditText sex;
    private EditText age;
    private EditText work;
    private EditText position;
    private EditText name;
    private Button changeConfirm;
    private ImageView backButton;

    private boolean statusAge;
    private boolean statusWork;
    private boolean statusPosition;
    private boolean statusSex;
    private boolean statusChange;
    private boolean statusName;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        mPresenter = new UserInfoPresenter(this);
        sharedPreferences= getSharedPreferences("data", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        statusAge = true;
        statusWork = true;
        statusPosition = true;
        statusSex = true;
        statusName = true;
        statusChange = false;

        toolbar = findViewById(R.id.toolbar_user_info);
        email = findViewById(R.id.etx_email);
        sex = findViewById(R.id.etx_sex);
        age = findViewById(R.id.etx_age);
        work = findViewById(R.id.etx_work);
        name = findViewById(R.id.etx_name);
        position = findViewById(R.id.etx_position);
        changeConfirm = findViewById(R.id.btn_confirm_change);
        backButton =findViewById(R.id.back_to_main);

        init();

        changeConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String re = "";
                int flag = 1;
                if (!statusAge){
                    re+= "年龄 ";
                    flag = 0;
                }
                if (!statusSex){
                    re+= "性别 ";
                    flag = 0;
                }
                if (!statusName){
                    re+= "昵称 ";
                    flag = 0;
                }
                if(flag == 1){
                    //执行修改操作的网络请求
                    Log.d("changeInfo","now");
                    mPresenter.changeUserInfo();
                }else{
                    Toast.makeText(UserInfoActivity.this,"格式错误："+re,Toast.LENGTH_SHORT).show();
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void init(){
        email.setEnabled(false);
        email.setText(sharedPreferences.getString("email",""));
        sex.addTextChangedListener(sexAdapter);
        sex.setText(sharedPreferences.getString("sex",""));
        age.addTextChangedListener(ageAdapter);
        age.setText(sharedPreferences.getString("age",""));
        work.addTextChangedListener(workAdapter);
        work.setText(sharedPreferences.getString("work",""));
        position.addTextChangedListener(posAdapter);
        position.setText(sharedPreferences.getString("pos",""));
        name.addTextChangedListener(nameAdapter);
        name.setText(sharedPreferences.getString("name",""));
        changeConfirm.setEnabled(false);
    }

    TextWatcherAdapter sexAdapter = new TextWatcherAdapter() {
        @Override
        public void afterTextChanged(Editable s) {
            changeConfirm.setEnabled(true);
            if ("男".equals(s.toString())||"女".equals(s.toString())){
                statusSex = true;
            } else {
                Toast.makeText(UserInfoActivity.this,"应该是男/女",Toast.LENGTH_SHORT).show();
                statusSex = false;
            }
        }
    };

    TextWatcherAdapter ageAdapter = new TextWatcherAdapter() {
        @Override
        public void afterTextChanged(Editable s) {
            changeConfirm.setEnabled(true);
            try {
                //把字符串强制转换为数字
                int num=Integer.valueOf(s.toString());
                if (num>=0){
                    statusAge = true;
                } else {
                    Toast.makeText(UserInfoActivity.this,"应大于0",Toast.LENGTH_SHORT).show();
                    statusAge = false;
                }
            } catch (Exception e) {
                //如果抛出异常，返回False
                Toast.makeText(UserInfoActivity.this,"格式需为自然数",Toast.LENGTH_SHORT).show();
                statusAge = false;
            }
        }
    };

    TextWatcherAdapter workAdapter = new TextWatcherAdapter() {
        @Override
        public void afterTextChanged(Editable s) {
            changeConfirm.setEnabled(true);
        }
    };

    TextWatcherAdapter posAdapter = new TextWatcherAdapter() {
        @Override
        public void afterTextChanged(Editable s) {
            changeConfirm.setEnabled(true);
        }
    };
    TextWatcherAdapter nameAdapter = new TextWatcherAdapter() {
        @Override
        public void afterTextChanged(Editable s) {
            changeConfirm.setEnabled(true);
            Log.d("testSex",s.toString()+"###");
            Log.d("testSex",s.length()+"###");
            if(s.length()> 0 && s.length() < 20){
                statusName = true;
            }else {
                Toast.makeText(UserInfoActivity.this,"字符数应位于1-20之间",Toast.LENGTH_SHORT).show();
                statusName = false;
            }
        }
    };

    @Override
    public void changeUserInfoSuccess() {
        editor.putString("email",email.getText().toString());
        editor.putString("sex",sex.getText().toString());
        editor.putString("age",age.getText().toString());
        editor.putString("work",work.getText().toString());
        editor.putString("pos",position.getText().toString());
        editor.putString("name",name.getText().toString());
        editor.commit();
        Looper.prepare();
        Toast.makeText(UserInfoActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
        Looper.loop();
    }

    @Override
    public void changeUserInfoFailed() {
        Looper.prepare();
        Toast.makeText(UserInfoActivity.this,"网络失败，请重试！",Toast.LENGTH_SHORT).show();
        Looper.loop();
    }

    @Override
    public JSONObject getUserInfo() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id",email.getText().toString());
            if("男".equals(sex.getText().toString())){
                jsonObject.put("sex",1);
            }else {
                jsonObject.put("sex",0);
            }
            jsonObject.put("age",Integer.parseInt(age.getText().toString()));
            jsonObject.put("work",work.getText().toString());
            jsonObject.put("location",position.getText().toString());
            jsonObject.put("user_name",name.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    public void attachPresenter(UserInfoContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
