package com.tj007.divbucketmvp.contract;

import com.tj007.divbucketmvp.BasePresenter;
import com.tj007.divbucketmvp.BaseView;

public interface RegisterContract {
    interface View extends BaseView<Presenter> {
        void verifySuccessfully();
        void verifyFailed();
        String getEmail();
    }

    interface Presenter extends BasePresenter {
        void verifyEmail();
    }
}
