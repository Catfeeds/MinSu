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

public class MyCouponActivity extends BaseActivity {


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
    private String money;

    @Override
    protected void processLogic() {
        MinSuApi.myCouponList(this, 0x001, tokenId, callBack);
    }

    @Override
    protected void setListener() {
        type = getIntent().getStringExtra("type");
        money = getIntent().getStringExtra("money");
        tokenId = StorageUtil.getTokenId(this);
        toolbarTitle.setText("我的优惠券");
        ivLeft.setVisibility(View.VISIBLE);
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_my_coupon);
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
                            for (int i=0;i<couponListBean.data.size();i++)
                            {
                                couponListBean.data.get(i).isview=1;
                            }
                            final CouponListAdapter couponListAdapter = new CouponListAdapter(R.layout.item_coupon, couponListBean.data, "person");
                            if (couponListBean.data.size() == 0) {
                                couponListAdapter.setEmptyView(R.layout.empty, recyclerView);
                            }
                            recyclerView.setAdapter(couponListAdapter);
                            couponListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                    if (type==null)
                                    {
                                        return;
                                    }
                                    if (type.equals("order")) {
                                        int price = couponListAdapter.getItem(position).price;
                                        float floatMoney = Float.parseFloat(money);
                                        float floatPrice = (float) price;
                                        if (couponListAdapter.getItem(position).is_type == 1) {
                                            ToastManager.show("优惠券已过期");
                                            return;
                                        }
                                        if (floatPrice > floatMoney) {
                                            ToastManager.show("优惠金额大于需支付金额");
                                            return;
                                        }
                                        Intent intent = new Intent();
                                        //把返回数据存入Intent
                                        intent.putExtra("discount_amount", couponListAdapter.getItem(position).price + "");
                                        intent.putExtra("coupon_id", couponListAdapter.getItem(position).quan_id + "");
                                        MyCouponActivity.this.setResult(RESULT_OK, intent);
                                        //关闭Activity
                                        MyCouponActivity.this.finish();
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
