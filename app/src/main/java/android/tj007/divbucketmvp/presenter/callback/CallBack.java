package android.tj007.divbucketmvp.presenter.callback;

public interface CallBack<T> {
    void onSuccess(T response);

    void onError(Throwable t);
}
