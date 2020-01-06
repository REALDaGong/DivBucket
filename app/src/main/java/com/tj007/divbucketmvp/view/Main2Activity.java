package com.tj007.divbucketmvp.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.example.myapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tj007.divbucketmvp.presenter.newWatchingTargetPresenter;
import com.tj007.divbucketmvp.presenter.showWatchingTargetPresenter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import butterknife.BindView;
import butterknife.ButterKnife;

//https://juejin.im/post/5d3807375188251b2569fb62#heading-1
//为啥能这么复杂？？？？？？
//好意思叫jet？
public class Main2Activity extends AppCompatActivity {

    @BindView(R.id.nav_view)
    BottomNavigationView nav_view;

    @BindView(R.id.host)
    FrameLayout host;
    private ActionBar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);
        toolbar=getSupportActionBar();
        initToobarAction();
        initMVP();
        //让人痛苦，我在这玩意身上浪费了5个小时也没明白怎么写MVP，只能重新动手了
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        //AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
        //        R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
        //        .build();
        //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        //NavigationUI.setupWithNavController(navView, navController);


    }
    //Edittext在激活时，点击别的地方不会移除它的焦点，这里手动移除，当然会影响所有使用这个activity的fragment，以后有需要再改。
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
                    toolbar.setTitle("监控列表");
                    switchFragment(showWatchingTargetView);
                    return true;
                case R.id.navigation_dashboard:
                    toolbar.setTitle("新的监控");
                    switchFragment(newWatchingTargetView);
                    return true;
                case R.id.navigation_notifications:
                    toolbar.setTitle("四大皆空");
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
        if(fragment==currentFrag)return;
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
