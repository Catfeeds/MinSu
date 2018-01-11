package com.zhuye.minsu.common;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lzy.okgo.model.Response;
import com.zhuye.minsu.R;
import com.zhuye.minsu.api.Constant;
import com.zhuye.minsu.api.DataProvider;
import com.zhuye.minsu.api.MinSuApi;
import com.zhuye.minsu.api.callback.CallBack;
import com.zhuye.minsu.base.BaseActivity;
import com.zhuye.minsu.common.adapter.BGABannerAdapter;
import com.zhuye.minsu.houseResource.adapter.FacilitiesAdapter;
import com.zhuye.minsu.houseResource.adapter.FacilitiesBean;
import com.zhuye.minsu.utils.StorageUtil;
import com.zhuye.minsu.utils.ToastManager;
import com.zhuye.minsu.widget.RoundedCornerImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.bgabanner.BGABanner;

public class RoomDetailActivity extends BaseActivity {

    @BindView(R.id.detail_room_name)
    TextView detailRoomName;
    @BindView(R.id.detail_room_description)
    TextView detailRoomDescription;
    @BindView(R.id.detail_room_address)
    TextView detailRoomAddress;
    @BindView(R.id.detail_room_collect)
    ImageView detailRoomCollect;
    @BindView(R.id.detail_room_comment_number)
    TextView detailRoomCommentNumber;
    @BindView(R.id.detail_room_price)
    TextView detailRoomPrice;
    @BindView(R.id.part_line)
    View partLine;
    @BindView(R.id.detail_user_img)
    RoundedCornerImageView detailUserImg;
    @BindView(R.id.detail_user_name)
    TextView detailUserName;
    @BindView(R.id.detail_shiming_renzheng)
    TextView detailShimingRenzheng;
    @BindView(R.id.detail_fangdong_xingzhi)
    TextView detailFangdongXingzhi;
    @BindView(R.id.detail_connect_online)
    TextView detailConnectOnline;
    @BindView(R.id.tag1)
    View tag1;
    @BindView(R.id.detail_connect_phone)
    TextView detailConnectPhone;
    @BindView(R.id.banner_pager)
    BGABanner bannerPager;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.ruzhu_time)
    TextView ruzhuTime;
    @BindView(R.id.leave_time)
    TextView leaveTime;
    @BindView(R.id.ll_tag)
    RelativeLayout llTag;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private String tokenId;
    private String house_id;
    private String h_mobile;
    private static final String TAG = "RoomDetailActivity";

    @Override
    protected void processLogic() {
        MinSuApi.houseDetail(this, 0x001, tokenId, house_id, callBack);
    }

    @Override
    protected void setListener() {

        tokenId = StorageUtil.getTokenId(this);

        house_id = getIntent().getStringExtra("house_id");
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        detailConnectPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call(h_mobile);
            }
        });
        recyclerView.setLayoutManager(new GridLayoutManager(this, 5));

    }

    private void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_room_detail);
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
                            JSONObject data = jsonObject.getJSONObject("data");
                            JSONObject jsonObject1 = new JSONObject(data.toString());
                            String title = jsonObject1.getString("title");
                            String house_info = jsonObject1.getString("house_info");
                            String house_price = jsonObject1.getString("house_price");
                            String province = jsonObject1.getString("province");
                            String city = jsonObject1.getString("city");
                            String district = jsonObject1.getString("district");
                            String town = jsonObject1.getString("town");
                            String h_name = jsonObject1.getString("h_name");
                            String status = jsonObject1.getString("status");
                            ArrayList<FacilitiesBean> sheshiList = new ArrayList<>();
                            String[] split = status.split(",");
                            for (String aSplit : split) {
                                sheshiList.add(DataProvider.getFacilitiesList().get(Integer.parseInt(aSplit) - 1));
                            }
                            Log.i(TAG, "onSuccess: " + sheshiList);
                            FacilitiesAdapter facilitiesAdapter = new FacilitiesAdapter(R.layout.item_facilities, sheshiList);
                            recyclerView.setAdapter(facilitiesAdapter);
                            JSONArray house_img = jsonObject1.getJSONArray("house_img");
                            List<String> bannerImage = new ArrayList<>();
                            for (int i = 0; i < house_img.length(); i++) {
                                bannerImage.add(Constant.BASE2_URL + house_img.get(i));
                            }
                            bannerPager.setAdapter(new BGABannerAdapter(RoomDetailActivity.this));
                            bannerPager.setData(bannerImage, null);
                            h_mobile = jsonObject1.getString("h_mobile");
                            String head_pic = jsonObject1.getString("head_pic");
                            String com_count = jsonObject1.getString("com_count");
                            int collect = jsonObject1.getInt("collect");
                            detailRoomName.setText(title);
                            detailRoomDescription.setText(house_info);
//                            detailConnectPhone.setText(h_mobile);
                            detailUserName.setText(h_name);
                            detailRoomCommentNumber.setText(com_count + "条评价");
                            detailRoomAddress.setText(province + " " + city + " " + district + " " + town);
                            detailRoomPrice.setText("￥" + house_price + "/天");
                            Glide.with(RoomDetailActivity.this)
                                    .load(Constant.BASE2_URL + head_pic)
                                    .into(detailUserImg);
                            if (collect == 1) {
                                //已收藏detailRoomCollect
                                Glide.with(RoomDetailActivity.this)
                                        .load(R.mipmap.collected)
                                        .into(detailRoomCollect);
                            } else if (collect == 0) {
                                //未收藏
                                Glide.with(RoomDetailActivity.this)
                                        .load(R.mipmap.collect)
                                        .into(detailRoomCollect);
                            }
                        } else if (code == 111) {
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
