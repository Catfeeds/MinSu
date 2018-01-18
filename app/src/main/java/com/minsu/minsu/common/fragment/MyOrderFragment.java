package com.minsu.minsu.common.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.minsu.minsu.R;
import com.minsu.minsu.base.BaseFragment;
import com.minsu.minsu.common.fragment.landlord.LAllOrderFragment;
import com.minsu.minsu.common.fragment.landlord.LDaiRuZhuOrderFragment;
import com.minsu.minsu.common.fragment.landlord.LRuZhuZhongOrderFragment;
import com.minsu.minsu.common.fragment.landlord.LYiTuiFangOrderFragment;
import com.minsu.minsu.widget.SmartTab.UtilsV4.v4.FragmentPagerItem;
import com.minsu.minsu.widget.SmartTab.UtilsV4.v4.FragmentPagerItemAdapter;
import com.minsu.minsu.widget.SmartTab.UtilsV4.v4.FragmentPagerItems;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by hpc on 2018/1/17.
 */

public class MyOrderFragment extends BaseFragment {
    @BindView(R.id.tab)
    FrameLayout tab;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    Unbinder unbinder;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.iv_left)
    ImageView ivLeft;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private View view;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.fragment_my_order, container, false);
        return view;
    }

    @Override
    protected void initListener() {
        toolbarTitle.setText("我的订单");
        tab.addView(LayoutInflater.from(getActivity()).inflate(R.layout.tab_top_layout, tab, false));
        SmartTabLayout viewPagerTab = view.findViewById(R.id.viewpagertab);
        FragmentPagerItems pages = new FragmentPagerItems(getActivity());
        pages.add(FragmentPagerItem.of("全部", LAllOrderFragment.class));
        pages.add(FragmentPagerItem.of("待入住", LDaiRuZhuOrderFragment.class));
        pages.add(FragmentPagerItem.of("入住中", LRuZhuZhongOrderFragment.class));
        pages.add(FragmentPagerItem.of("已退房", LYiTuiFangOrderFragment.class));
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(getChildFragmentManager(), pages);
        viewpager.setAdapter(adapter);
        viewPagerTab.setViewPager(viewpager);
    }

    @Override
    protected void initData() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
