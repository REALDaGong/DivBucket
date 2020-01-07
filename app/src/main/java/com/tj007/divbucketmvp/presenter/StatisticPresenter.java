package com.tj007.divbucketmvp.presenter;

import com.tj007.divbucketmvp.contract.StatisticContract;

import org.jetbrains.annotations.NotNull;

public class StatisticPresenter implements StatisticContract.Presenter {
    private StatisticContract.View mView;

    public StatisticPresenter(@NotNull StatisticContract.View mView){
        this.mView=mView;
        mView.attachPresenter(this);
    }


    @Override
    public void start() {

    }

    @Override
    public void detachView() {

    }
}
