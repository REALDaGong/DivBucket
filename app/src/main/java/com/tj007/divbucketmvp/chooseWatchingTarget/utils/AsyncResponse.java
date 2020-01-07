package com.tj007.divbucketmvp.chooseWatchingTarget.utils;

public abstract class AsyncResponse<T> implements callback<T> {
    @Override
    public void processFailed(Throwable throwable, ASYNC_RES_STATE state){
        throwable.printStackTrace();
    }
}
