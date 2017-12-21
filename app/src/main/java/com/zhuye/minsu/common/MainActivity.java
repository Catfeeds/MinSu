package com.zhuye.minsu.common;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.zhuye.minsu.R;
import com.zhuye.minsu.base.BaseActivity;
import com.zhuye.minsu.base.BaseNoActivity;
import com.zhuye.minsu.common.fragment.FragmentController;
import com.zhuye.minsu.utils.StatusBarUtils;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener{

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

    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {
        hometabRadio = (RadioGroup) findViewById(R.id.hometab_radio);
        hometabRadio.setOnCheckedChangeListener(this);
        rbOrder.setVisibility(View.GONE);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_main);
//        StatusBarUtils.setColor(this,R.color.colorPrimary);
        controller = FragmentController.getInstance(this, R.id.frame_layout);
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
//                toolbarTitle.setText("动态");
                controller.showFragment(0);
                break;
            case R.id.rb_find:
                controller.showFragment(1);
                break;
            case R.id.rb_message:
                controller.showFragment(2);
                break;
            case R.id.rb_order:
                controller.showFragment(3);
                break;
            case R.id.rb_me:
                controller.showFragment(4);
                break;
            default:
                break;
        }
    }
}
