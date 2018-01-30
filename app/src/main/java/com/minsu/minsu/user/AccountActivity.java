package com.minsu.minsu.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.minsu.minsu.R;
import com.minsu.minsu.base.BaseActivity;
import com.minsu.minsu.utils.StorageUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    @BindView(R.id.rl_wallet)
    RelativeLayout rlWallet;

    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {
        toolbarTitle.setText("账户");
        ivLeft.setVisibility(View.VISIBLE);
        String role = StorageUtil.getValue(this, "role");
        if (role.equals("landlord")) {
            rlWallet.setVisibility(View.VISIBLE);
        } else if (role.equals("user")) {
            rlWallet.setVisibility(View.GONE);
        }
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        rlUserData.setOnClickListener(this);
        rlAddress.setOnClickListener(this);
        rlWallet.setOnClickListener(this);
        rlVisitor.setOnClickListener(this);
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
                startActivity(new Intent(AccountActivity.this, PassengerListActivity.class));
                break;
            case R.id.rl_wallet:
                startActivity(new Intent(AccountActivity.this, WalletActivity.class));
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
