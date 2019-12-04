package com.tj007.divbucketmvp.chooseWatchingTarget;
//我有一些事情不理解，比如，这里有一个isActive方法，但是isActive应该很多view都需要，所以
//我想写一个虚类把isActive搞定，但是这样就没法这样写到一个contract里面
//我觉得理想情况下这里应该只有业务逻辑
//咋办？

import com.tj007.divbucketmvp.BasePresenter;
import com.tj007.divbucketmvp.BaseView;
import com.tj007.divbucketmvp.components.treeview.DomNode;

//还有presenter的事情，类似于绑定view，检查指针的方法应该是大家都一样的，但是要是这样的话就得有个泛型父类
//你又不能多继承
//不这么做的话会把指针丢失的风险甩的到处都是
//原则上每一个程序员都不值得信任，所以要把这种可能规避到最小
//但我太菜了
public interface ChooseWatchingTargetContract {
    //这里的泛型可以把对应的view和presenter绑死，是个好事情
    interface View extends BaseView<Presenter> {
        boolean isActive();
        void loadNewDOMTree(DomNode root);

        //1.超时怎么办？
        //2.不知道为啥，hideLoading就是没调用怎么办？
        void showLoading();
        void hideLoading();
        void showError(String Message);
    }

    interface Presenter extends BasePresenter {
        //这个东西没有返回值。。。或许我应该返回future？
        //需要叫"asyncxxxx"吗？
        void asyncRequestHTML(String URL);
    }
}
