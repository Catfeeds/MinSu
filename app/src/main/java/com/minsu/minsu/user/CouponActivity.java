package com.minsu.minsu.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.lzy.okgo.model.Response;
import com.minsu.minsu.R;
import com.minsu.minsu.api.MinSuApi;
import com.minsu.minsu.api.callback.CallBack;
import com.minsu.minsu.base.BaseActivity;
import com.minsu.minsu.user.adapter.CouponListAdapter;
import com.minsu.minsu.user.bean.CouponListBean;
import com.minsu.minsu.utils.StorageUtil;
import com.minsu.minsu.utils.ToastManager;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CouponActivity extends BaseActivity {


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
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private String tokenId;
    private String type;

    @Override
    protected void processLogic() {
        MinSuApi.CouponList(this, 0x001, tokenId, callBack);
    }

    @Override
    protected void setListener() {

        type = getIntent().getStringExtra("type");
        tokenId = StorageUtil.getTokenId(this);
        toolbarTitle.setText("优惠券");
        ivLeft.setVisibility(View.VISIBLE);
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("我的");
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CouponActivity.this,MyCouponActivity.class));
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_coupon);
    }

    @Override
    protected Context getActivityContext() {
        return this;
    }

    private CallBack callBack = new CallBack() {
        @Override
        public void onSuccess(int what, Response<String> result) {
            switch (what) {
                case 0x001:
                    try {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        String msg = jsonObject.getString("msg");
                        if (code == 200) {
                            CouponListBean couponListBean = new Gson().fromJson(result.body(), CouponListBean.class);
                            final CouponListAdapter couponListAdapter = new CouponListAdapter(R.layout.item_coupon, couponListBean.data,"mo_person");
                            if (couponListBean.data.size() == 0) {
                                couponListAdapter.setEmptyView(R.layout.empty, recyclerView);
                            }
                            recyclerView.setAdapter(couponListAdapter);
                            couponListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                                @Override
                                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                                    switch (view.getId()) {
                                        case R.id.coupon_status:
                                            MinSuApi.getCoupon(0x002, tokenId, couponListAdapter.getItem(position).quan_id, callBack);
                                            break;
                                    }
                                }
                            });
                            couponListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                    if (type.equals("order")){
                                        Intent intent = new Intent();
                                        //把返回数据存入Intent
                                        intent.putExtra("discount_amount", couponListAdapter.getItem(position).price + "");
                                        intent.putExtra("coupon_id", couponListAdapter.getItem(position).quan_id + "");
                                        CouponActivity.this.setResult(RESULT_OK, intent);
                                        //关闭Activity
                                        CouponActivity.this.finish();
                                    }
                                }
                            });
                        } else if (code == 111) {
                            ToastManager.show(msg);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 0x002:
                    try {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        String msg = jsonObject.getString("msg");
                        if (code == 200) {
                            ToastManager.show(msg);
                            MinSuApi.CouponList(CouponActivity.this, 0x001, tokenId, callBack);
                        } else if (code == 211) {
                            ToastManager.show(msg);
                        } else if (code == 201) {
                            ToastManager.show(msg);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
