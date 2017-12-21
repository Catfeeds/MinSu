package com.zhuye.minsu.common.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhuye.minsu.R;
import com.zhuye.minsu.base.BaseFragment;
import com.zhuye.minsu.user.AccountActivity;
import com.zhuye.minsu.user.CollectActivity;
import com.zhuye.minsu.user.CouponActivity;
import com.zhuye.minsu.user.HelpActivity;
import com.zhuye.minsu.user.IntegralActivity;
import com.zhuye.minsu.user.OrderActivity;
import com.zhuye.minsu.user.UserInfoActivity;
import com.zhuye.minsu.user.setting.SettingActivity;
import com.zhuye.minsu.widget.ActionBarClickListener;
import com.zhuye.minsu.widget.RoundedCornerImageView;
import com.zhuye.minsu.widget.TranslucentActionBar;
import com.zhuye.minsu.widget.TranslucentScrollView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by hpc on 2017/12/1.
 */

public class MeFragment extends BaseFragment implements View.OnClickListener, TranslucentScrollView.TranslucentChangedListener ,ActionBarClickListener{


    @BindView(R.id.user_avatar)
    RoundedCornerImageView userAvatar;
    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.renzheng)
    TextView renzheng;
    @BindView(R.id.ll_order)
    LinearLayout llOrder;
    @BindView(R.id.ll_account)
    LinearLayout llAccount;
    @BindView(R.id.ll_integral)
    LinearLayout llIntegral;
    @BindView(R.id.ll_coupon)
    LinearLayout llCoupon;
    @BindView(R.id.ll_help)
    LinearLayout llHelp;
    @BindView(R.id.ll_collect)
    LinearLayout llCollect;
    @BindView(R.id.landlord)
    TextView landlord;
    Unbinder unbinder;
    @BindView(R.id.img_setting)
    ImageView imgSetting;
    @BindView(R.id.lay_header)
    RelativeLayout layHeader;
    private View view;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.fragment_me, container, false);

        return view;
    }



    @Override
    protected void initListener() {
        llOrder.setOnClickListener(this);
        llAccount.setOnClickListener(this);
        llIntegral.setOnClickListener(this);
        imgSetting.setOnClickListener(this);
        llCoupon.setOnClickListener(this);
        llCollect.setOnClickListener(this);
        llHelp.setOnClickListener(this);
        userAvatar.setOnClickListener(this);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_order:
                startActivity(new Intent(getActivity(), OrderActivity.class));
                break;
            case R.id.ll_account:
                startActivity(new Intent(getActivity(), AccountActivity.class));
                break;
            case R.id.ll_integral:
                startActivity(new Intent(getActivity(), IntegralActivity.class));
                break;
            case R.id.ll_coupon:
                startActivity(new Intent(getActivity(), CouponActivity.class));
                break;
            case R.id.ll_collect:
                startActivity(new Intent(getActivity(), CollectActivity.class));
                break;
            case R.id.img_setting:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.ll_help:
                startActivity(new Intent(getActivity(), HelpActivity.class));
                break;
                case R.id.user_avatar:
                startActivity(new Intent(getActivity(), UserInfoActivity.class));
                break;
        }
    }

    @Override
    public void onTranslucentChanged(int transAlpha) {
    }

    @Override
    public void onLeftClick() {

    }

    @Override
    public void onRightClick() {

    }
}
