package com.tj007.divbucketmvp.contract;

import com.tj007.divbucketmvp.BasePresenter;
import com.tj007.divbucketmvp.BaseView;
import com.tj007.divbucketmvp.chooseWatchingTarget.utils.AsyncResponse;
import com.tj007.divbucketmvp.model.warpper.ListData;

import java.util.List;

public interface StatisticContract {
    interface View extends BaseView<Presenter> {

    }
    interface Presenter extends BasePresenter {

    }
}