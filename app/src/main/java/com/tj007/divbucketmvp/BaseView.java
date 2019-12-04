package com.tj007.divbucketmvp;

public interface BaseView<T> {
    void attachPresenter(T presenter);
}
