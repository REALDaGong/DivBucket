package com.tj007.divbucketmvp.view.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tj007.divbucketmvp.R;
import com.tj007.divbucketmvp.contract.HomePageUserContract;
import com.tj007.divbucketmvp.presenter.HomePageUserPresenter;
import com.tj007.divbucketmvp.presenter.newWatchingTargetPresenter;
import com.tj007.divbucketmvp.presenter.showWatchingTargetPresenter;
import com.tj007.divbucketmvp.utils.ScreenInfoUtils;

import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.tj007.divbucketmvp.view.NewWatchingTargetView;
import com.tj007.divbucketmvp.view.ShowWatchingTargetView;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomePageActivity extends AppCompatActivity implements HomePageUserContract.View {

    private final static int LOAD_IMAGE = 1;

    @BindView(R.id.nav_view)
    BottomNavigationView nav_view;

    @BindView(R.id.host)
    FrameLayout host;
    private Toolbar toolbar;

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
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenInfoUtils.fullScreen(this);
        setContentView(R.layout.activity_home_page);

        ButterKnife.bind(this);
        mPresenter = new HomePageUserPresenter(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        title = findViewById(R.id.title_main);
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

        initToobarAction();
        initMVP();

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
    public boolean dispatchTouchEvent(MotionEvent ev){
        if(ev.getAction()==MotionEvent.ACTION_DOWN){
            View v=getCurrentFocus();
            if(isShouldHideKeyBoard(v,ev)){
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
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
    //****************************************************
    private boolean isShouldHideKeyBoard(View v, MotionEvent ev){
        if(v != null &&(v instanceof EditText)){
            int[] l={0,0};
            v.getLocationOnScreen(l);
            int left=l[0],
                    top=l[1],
                    bottom = top+v.getHeight(),
                    right = left+v.getWidth();
            if(ev.getX()>left && ev.getX()<right && ev.getY()>top && ev.getY()<bottom){
                Log.i("2","flase.");
                return false;

                //点在里面
            }else {
                Log.i("2","ture.");
                return true;
            }
        }
        return false;
        //忽略其它的控件，目前只响应Edittext。
        //美中不足的是点不同的编辑区，键盘会跳，不好看
        //不是优先任务。
    }

    //居然是个服务？
    private void hideKeyboard(IBinder token){
        if(token !=null){
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token,InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void initView(){

    }
    private void initToobarAction(){
        nav_view.setOnNavigationItemSelectedListener((item)->{
            switch (item.getItemId()){
                case R.id.navigation_home:
                    title.setText("监控列表");
                    switchFragment(showWatchingTargetView);
                    return true;
                case R.id.navigation_dashboard:
                    title.setText("新的监控");
                    switchFragment(newWatchingTargetView);
                    return true;
                case R.id.navigation_notifications:
                    title.setText("四大皆空");
                    //switchFragment(chooseWatchingTargetView);
                    return true;
            }
            return false;
        });
    }

    private Fragment currentFrag=new ShowWatchingTargetView();

    private void initMVP(){
        //全部实例化...
        newWatchingTargetView=new NewWatchingTargetView();
        newWatchingTargetPresenter presenter2=new newWatchingTargetPresenter(newWatchingTargetView);
        showWatchingTargetView=new ShowWatchingTargetView();
        showWatchingTargetPresenter presenter3=new showWatchingTargetPresenter(showWatchingTargetView);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.host,newWatchingTargetView,newWatchingTargetView.getClass().getName())
                .add(R.id.host,showWatchingTargetView,showWatchingTargetView.getClass().getName())
                .hide(newWatchingTargetView)
                .hide(showWatchingTargetView);
        transaction.commit();

        switchFragment(showWatchingTargetView);
    }

    private NewWatchingTargetView newWatchingTargetView;
    private ShowWatchingTargetView showWatchingTargetView;

    private void switchFragment(Fragment fragment){
        if(fragment==currentFrag) {
            return;
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (fragment==null){

            transaction.add(fragment,fragment.getClass().getName());

        }else{
            if(currentFrag!=null) {
                transaction.hide(currentFrag).show(fragment);
            }
        }
        currentFrag=fragment;
        transaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        newWatchingTargetView.addPath(data.getStringArrayExtra("path"));
    }
}
