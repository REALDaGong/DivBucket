package android.tj007.divbucketmvp.presenter;

import android.tj007.divbucketmvp.model.impl.LoginAModelImpl;
import android.tj007.divbucketmvp.model.inter.ILoginAModel;
import android.tj007.divbucketmvp.presenter.inter.ILoginAPresenter;
import android.tj007.divbucketmvp.view.inter.ILoginAView;

public class LoginAPresenterImpl implements ILoginAPresenter {
    private ILoginAView mILoginAView;
    private ILoginAModel mILoginAModel;

    public LoginAPresenterImpl(ILoginAView aILoginAView) {
        mILoginAView = aILoginAView;
        mILoginAModel = new LoginAModelImpl();
    }

    @Override
    public boolean login() {
        return true;
    }
}
