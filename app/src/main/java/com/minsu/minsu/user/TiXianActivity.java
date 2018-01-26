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
import com.minsu.minsu.api.MinSuApi;
import com.minsu.minsu.base.BaseActivity;
import com.minsu.minsu.utils.StorageUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TiXianActivity extends BaseActivity implements View.OnClickListener {


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
    @BindView(R.id.tag)
    TextView tag;
    @BindView(R.id.tag1)
    TextView tag1;
    @BindView(R.id.ketixian)
    TextView ketixian;
    @BindView(R.id.tixian_all)
    TextView tixianAll;
    @BindView(R.id.ll_bankcard)
    RelativeLayout llBankcard;
    @BindView(R.id.confirm_tixian)
    TextView confirmTixian;
    private  String tokenId;

    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {

        tokenId = StorageUtil.getTokenId(this);
        toolbarTitle.setText("提现");
        ivLeft.setVisibility(View.VISIBLE);
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tixianAll.setOnClickListener(this);
        llBankcard.setOnClickListener(this);
        confirmTixian.setOnClickListener(this);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_ti_xian);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tixian_all:

                break;
            case R.id.ll_bankcard:
                startActivity(new Intent(TiXianActivity.this, BankCardListActivity.class));
                break;
                case R.id.confirm_tixian:
//                    MinSuApi.tixianApply(TiXianActivity.this,0x001,tokenId,);
                break;
        }
    }
}
