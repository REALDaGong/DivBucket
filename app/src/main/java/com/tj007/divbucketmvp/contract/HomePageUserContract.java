package com.tj007.divbucketmvp.contract;

import com.tj007.divbucketmvp.BasePresenter;
import com.tj007.divbucketmvp.BaseView;

import org.json.JSONObject;

public interface HomePageUserContract {
    interface View extends BaseView<Presenter> {
        void getUserInfoSuccess(JSONObject userInfo);
        void getUserInfoFailed();
        String getEmail();
    }

    interface Presenter extends BasePresenter {
        void getUserInfo();
    }
}
