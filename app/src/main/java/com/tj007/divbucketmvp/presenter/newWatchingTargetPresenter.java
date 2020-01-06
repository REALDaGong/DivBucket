package com.tj007.divbucketmvp.presenter;

import com.tj007.divbucketmvp.contract.newWatchingTargetContract;
import com.tj007.divbucketmvp.model.DatabaseManager;
import com.tj007.divbucketmvp.model.warpper.watchingTargetWarpper;

import org.jetbrains.annotations.NotNull;

public class newWatchingTargetPresenter implements newWatchingTargetContract.Presenter {

    private DatabaseManager databaseManager=DatabaseManager.getInstance();
    private newWatchingTargetContract.View mView=null;
    public newWatchingTargetPresenter(@NotNull newWatchingTargetContract.View view){
        mView=view;
        mView.attachPresenter(this);
    }

    @Override
    public void saveAll(watchingTargetWarpper data) {
        databaseManager.saveCache(data.getName(),data.getCls());
    }

    @Override
    public void start() {

    }

    @Override
    public void detachView() {

    }
}
