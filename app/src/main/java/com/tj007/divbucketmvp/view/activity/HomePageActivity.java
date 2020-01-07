package com.tj007.divbucketmvp.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import com.tj007.divbucketmvp.R;
import com.tj007.divbucketmvp.contract.HomePageUserContract;
import com.tj007.divbucketmvp.presenter.HomePageUserPresenter;
import com.tj007.divbucketmvp.utils.ScreenInfoUtils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

public class HomePageActivity extends AppCompatActivity implements HomePageUserContract.View {

    private final static int LOAD_IMAGE = 1;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private HomePageUserContract.Presenter mPresenter;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case LOAD_IMAGE:
                    Glide.with(HomePageActivity.this).load(sharedPreferences.getString("avatar","")).into(callDrawer);
                    Glide.with(HomePageActivity.this).load(sharedPreferences.getString("avatar","")).into(avatar);
            }
        }
    };

    private Button logOut;
    private ImageView callDrawer;
    private ImageView avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenInfoUtils.fullScreen(this);
        setContentView(R.layout.activity_home_page);

        mPresenter = new HomePageUserPresenter(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        NavigationView navigationview = (NavigationView) findViewById(R.id.navigation_view);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        sharedPreferences= getSharedPreferences("data", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        logOut = findViewById(R.id.footer_item_out);

        callDrawer = (ImageView) findViewById(R.id.call_drawer);

        callDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        setSupportActionBar(toolbar);//将toolbar与ActionBar关联
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.clear();
                editor.apply();
                Intent intent = new Intent(HomePageActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        /*---------------------------添加头布局和尾布局-----------------------------*/
        //获取xml头布局view
        View headerView = navigationview.getHeaderView(0);
        //添加头布局的另外一种方式
        //View headview=navigationview.inflateHeaderView(R.layout.navigationview_header);

        //寻找头部里面的控件
        TextView userName = headerView.findViewById(R.id.user_name);
        userName.setText(sharedPreferences.getString("name",""));
        avatar = headerView.findViewById(R.id.iv_head);
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePageActivity.this,UploadAvatarActivity.class);
                startActivity(intent);
            }
        });
        navigationview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        });
        ColorStateList csl = (ColorStateList) getResources().getColorStateList(R.color.nav_menu_text_color);



        //设置item的条目颜色
        navigationview.setItemTextColor(csl);
        //去掉默认颜色显示原来颜色  设置为null显示本来图片的颜色
        navigationview.setItemIconTintList(csl);

        //设置消息数量
        //LinearLayout llAndroid = (LinearLayout) navigationview.getMenu().findItem(R.id.single_1).getActionView();
        //TextView msg = (TextView) llAndroid.findViewById(R.id.msg_bg);
        //msg.setText("99+");

        //设置条目点击监听
        navigationview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                //点击哪个按钮
                switch (menuItem.getItemId()){
                    case R.id.user_info_item:
                        Intent intent = new Intent(HomePageActivity.this,UserInfoActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

        mPresenter.getUserInfo();

        /*---------------------------自定义侧边栏布局-----------------------------*/
//        getSupportFragmentManager().beginTransaction().replace(R.id.navigation_view,
//                new NavigationViewFragment()).commit();
    }

    @Override
    public void getUserInfoSuccess(JSONObject userInfo) {
        Log.d("getUSerInfo","success:"+userInfo.toString());
        try {
            Log.d("getUSerInfo","success:"+userInfo.has("avatar")+"$$$$$$$$$$$$$");
            editor.putString("avatar",userInfo.getString("avatar"));
            editor.putString("name",userInfo.getString("user_name"));
            if (userInfo.getInt("sex") == 1){
                editor.putString("sex","男");
            }
            if (userInfo.getInt("sex") == 0){
                editor.putString("sex","女");
            }
            editor.putString("age",Integer.toString(userInfo.getInt("age")));
            editor.putString("work",userInfo.getString("work"));
            editor.putString("pos",userInfo.getString("location"));
            editor.commit();
            Message message = new Message();
            message.what = LOAD_IMAGE;
            handler.sendMessage(message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getUserInfoFailed() {
        Looper.prepare();
        Toast.makeText(HomePageActivity.this,"网络错误！",Toast.LENGTH_SHORT).show();
        Looper.loop();
    }

    @Override
    public String getEmail() {
        return sharedPreferences.getString("email","");
    }

    @Override
    public void attachPresenter(HomePageUserContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
