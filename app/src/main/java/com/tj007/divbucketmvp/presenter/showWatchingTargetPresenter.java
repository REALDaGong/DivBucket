package com.tj007.divbucketmvp.presenter;

import android.util.Log;

import com.tj007.divbucketmvp.chooseWatchingTarget.utils.ASYNC_RES_STATE;
import com.tj007.divbucketmvp.chooseWatchingTarget.utils.AsyncResponse;
import com.tj007.divbucketmvp.model.DatabaseManager;
import com.tj007.divbucketmvp.model.NETWORK_FETCHER_ERROR;
import com.tj007.divbucketmvp.model.NetworkDataFetcher;
import com.tj007.divbucketmvp.model.warpper.ListData;
import com.tj007.divbucketmvp.contract.showWatchingTargetContract;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Dictionary;
import java.util.List;


public class showWatchingTargetPresenter implements showWatchingTargetContract.Presenter {
    private showWatchingTargetContract.View mView;

    private List<ListData> mlist= Collections.synchronizedList(new ArrayList<>());
    private boolean updated=false;
    private DatabaseManager Dmanager=DatabaseManager.getInstance();
    private NetworkDataFetcher fetcher=NetworkDataFetcher.getInstance();

    public showWatchingTargetPresenter(showWatchingTargetContract.View view){
        mView=view;
        mView.attachPresenter(this);

        //初始化一批假数据
        ListData d=new ListData();
        d.msg="悍匪";
        d.lastUpdateTime=new Date();
        d.name="返回目的地";
        for(int i=0;i<3;i++) {
            mlist.add(d);
        }
    }
    @Override
    public void start() {

    }

    @Override
    public void updateAll(AsyncResponse callback) {
        mlist.clear();
        class mythread extends Thread{
            @Override
            public void run(){

                List<String> urls= Dmanager.getURL("",-1);
                for (String url:urls
                     ) {
                    List<String> paths=Dmanager.getPathInURL(url,"");
                    List<String> data=fetcher.getData(url,paths);
                    if(data!=null) {
                        if(data.size()==0){
                            //没有数据,由于没有可用的path
                        }else if(data.get(0).equals(NETWORK_FETCHER_ERROR.NO_NETWORK)){
                            addListData(url,"网络异常",new Date());
                        }else if(data.get(0).equals(NETWORK_FETCHER_ERROR.TIMEOUT)){
                            addListData(url,"网络超时",new Date());
                        }
                        Dmanager.writeNewWatchingResult("defaultpath",url,concatListString(decodeRawListData(data)));
                        addListData(url, concatListString(data), new Date());
                    }else{
                        //异常，来源是countdownlauch的interrupt。
                        //no network.
                    }
                }
                notifyDataUpdated();

            }
        }

        mythread t=new mythread();
        t.start();
    }

    //返回的数据是List<String>，里面有一些特殊字段，不是有效信息，一般只是用来指示是否出错，用这两个来清理

    private List<String> decodeRawListData(List<String> data){
        return data;
    }

    private void notifyDataUpdated(){
        mView.updateViewOnly();
    }

    private String concatListString(List<String> list){
        StringBuilder sb=new StringBuilder("");
        for (String s:list
             ) {
            sb.append(s);
            sb.append('\n');
        }
        return sb.toString();
    }

    private void addListData(String name,String msg,Date date){
        ListData listData=new ListData();
        listData.name=name;
        listData.lastUpdateTime=date;
        listData.msg=msg;
        mlist.add(listData);
    }

    @Override
    public void updateById(int id, AsyncResponse callback) {

    }

    //备注名
    @Override
    public void updateByName(String name, AsyncResponse callback){

    }

    @Override
    public void detachView() {

    }

    @Override
    public List<ListData> getListData() {
        updated=false;
        return mlist;
    }
    @Override
    public boolean isUpdated() {
        return updated;
    }
}
