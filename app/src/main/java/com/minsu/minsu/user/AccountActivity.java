package com.minsu.minsu.user;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.minsu.minsu.R;
import com.minsu.minsu.base.BaseActivity;

import butterknife.BindView;

public class AccountActivity extends BaseActivity implements View.OnClickListener {


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
    @BindView(R.id.rl_user_data)
    RelativeLayout rlUserData;
    @BindView(R.id.rl_address)
    RelativeLayout rlAddress;
    @BindView(R.id.rl_visitor)
    RelativeLayout rlVisitor;

    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {
        toolbarTitle.setText("账户");
        ivLeft.setVisibility(View.VISIBLE);
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        rlUserData.setOnClickListener(this);
        rlAddress.setOnClickListener(this);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_account);
    }

    @Override
    protected Context getActivityContext() {
        return this;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_user_data:
                startActivity(new Intent(AccountActivity.this, UserInfoActivity.class));
                break;
            case R.id.rl_address:
                startActivity(new Intent(this, AddressActivity.class));
                break;
            case R.id.rl_visitor:

                break;
        }
    }
}
