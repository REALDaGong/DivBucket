package com.tj007.divbucketmvp.contract;

import com.tj007.divbucketmvp.BasePresenter;
import com.tj007.divbucketmvp.BaseView;
import com.tj007.divbucketmvp.chooseWatchingTarget.utils.AsyncResponse;
import com.tj007.divbucketmvp.model.warpper.ListData;

import java.util.List;

public interface showWatchingTargetContract {
    interface View extends BaseView<Presenter>{
        void updateAll();
        void updateViewOnly();
    }
    interface Presenter extends BasePresenter{
        //按照数据库里的顺序返回所有的更新结果。
        void updateAll(AsyncResponse callback);
        //按照选择的id返回一条结果。

        void updateById(int id, AsyncResponse callback);
        //更准确地说，返回的不是即时的“结果”，future更好。
        void updateByName(String name, AsyncResponse callback);

        //得到目前的结果列表，这个东西被维护在presenter里面。
        List<ListData> getListData();
        boolean isUpdated();
    }
}
