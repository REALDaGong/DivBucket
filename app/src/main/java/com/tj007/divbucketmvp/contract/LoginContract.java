package com.tj007.divbucketmvp.contract;

import com.tj007.divbucketmvp.BasePresenter;
import com.tj007.divbucketmvp.BaseView;

public interface  LoginContract {
    interface View extends BaseView<Presenter> {
        void loginSuccessfully();
        void loginFailed();
        String getEmail();
        String getUserPassWord();
    }

    interface Presenter extends BasePresenter {
        void login();
    }
}
