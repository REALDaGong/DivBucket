package com.tj007.divbucketmvp;

public interface BasePresenter {
    void start();
    void detachView();

    //需不需要把getView写在这里？写在这里就非用泛型不可
    //T getView();
}
