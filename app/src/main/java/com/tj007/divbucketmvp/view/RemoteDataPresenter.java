package com.tj007.divbucketmvp.view;

//失败的设计？

/*
public abstract class RemoteDataPresenter implements BasePresenter{

    protected Reference<T> ViewRef;

    @Override
    public void start() {

    }


    @Override
    public void detachView() {
        if(ViewRef!=null){
            ViewRef.clear();
            ViewRef=null;
        }
    }

    @Override
    public boolean isAttached() {
        return !(ViewRef==null);
    }

    @Override
    public T getView() {
        return ViewRef.get();
    }

}
 */
