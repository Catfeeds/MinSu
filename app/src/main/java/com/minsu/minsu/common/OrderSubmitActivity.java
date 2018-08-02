package com.minsu.minsu.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lzy.okgo.model.Response;
import com.minsu.minsu.R;
import com.minsu.minsu.api.Constant;
import com.minsu.minsu.api.MinSuApi;
import com.minsu.minsu.api.callback.CallBack;
import com.minsu.minsu.base.BaseActivity;
import com.minsu.minsu.user.CouponActivity;
import com.minsu.minsu.user.MyCouponActivity;
import com.minsu.minsu.user.bean.CouponListBean;
import com.minsu.minsu.utils.StorageUtil;
import com.minsu.minsu.utils.ToastManager;
import com.minsu.minsu.weixin.WXPay;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderSubmitActivity extends BaseActivity implements View.OnClickListener {

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
    @BindView(R.id.landlord)
    TextView landlord;
    @BindView(R.id.renzheng_status)
    TextView renzhengStatus;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.order_roomImg)
    ImageView orderRoomImg;
    @BindView(R.id.order_room_title)
    TextView orderRoomTitle;
    @BindView(R.id.order_room_description)
    TextView orderRoomDescription;
    @BindView(R.id.ruzhu_time)
    TextView ruzhuTime;
    @BindView(R.id.leave_time)
    TextView leaveTime;
    @BindView(R.id.location_address)
    TextView locationAddress;
    @BindView(R.id.total_day)
    TextView totalDay;
    @BindView(R.id.tag01)
    TextView tag01;
    @BindView(R.id.tag03)
    ImageView tag03;
    @BindView(R.id.ll_aliPay)
    RelativeLayout llAliPay;
    @BindView(R.id.tag04)
    ImageView tag04;
    @BindView(R.id.ll_weixinPay)
    RelativeLayout llWeixinPay;
    @BindView(R.id.quick_pay)
    TextView quickPay;
    @BindView(R.id.tv_coupon)
    TextView tvCoupon;
    @BindView(R.id.rl_coupon)
    RelativeLayout rlCoupon;
    @BindView(R.id.total_money)
    TextView totalMoney;
    @BindView(R.id.rb_ali)
    RadioButton rbAli;
    @BindView(R.id.rb_wechat)
    RadioButton rbWechat;
    private String tokenId;
    private String order_id;
    private boolean byAli;
    private static final int REQUEST_CODE = 0x001;
    private String discount_amount;
    private String coupon_id;
    private String total_price;
    private static final int SDK_PAY_FLAG = 1;
    private int price=0;
    private int quan_id;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
