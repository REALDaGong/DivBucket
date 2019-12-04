package com.tj007.divbucketmvp.chooseWatchingTarget.utils;

public interface AsyncResponse<T> {
    public void processFinish(T output, ASYNC_RES_STATE state);
}
