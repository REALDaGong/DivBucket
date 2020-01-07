package com.tj007.divbucketmvp.contract;

import com.tj007.divbucketmvp.BasePresenter;
import com.tj007.divbucketmvp.BaseView;

public interface RegistInfoContract {
    interface View extends BaseView<Presenter> {
        void registSuccessfully();
        void registFailed();
        String getEmail();
        String getPwd();
    }

    interface Presenter extends BasePresenter {
        void register();
    }
}