//                    PayResult payResult = new PayResult((String) msg.obj);
                            PayResult payResult = new PayResult((Map<String, String>) msg.obj);

                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    try {
                        JSONObject jsonObject = new JSONObject(resultInfo);
                        String app_pay_response = jsonObject.getString("alipay_trade_app_pay_response");
                        JSONObject payData = new JSONObject(app_pay_response);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(OrderSubmitActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        StorageUtil.setKeyValue(OrderSubmitActivity.this,"issuccess","yes");
                        if (coupon_id == null) {
                            MinSuApi.changeOrderStatus(0x002, tokenId, order_id, "", callBack);
                        } else {

                            //需要优惠券id
                            MinSuApi.changeOrderStatus(0x002, tokenId, order_id, coupon_id, callBack);
                        }
//                        Intent intent = new Intent(OrderSubmitActivity.this, MainActivity.class);
//                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(OrderSubmitActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                        //                 该笔订单真实的支付结果，需要依赖服务端的异步通知。
//                        Intent intent = new Intent(PayActivity.this, PayFailActivity.class);
//                        startActivity(intent);
//                        finish();
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    @Override
    protected void processLogic() {
        MinSuApi.orderPayPage(this, 0x001, tokenId, order_id, callBack);
    }

    @Override
    protected void setListener() {
        tokenId = StorageUtil.getTokenId(this);
        toolbarTitle.setText("确认支付");
        ivLeft.setVisibility(View.VISIBLE);
        order_id = getIntent().getStringExtra("order_id");
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        quickPay.setOnClickListener(this);
        llAliPay.setOnClickListener(this);
        llWeixinPay.setOnClickListener(this);
        rlCoupon.setOnClickListener(this);
        rbAli.setClickable(false);
        rbWechat.setClickable(false);
        rbAli.setChecked(true);
        rbWechat.setChecked(false);
        byAli = true;
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_order_submit);
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
                        if (code == 200) {
                            JSONObject data = jsonObject.getJSONObject("data");
                            JSONObject jsonObject1 = new JSONObject(data.toString());
                            String check_time = jsonObject1.getString("check_time");
                            String leave_time = jsonObject1.getString("leave_time");
                            int days = jsonObject1.getInt("days");
                            String house_img = jsonObject1.getString("house_img");

                            total_price = jsonObject1.getString("total_price");
                            String title = jsonObject1.getString("title");
                            String house_info = jsonObject1.getString("house_info");
                            String province = jsonObject1.getString("province");
                            String city = jsonObject1.getString("city");
                            String district = jsonObject1.getString("district");
                            String town = jsonObject1.getString("town");
                            String nickname = jsonObject1.getString("nickname");
                            String mobile = jsonObject1.getString("mobile");
                            int is_name = jsonObject1.getInt("is_name");
                            totalMoney.setText("￥" + total_price);
                            landlord.setText(nickname);
                            phone.setText(mobile);
                            orderRoomTitle.setText(title);
                            orderRoomDescription.setText(house_info);
                            ruzhuTime.setText("入住:" + check_time);
                            leaveTime.setText("离开:" + leave_time);
                            totalDay.setText("共" + days + "天");
                            locationAddress.setText(city + " " + district + " " + town);
                            if (is_name == 2) {
                                renzhengStatus.setText("实名认证审核中...");
                            } else if (is_name == 1) {
                                renzhengStatus.setText("实名认证通过");
                            } else if (is_name == 0) {
                                renzhengStatus.setText("没有认证");
                            }
                            Glide.with(OrderSubmitActivity.this)
                                    .load(Constant.BASE2_URL + house_img)
                                    .into(orderRoomImg);
                            MinSuApi.myCoupon(OrderSubmitActivity.this, 0x005, tokenId, callBack);
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
                            StorageUtil.setKeyValue(OrderSubmitActivity.this,"issuccess","yes");
                        } else if (code == 211) {
                            ToastManager.show(msg);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 0x003:
                    try {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        if (code == 200) {
                            JSONObject data = jsonObject.getJSONObject("data");
                            doWXPay(data);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 0x004:
                    try {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        if (code == 2101003) {
                            JSONObject data = jsonObject.getJSONObject("data");
                            final String payinfo = data.getString("payinfo");
                            Runnable payRunnable = new Runnable() {
                                @Override
                                public void run() {
                                    PayTask alipay = new PayTask(OrderSubmitActivity.this);
//                                String result = alipay.pay(payinfo, true);
                                    Map<String, String> result = alipay.payV2(payinfo, true);
                                    Log.i("msp", result.toString());
                                    Message msg = new Message();
                                    msg.what = SDK_PAY_FLAG;
                                    msg.obj = result;
                                    mHandler.sendMessage(msg);
                                }
                            };
                            Thread payThread = new Thread(payRunnable);
                            payThread.start();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 0x005:
                    JSONObject jsonObject = null;
                    try
                    {
                        jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        String msg = jsonObject.getString("msg");
                        if (code == 200)
                        {
                            CouponListBean couponListBean = new Gson().fromJson(result.body(), CouponListBean.class);
                            for (int i=0;i<couponListBean.data.size();i++)
                            {
                                if (couponListBean.data.get(i).is_type==0)
                                {
                                    int a=couponListBean.data.get(i).price;
                                    if (a>price)
                                    {
                                        quan_id=couponListBean.data.get(i).quan_id;
                                        price=a;
                                    }
                                }
                            }
                            if (price==0)
                            {
                                discount_amount=null;
                                coupon_id=null;
                                return;
                            }else {
                                discount_amount=price+"";
                                coupon_id=quan_id+"";
                            }
                            Double obj1 = new Double(total_price);
                            Double obj2= new Double(price);
                            if (obj1<=obj2)
                            {
                                totalMoney.setText("￥"+(obj1)+"");
                                tvCoupon.setVisibility(View.GONE);
                                discount_amount=null;
                                coupon_id=null;
                            }else {
                                totalMoney.setText("￥"+(obj1-obj2)+"");
                                tvCoupon.setText("￥"+discount_amount);
                                tvCoupon.setVisibility(View.VISIBLE);

                            }



                        }
                    } catch (JSONException e)
                    {
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

    private void doWXPay(JSONObject pay_param) {
        WXPay.init(this, Constant.WX_APP_ID);
        WXPay.getInstance().doPay(pay_param, new WXPay.WXPayResultCallBack() {
            @Override
            public void onSuccess() {
                Toast.makeText(getApplication(), "支付成功", Toast.LENGTH_SHORT).show();
                if (coupon_id == null) {
                    MinSuApi.changeOrderStatus(0x002, tokenId, order_id, "", callBack);
                } else {

                    //需要优惠券id
                    MinSuApi.changeOrderStatus(0x002, tokenId, order_id, coupon_id, callBack);
                }

//                Intent intent = new Intent(OrderSubmitActivity.this, MainActivity.class);
//                startActivity(intent);
                finish();
            }

            @Override
            public void onError(int error_code) {
                switch (error_code) {
                    case WXPay.NO_OR_LOW_WX:
                        Toast.makeText(getApplication(), "未安装微信或微信版本过低", Toast.LENGTH_SHORT).show();
                        break;

                    case WXPay.ERROR_PAY_PARAM:
                        Toast.makeText(getApplication(), "参数错误", Toast.LENGTH_SHORT).show();
                        break;

                    case WXPay.ERROR_PAY:
                        Toast.makeText(getApplication(), "支付失败", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplication(), "支付取消", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(PayActivity.this, PayFailActivity.class);
//                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.quick_pay:
                total_price=totalMoney.getText().toString();
                 total_price=total_price.substring(1,total_price.length());
                if (total_price.equals("0")||total_price.equals("0.0")||total_price.equals("0.00")||total_price.equals("00"))
                {
                    ToastManager.show("支付金额必须大于0元");
                    return;
                }
                //支付操作
                if (byAli) {
                   // ToastManager.show("阿里支付");
                    if (discount_amount == null) {
                        MinSuApi.aliPay(OrderSubmitActivity.this, 0x004, tokenId, order_id, 0, callBack);
                    } else {
                        MinSuApi.aliPay(OrderSubmitActivity.this, 0x004, tokenId, order_id, Integer.parseInt(discount_amount), callBack);
                    }
                } else {
                   // ToastManager.show("微信支付");
                    if (discount_amount == null) {
                        MinSuApi.weixinPay(OrderSubmitActivity.this, 0x003, tokenId, order_id, 0, callBack);
                    } else {
                        MinSuApi.weixinPay(OrderSubmitActivity.this, 0x003, tokenId, order_id, Integer.parseInt(discount_amount), callBack);
                    }
                }


                break;
            case R.id.ll_aliPay:
                rbAli.setChecked(true);
                rbWechat.setChecked(false);
                byAli = true;
                break;
            case R.id.ll_weixinPay:
                rbAli.setChecked(false);
                rbWechat.setChecked(true);
                byAli = false;
                break;
            case R.id.rl_coupon:
                Intent intent = new Intent(OrderSubmitActivity.this, MyCouponActivity.class);
                intent.putExtra("type", "order");
                intent.putExtra("money", total_price);
                startActivityForResult(intent, REQUEST_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && REQUEST_CODE == requestCode) {
            discount_amount = data.getExtras().getString("discount_amount");
            coupon_id = data.getExtras().getString("coupon_id");
            tvCoupon.setText("￥" + discount_amount);
            Double obj1 = new Double(total_price);
            Double obj2 = new Double(discount_amount);
//            int retval =  obj1.compareTo(obj2);
            totalMoney.setText("￥" + (obj1 - obj2));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
