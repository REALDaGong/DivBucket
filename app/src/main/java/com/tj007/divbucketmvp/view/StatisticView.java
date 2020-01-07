package com.tj007.divbucketmvp.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tj007.divbucketmvp.R;
import com.tj007.divbucketmvp.contract.StatisticContract;

public class StatisticView extends Fragment implements StatisticContract.View {

    StatisticContract.Presenter mPresenter=null;


    @Override
    public void attachPresenter(StatisticContract.Presenter presenter) {
        mPresenter=presenter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistic,container,false);
        return view;
    }
}
