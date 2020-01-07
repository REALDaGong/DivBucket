package com.tj007.divbucketmvp.contract;

import com.tj007.divbucketmvp.BasePresenter;
import com.tj007.divbucketmvp.BaseView;

import org.json.JSONObject;

public interface UploadAvatarContract {
    interface View extends BaseView<Presenter> {
        void uploadAvatarSuccess(String avatarUrl);
        void uploadAvatarFailed();
        String getImagePath();
        String getEmail();
    }

    interface Presenter extends BasePresenter {
        void uploadAvatar();
    }
}
