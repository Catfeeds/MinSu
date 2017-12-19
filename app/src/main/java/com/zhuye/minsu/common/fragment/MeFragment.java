package com.zhuye.minsu.common.fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhuye.minsu.R;
import com.zhuye.minsu.base.BaseFragment;
import com.zhuye.minsu.base.BaseNoFragment;


/**
 * Created by hpc on 2017/12/1.
 */

public class MeFragment extends BaseFragment {


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
