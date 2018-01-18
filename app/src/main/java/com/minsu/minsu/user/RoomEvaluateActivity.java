package com.minsu.minsu.user;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.minsu.minsu.R;
import com.minsu.minsu.base.BaseActivity;
import com.minsu.minsu.utils.StorageUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RoomEvaluateActivity extends BaseActivity {


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
    @BindView(R.id.content)
    EditText content;
    private String tokenId;
    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {
        tokenId = StorageUtil.getTokenId(this);
        toolbarTitle.setText("订单评价");
        ivLeft.setVisibility(View.VISIBLE);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("提交");
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_room_evaluate);
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
}
