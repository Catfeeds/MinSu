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

import com.bumptech.glide.Glide;
import com.lzy.okgo.model.Response;
import com.zhuye.minsu.R;
import com.zhuye.minsu.api.Constant;
import com.zhuye.minsu.api.MinSuApi;
import com.zhuye.minsu.api.callback.CallBack;
import com.zhuye.minsu.base.BaseFragment;
import com.zhuye.minsu.houseResource.HouseResourceActivity;
import com.zhuye.minsu.user.AccountActivity;
import com.zhuye.minsu.user.CollectActivity;
import com.zhuye.minsu.user.CouponActivity;
import com.zhuye.minsu.user.HelpActivity;
import com.zhuye.minsu.user.IntegralActivity;
import com.zhuye.minsu.user.OrderActivity;
import com.zhuye.minsu.user.UserInfoActivity;
import com.zhuye.minsu.user.setting.SettingActivity;
import com.zhuye.minsu.utils.StorageUtil;
import com.zhuye.minsu.widget.ActionBarClickListener;
import com.zhuye.minsu.widget.RoundedCornerImageView;
import com.zhuye.minsu.widget.TranslucentScrollView;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by hpc on 2017/12/1.
 */

public class MeFragment extends BaseFragment implements View.OnClickListener, TranslucentScrollView.TranslucentChangedListener, ActionBarClickListener {


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
    @BindView(R.id.ll_house_resource)
    LinearLayout llHouseResource;
    private View view;
    private String token;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.fragment_me, container, false);

        return view;
    }


    @Override
    protected void initListener() {

        token = StorageUtil.getTokenId(getActivity());
        llOrder.setOnClickListener(this);
        llAccount.setOnClickListener(this);
        llIntegral.setOnClickListener(this);
        imgSetting.setOnClickListener(this);
        llCoupon.setOnClickListener(this);
        llCollect.setOnClickListener(this);
        llHelp.setOnClickListener(this);
        userAvatar.setOnClickListener(this);
        llHouseResource.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        MinSuApi.userCenter(getActivity(), 0x001, token, callBack);
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

    private CallBack callBack = new CallBack() {
        @Override
        public void onSuccess(int what, Response<String> result) {
            switch (what) {
                case 0x001:
                    try {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        if (code == 200) {
                            JSONObject data = jsonObject.getJSONObject("data");
                            JSONObject userData = new JSONObject(data.toString());
                            String nickname = userData.getString("nickname");
                            String head_pic = userData.getString("head_pic");
                            int is_name = userData.getInt("is_name");
                            int is_house = userData.getInt("is_house");
                            int user_id = userData.getInt("user_id");
                            username.setText(nickname);
                            if (is_name == 1) {
                                renzheng.setText("已实名认证");
                            } else if (is_name == 2) {
                                renzheng.setText("审核中...");
                            }
                            Glide.with(getActivity())
                                    .load(Constant.BASE2_URL + head_pic)
//                                    .placeholder(R.mipmap.ic_launcher)
                                    .into(userAvatar);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }

        @Override
        public void onFail(int what, Response<String> result) {

        }

        @Override
        public void onFinish(int what) {

        }
    };

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
                case R.id.ll_house_resource:
                startActivity(new Intent(getActivity(), HouseResourceActivity.class));
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

    @Override
    public void onResume() {
        super.onResume();
//        MinSuApi.userCenter(getActivity(), 0x001, token, callBack);
    }
}
