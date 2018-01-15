package com.minsu.minsu.common.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.minsu.minsu.R;
import com.minsu.minsu.base.BaseFragment;

/**
 * Created by hpc on 2017/12/6.
 */

public class ChatRecordFragment extends BaseFragment {
    private View view;
    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.fragment_me, container, false);
        return view;
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {

    }
}
