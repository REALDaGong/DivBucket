package com.tj007.divbucketmvp.contract;

import com.tj007.divbucketmvp.BasePresenter;
import com.tj007.divbucketmvp.BaseView;
import com.tj007.divbucketmvp.model.warpper.watchingTargetWarpper;


public interface newWatchingTargetContract {
    interface View extends BaseView<Presenter> {
        void showLoading();
        void hideLoading();
        void showError();
    }
    interface Presenter extends BasePresenter {
        void saveAll(watchingTargetWarpper data);
    }
}
