package com.zhuye.minsu.common.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhuye.minsu.R;
import com.zhuye.minsu.api.DataProvider;
import com.zhuye.minsu.base.BaseFragment;
import com.zhuye.minsu.common.adapter.BGABannerAdapter;
import com.zhuye.minsu.sign.CalendarSignActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bingoogolapple.bgabanner.BGABanner;


/**
 * Created by hpc on 2017/12/1.
 */

public class HomeFragment extends BaseFragment {


    @BindView(R.id.banner_pager)
    BGABanner bannerPager;

    private View view;


    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    protected void initListener() {


    }


    @Override
    protected void initData() {
        getBannerData();
    }

    private void getBannerData() {
        bannerPager = view.findViewById(R.id.banner_pager);
        bannerPager.setAdapter(new BGABannerAdapter(getActivity()));
        ArrayList<Integer> bannerImageData = DataProvider.getBannerImage();
        List<Integer> bannerImage = new ArrayList<Integer>();
//        for (int i = 0; i < bannerImageData.size(); i++) {
        bannerImage.addAll(bannerImageData);
//        }
        bannerPager.setData(bannerImage, null);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
