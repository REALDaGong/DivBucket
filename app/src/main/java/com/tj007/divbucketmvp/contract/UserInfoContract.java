package com.tj007.divbucketmvp.contract;

import com.tj007.divbucketmvp.BasePresenter;
import com.tj007.divbucketmvp.BaseView;

import org.json.JSONObject;

public interface UserInfoContract {
    interface View extends BaseView<Presenter> {
        void changeUserInfoSuccess();
        void changeUserInfoFailed();
        JSONObject getUserInfo();
    }

    interface Presenter extends BasePresenter {
        void changeUserInfo();
    }
}
