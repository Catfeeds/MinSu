package com.minsu.minsu.common.fragment;


import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.minsu.minsu.R;
import com.minsu.minsu.base.BaseFragment;
import com.minsu.minsu.widget.SmartTab.UtilsV4.v4.FragmentPagerItem;
import com.minsu.minsu.widget.SmartTab.UtilsV4.v4.FragmentPagerItemAdapter;
import com.minsu.minsu.widget.SmartTab.UtilsV4.v4.FragmentPagerItems;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by hpc on 2017/12/1.
 */

public class MessageFragment extends BaseFragment {


    @BindView(R.id.viewpager)
    ViewPager viewpager;
    Unbinder unbinder;
    @BindView(R.id.tab)
    FrameLayout tab;
    private View view;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {

        view = inflater.inflate(R.layout.fragment_message, container, false);
        return view;
    }

    @Override
    protected void initListener() {

        tab.addView(LayoutInflater.from(getActivity()).inflate(R.layout.tab_top_layout, tab, false));
        SmartTabLayout viewPagerTab = view.findViewById(R.id.viewpagertab);
        FragmentPagerItems pages = new FragmentPagerItems(getActivity());
        pages.add(FragmentPagerItem.of("订单提示", OrderPromptFragment.class));
        pages.add(FragmentPagerItem.of("聊天记录", ChatRecordFragment.class));
        pages.add(FragmentPagerItem.of("系统消息", SystemMessageFragment.class));
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