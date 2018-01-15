package com.minsu.minsu.user;

import android.content.Context;
import android.os.Bundle;

import com.minsu.minsu.R;
import com.minsu.minsu.base.BaseActivity;
import com.minsu.minsu.widget.ActionBarClickListener;
import com.minsu.minsu.widget.TranslucentActionBar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AuthenticationActivity extends BaseActivity implements ActionBarClickListener{


    @BindView(R.id.actionbar)
    TranslucentActionBar actionbar;

    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {
        actionbar.setData("认证", 0, null, 0, null, null);
        //开启渐变
        actionbar.setNeedTranslucent();
        //设置状态栏高度
        actionbar.setStatusBarHeight(getStatusBarHeight());

    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_authentication);
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
    public void onLeftClick() {

    }

    @Override
    public void onRightClick() {

    }
}
