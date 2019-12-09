package com.tj007.divbucketmvp.showWatchingTarget;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class showWatchingTargetView extends Fragment implements showWatchingTargetContract.View {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void updateAll() {

    }

    @Override
    public void attachPresenter(showWatchingTargetContract.Presenter presenter) {

    }
}
