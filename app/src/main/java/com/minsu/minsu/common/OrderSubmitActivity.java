package com.minsu.minsu.common;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lzy.okgo.model.Response;
import com.minsu.minsu.R;
import com.minsu.minsu.api.Constant;
import com.minsu.minsu.api.MinSuApi;
import com.minsu.minsu.api.callback.CallBack;
import com.minsu.minsu.base.BaseActivity;
import com.minsu.minsu.utils.StorageUtil;
import com.minsu.minsu.utils.ToastManager;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;

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
    private String tokenId;
    private String order_id;

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
                            String title = jsonObject1.getString("title");
                            String house_info = jsonObject1.getString("house_info");
                            String province = jsonObject1.getString("province");
                            String city = jsonObject1.getString("city");
                            String district = jsonObject1.getString("district");
                            String town = jsonObject1.getString("town");
                            String nickname = jsonObject1.getString("nickname");
                            String mobile = jsonObject1.getString("mobile");
                            int is_name = jsonObject1.getInt("is_name");
                            landlord.setText(nickname);
                            phone.setText(mobile);
                            orderRoomTitle.setText(title);
                            orderRoomDescription.setText(house_info);
                            ruzhuTime.setText("入住:" + check_time);
                            leaveTime.setText("离开:" + leave_time);
                            totalDay.setText("共" + days + "天");
                            locationAddress.setText(city + " " + district + " " + town);
                            if (is_name == 2) {
                                renzhengStatus.setText("审核中");
                            } else if (is_name == 1) {
                                renzhengStatus.setText("认证通过");
                            } else if (is_name == 0) {
                                renzhengStatus.setText("没有认证");
                            }
                            Glide.with(OrderSubmitActivity.this)
                                    .load(Constant.BASE2_URL + house_img)
                                    .into(orderRoomImg);
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
                        if (code==200){
                            ToastManager.show(msg);
                        }else if (code==211){
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.quick_pay:
                MinSuApi.changeOrderStatus(OrderSubmitActivity.this, 0x002, tokenId, order_id, "", 0, callBack);
                break;
        }
    }
}
