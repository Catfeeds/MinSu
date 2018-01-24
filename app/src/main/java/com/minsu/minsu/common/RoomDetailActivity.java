package com.minsu.minsu.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lzy.okgo.model.Response;
import com.minsu.minsu.R;
import com.minsu.minsu.api.Constant;
import com.minsu.minsu.api.DataProvider;
import com.minsu.minsu.api.MinSuApi;
import com.minsu.minsu.api.callback.CallBack;
import com.minsu.minsu.base.BaseActivity;
import com.minsu.minsu.common.adapter.BGABannerAdapter;
import com.minsu.minsu.houseResource.adapter.FacilitiesAdapter;
import com.minsu.minsu.houseResource.adapter.FacilitiesBean;
import com.minsu.minsu.utils.StorageUtil;
import com.minsu.minsu.utils.ToastManager;
import com.minsu.minsu.widget.RoundedCornerImageView;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    @BindView(R.id.quick_reserve)
    TextView quickReserve;
    @BindView(R.id.calendarView)
    MaterialCalendarView calendarView;
    @BindView(R.id.click_ruzhu_time)
    TextView clickRuzhuTime;
    @BindView(R.id.click_leave_time)
    TextView clickLeaveTime;
    @BindView(R.id.totalDay)
    TextView totalDay;
    private String tokenId;
    private String house_id;
    private String h_mobile;
    private static final String TAG = "RoomDetailActivity";
    private int user_id;
    private List<CalendarDay> selectedDates;
    private int days;
    private String house_price;

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
        detailConnectOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MinSuApi.addChatList(RoomDetailActivity.this, 0x003, tokenId, user_id, callBack);
            }
        });
        recyclerView.setLayoutManager(new GridLayoutManager(this, 5));

        quickReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String ruzhu_time = ruzhuTime.getText().toString();
                final String leave_time = leaveTime.getText().toString();
                getTotalDays(ruzhu_time, leave_time);
                if (ruzhu_time.equals("")) {
                    ToastManager.show("入住时间不能为空");
                    return;
                }
                if (leave_time.equals("")) {
                    ToastManager.show("离开时间不能为空");
                    return;
                }
                if (days == 0) {
                    return;
                }
                if (house_price.equals("")) {
                    return;
                }
                MinSuApi.orderSubmit(RoomDetailActivity.this, 0x002, tokenId, Integer.parseInt(house_id), ruzhu_time, leave_time, days, house_price, callBack);


            }
        });

//        checkDate();
        clickRuzhuTime.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(RoomDetailActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        ToastManager.show("您选择了：" + year + "年" + monthOfYear
                                + "月" + dayOfMonth + "日");
                        ruzhuTime.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        String ruzhu_time = ruzhuTime.getText().toString();
                        String leave_time = leaveTime.getText().toString();
                        getTotalDays(ruzhu_time, leave_time);
                    }

                });
                datePickerDialog.show();
            }
        });
        clickLeaveTime.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(RoomDetailActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        ToastManager.show("您选择了：" + year + "年" + monthOfYear
                                + "月" + dayOfMonth + "日");
                        leaveTime.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        String ruzhu_time = ruzhuTime.getText().toString();
                        String leave_time = leaveTime.getText().toString();
                        getTotalDays(ruzhu_time, leave_time);
                    }

                });
                datePickerDialog.show();
            }
        });

        //查看评价
        detailRoomCommentNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void getTotalDays(String ruzhu_time, String leave_time) {
        if (ruzhu_time.equals("") || leave_time.equals("")) {
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d1 = sdf.parse(ruzhu_time);
            Date d2 = sdf.parse(leave_time);

            days = daysBetween(d1, d2);
            Log.i(TAG, "onClick: " + days);
            totalDay.setText("共" + days + "晚");

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public int daysBetween(Date smdate, Date bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        smdate = sdf.parse(sdf.format(smdate));
        bdate = sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    public class MySelectorDecorator implements DayViewDecorator {

        private final Drawable drawable;

        public MySelectorDecorator(Activity context) {
            drawable = context.getResources().getDrawable(R.mipmap.sign_2);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            if (selectedDates.size() >= 2) {
                CalendarDay day1 = selectedDates.get(selectedDates.size() - 1);
                CalendarDay day2 = selectedDates.get(selectedDates.size() - 2);
                ArrayList<CalendarDay> calendarDays = new ArrayList<>();
                calendarDays.add(day1);
                calendarDays.add(day2);
                return calendarDays.contains(day.getDate());
            } else {
                return false;
            }
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.setSelectionDrawable(drawable);
        }
    }

    private void checkDate() {
        calendarView.setSelectionMode(2);
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                Log.i(TAG, "onRangeSelected: " + date);

                selectedDates = calendarView.getSelectedDates();
                Log.i(TAG, "checkDate: " + selectedDates);
                calendarView.addDecorator(new MySelectorDecorator(RoomDetailActivity.this));
            }
        });


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

                            house_price = jsonObject1.getString("house_price");
                            String province = jsonObject1.getString("province");
                            String city = jsonObject1.getString("city");
                            String district = jsonObject1.getString("district");
                            String town = jsonObject1.getString("town");
                            String h_name = jsonObject1.getString("h_name");
                            String status = jsonObject1.getString("status");
                            user_id = jsonObject1.getInt("user_id");
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
                case 0x002:
                    try {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        String msg = jsonObject.getString("msg");
                        if (code == 200) {
                            ToastManager.show(msg);
                            String order_id = jsonObject.getString("data");
                            Intent intent = new Intent(RoomDetailActivity.this, OrderSubmitActivity.class);
                            intent.putExtra("order_id", order_id + "");
                            startActivity(intent);
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
                        String msg = jsonObject.getString("msg");
                        if (code == 200) {
                            ToastManager.show(msg);
                            //todo
                            // 进入聊天界面
                        } else if (code == 211) {
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
