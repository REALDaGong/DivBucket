package com.tj007.divbucketmvp.chooseWatchingTarget.utils;

public interface callback<T> {
    public void processFinish(T output, ASYNC_RES_STATE state);
    public void processFailed(Throwable throwable, ASYNC_RES_STATE state);
}