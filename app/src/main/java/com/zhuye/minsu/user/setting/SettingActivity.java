package com.zhuye.minsu.user.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhuye.minsu.R;
import com.zhuye.minsu.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingActivity extends BaseActivity implements View.OnClickListener {


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
    @BindView(R.id.tv_change_password)
    TextView tvChangePassword;
    @BindView(R.id.rl_change_password)
    RelativeLayout rlChangePassword;
    @BindView(R.id.set_tag1)
    View setTag1;
    @BindView(R.id.tv_function_feedback)
    TextView tvFunctionFeedback;
    @BindView(R.id.rl_function_feedback)
    RelativeLayout rlFunctionFeedback;
    @BindView(R.id.set_tag2)
    View setTag2;
    @BindView(R.id.tv_privacy_setting)
    TextView tvPrivacySetting;
    @BindView(R.id.rl_privacy_setting)
    RelativeLayout rlPrivacySetting;
    @BindView(R.id.set_tag3)
    View setTag3;
    @BindView(R.id.tv_about)
    TextView tvAbout;
    @BindView(R.id.rl_about)
    RelativeLayout rlAbout;
    @BindView(R.id.set_tag4)
    View setTag4;
    @BindView(R.id.logout)
    TextView logout;

    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {
        toolbarTitle.setText("设置");
        ivLeft.setVisibility(View.VISIBLE);
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        rlAbout.setOnClickListener(this);
        rlFunctionFeedback.setOnClickListener(this);
        rlPrivacySetting.setOnClickListener(this);
        logout.setOnClickListener(this);
        rlChangePassword.setOnClickListener(this);
        rlPrivacySetting.setOnClickListener(this);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_setting);
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_about:
                startActivity(new Intent(this, AboutUsActivity.class));
                break;
            case R.id.rl_function_feedback:
                startActivity(new Intent(this, FeedbackActivity.class));
                break;
        }
    }
}
