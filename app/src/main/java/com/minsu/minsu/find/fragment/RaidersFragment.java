package com.minsu.minsu.find.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.minsu.minsu.R;
import com.minsu.minsu.base.BaseFragment;

/**
 * Created by hpc on 2017/12/25.
 */

public class RaidersFragment extends BaseFragment {
    private View view;
    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.fragment_raiders, container, false);
        return view;
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {

    }
}
