package com.tj007.divbucketmvp.showWatchingTarget;

import com.tj007.divbucketmvp.BasePresenter;
import com.tj007.divbucketmvp.BaseView;

public interface showWatchingTargetContract {
    interface View extends BaseView<Presenter>{
        void updateAll();

    }
    interface Presenter extends BasePresenter{
        void updateAll();
        void updateById(int id);
    }
}
