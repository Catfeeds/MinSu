package com.minsu.minsu.sign;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.minsu.minsu.R;
import com.minsu.minsu.base.BaseFragment;

/**
 * Created by chniccs on 16/8/17.
 * 签到日历，已经将日历功能封装到SignCalendarView中
 */
public class SignFragment extends BaseFragment {



    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_sign,container,false);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {

    }
}
