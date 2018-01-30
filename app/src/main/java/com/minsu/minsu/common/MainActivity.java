package com.minsu.minsu.common;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.minsu.minsu.R;
import com.minsu.minsu.base.BaseActivity;
import com.minsu.minsu.common.fragment.FragmentController;
import com.minsu.minsu.common.fragment.MeFragment;
import com.minsu.minsu.utils.StorageUtil;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, MeFragment.RoleInterface {

    private static final String TAG_PAGE_HOME = "首页";
    private static final String TAG_PAGE_FIND = "发现";
    private static final String TAG_PAGE_MESSAGE = "消息";
    private static final String TAG_PAGE_ORDER = "订单";
    private static final String TAG_PAGE_USER = "我的";
    @BindView(R.id.frame_layout)
    FrameLayout frameLayout;
    @BindView(R.id.rb_home)
    RadioButton rbHome;
    @BindView(R.id.rb_find)
    RadioButton rbFind;
    @BindView(R.id.rb_message)
    RadioButton rbMessage;
    @BindView(R.id.rb_order)
    RadioButton rbOrder;
    @BindView(R.id.rb_me)
    RadioButton rbMe;
    @BindView(R.id.hometab_radio)
    RadioGroup hometabRadio;
    private FragmentController controller;
    private int localRole;

    private static final String TAG = "MainActivity";
    private String token;

    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {
        token = StorageUtil.getTokenId(this);
        hometabRadio = (RadioGroup) findViewById(R.id.hometab_radio);
        hometabRadio.setOnCheckedChangeListener(this);
        rbOrder.setVisibility(View.GONE);
//        rbHome.setVisibility(View.GONE);
    }


    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_main);
        controller = FragmentController.getInstance(MainActivity.this, R.id.frame_layout);
        controller.showFragment(0);
    }

    @Override
    protected Context getActivityContext() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        switch (checkedId) {
            case R.id.rb_home:
                controller.showFragment(0);
                break;
            case R.id.rb_order:
                controller.showFragment(1);
                break;
            case R.id.rb_find:
                controller.showFragment(2);
                break;
            case R.id.rb_message:
                controller.showFragment(3);
                break;
            case R.id.rb_me:
                controller.showFragment(4);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void changeRole(int role) {
        localRole = role;
        if (localRole == 1) {
            rbHome.setVisibility(View.GONE);
            rbOrder.setVisibility(View.VISIBLE);
        } else if (localRole == 2) {
            rbHome.setVisibility(View.VISIBLE);
            rbOrder.setVisibility(View.GONE);
        }
    }
}
