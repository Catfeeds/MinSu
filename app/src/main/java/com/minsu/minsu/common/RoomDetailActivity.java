

package com.minsu.minsu.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lzy.okgo.model.Response;
import com.minsu.minsu.App;
import com.minsu.minsu.R;
import com.minsu.minsu.api.Constant;
import com.minsu.minsu.api.DataProvider;
import com.minsu.minsu.api.MinSuApi;
import com.minsu.minsu.api.callback.CallBack;
import com.minsu.minsu.base.BaseActivity;
import com.minsu.minsu.common.adapter.BGABannerAdapter;
import com.minsu.minsu.common.lanuch.Textspan;
import com.minsu.minsu.houseResource.XiuGaiHouseActivity;
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
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.bgabanner.BGABanner;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;

public class RoomDetailActivity extends BaseActivity
{

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
    //   @BindView(R.id.calendarView)
    MaterialCalendarView calendarView;
    @BindView(R.id.totalDay)
    TextView totalDay;
    private String tokenId;
    private String house_id;
    private String h_mobile;
    private static final String TAG = "RoomDetailActivity";
    private int user_id;
    private List<CalendarDay> selectedDates;
    private int days = 0;
    private String house_price;
    private Calendar cal;
    private int collect;
    private String order_id;
    private String typeq;
    private String data = "";
    private BGABannerAdapter adapter;
    private String nickname;
    private String head_pic;

    @Override
    protected void processLogic()
    {
        MinSuApi.houseDetail(this, 0x001, tokenId, house_id, callBack);
    }

    @Override
    protected void setListener()
    {
        calendarView = findViewById(R.id.calendarView);
        typeq = getIntent().getStringExtra("type");
        Log.i("android.os.Build.BRAND", Build.MODEL);
        checkDate();
        tokenId = StorageUtil.getTokenId(this);
        detailRoomCollect.setVisibility(View.VISIBLE);
        house_id = getIntent().getStringExtra("house_id");
        StorageUtil.setKeyValue(this, "islt", "");
        ivBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
        cal = Calendar.getInstance();
        detailConnectPhone.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                call(h_mobile);
            }
        });
        detailConnectOnline.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                MinSuApi.addChatList(RoomDetailActivity.this, 0x003, tokenId, user_id, callBack);
            }
        });
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(manager);
        // calendarView.setShowOtherDates(MaterialCalendarView.SHOW_OTHER_MONTHS);
        // calendarView.setDateTextAppearance();
        quickReserve.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (startday == null || startday.equals(""))
                {
                    ToastManager.show("请选择入住和离开时间");
                    return;
                }
                if (days == 0)
                {
                    ToastManager.show("请选择入住和离开时间");
                    return;
                }

                String isname = StorageUtil.getValue(RoomDetailActivity.this, "is_name");
                if (isname != null && isname.equals("isname"))
                {
                    final String ruzhu_time = getStringData(startday);
                    MinSuApi.isyoufang(ruzhu_time, Integer.parseInt(house_id), days, 0x007, callBack);
                    quickReserve.setEnabled(false);
                    quickReserve.setFocusableInTouchMode(false);
                } else
                {
                    ToastManager.show("必须实名认证后才能订房");
                    return;
                }

            }
        });


        //查看评价
        detailRoomCommentNumber.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(RoomDetailActivity.this, CommentActivity.class);
                intent.putExtra("house_id", house_id);
                startActivity(intent);
            }
        });
    }

    private void getTotalDays(String ruzhu_time, String leave_time)
    {
        if (ruzhu_time.equals("") || leave_time.equals(""))
        {
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try
        {
            Date d1 = sdf.parse(ruzhu_time);
            Date d2 = sdf.parse(leave_time);

            days = daysBetween(d1, d2);
            Log.i(TAG, "onClick: " + days);
            if (days == 0)
            {
                days = 1;
            }
            totalDay.setText("共" + Math.abs(days) + "晚");

        } catch (ParseException e)
        {
            e.printStackTrace();
        }
    }

    public int daysBetween(Date smdate, Date bdate) throws ParseException
    {
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


    private class EnableOneToTenDecorator implements DayViewDecorator
    {
        private final Drawable drawable;

        public EnableOneToTenDecorator(Activity activity)
        {
            drawable = activity.getResources().getDrawable(R.mipmap.launcher_02);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day)
        {
            return istoday(day);
        }

        @Override
        public void decorate(DayViewFacade view)
        {
            view.setDaysDisabled(false);
        }
    }


    private boolean istoday(CalendarDay day)
    {
        if (isPastDate(day.getYear() + "-" + (day.getMonth() + 1) + "-" + day.getDay()))
        {
            return true;
        }
        return false;
    }


    public class AllDecorator implements DayViewDecorator
    {
        private final Drawable drawable;

        public AllDecorator(Activity activity)
        {
            drawable = activity.getResources().getDrawable(R.mipmap.sign_2);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day)
        {
            return noPastDate(day.getYear() + "-" + (day.getMonth() + 1) + "-" + day.getDay());
        }

        @Override
        public void decorate(DayViewFacade view)
        {
            view.addSpan(new Textspan("\n" + house_price, false, RoomDetailActivity.this));
        }
    }

    public class MySelectorDecorator extends AllDecorator
    {

        private final Drawable drawable;

        public MySelectorDecorator(Activity context)
        {
            super(context);
            drawable = context.getResources().getDrawable(R.mipmap.sign_2);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day)
        {

            if (!compareDay(startday, day) && compareDay(endday, day))
            {
                return true;
            } else
            {
                return false;
            }

        }

        @Override
        public void decorate(DayViewFacade view)
        {
            //view.setSelectionDrawable(drawable);
            view.addSpan(new Textspan("\n" + house_price, true, RoomDetailActivity.this));
        }
    }

    protected String getStringData(CalendarDay day)
    {
        return day.getYear() + "-" + (day.getMonth() + 1) + "-" + day.getDay();
    }

    public class MySelector2Decorator extends AllDecorator
    {

        private final Drawable drawable;

        public MySelector2Decorator(Activity context)
        {
            super(context);
            drawable = context.getResources().getDrawable(R.mipmap.launcher_01);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day)
        {
            return compareeql(startday, day);
        }

        @Override
        public void decorate(DayViewFacade view)
        {
            view.addSpan(new Textspan("\n" + house_price, true, RoomDetailActivity.this));
        }
    }

    public class MySelector3Decorator extends AllDecorator
    {

        private final Drawable drawable;

        public MySelector3Decorator(Activity context)
        {
            super(context);
            drawable = context.getResources().getDrawable(R.mipmap.launcher_02);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day)
        {
            return compareeql(endday, day);
        }

        @Override
        public void decorate(DayViewFacade view)
        {
            view.addSpan(new Textspan("\n" + house_price, true, RoomDetailActivity.this));
        }
    }

    private CalendarDay startday;
    private CalendarDay endday;


    public static boolean noPastDate(String str)
    {

        boolean flag = false;
        Date nowDate = new Date();
        Date pastDate = null;
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        //在日期字符串非空时执行
        if (str != null && !"".equals(str))
        {
            try
            {
                //将字符串转为日期格式，如果此处字符串为非合法日期就会抛出异常。
                pastDate = sdf.parse(str);
                //调用Date里面的before方法来做判断
                flag = pastDate.after(nowDate);
                if (flag)
                {
                    System.out.println("该日期早于今日");
                } else
                {
                    System.out.println("该日期晚于今日");

                }
            } catch (ParseException e)
            {
                e.printStackTrace();
            }
        } else
        {
            System.out.println("日期参数不可为空");
        }
        Calendar calendar = Calendar.getInstance();

        int ny = calendar.get(Calendar.YEAR);
        int nm = calendar.get(Calendar.MONTH) + 1;
        int nd = calendar.get(Calendar.DAY_OF_MONTH);

        String nowday = ny + "-" + nm + "-" + nd;
        if (nowday.equals(str))
        {
            flag = true;
        }
        return flag;
    }


    public static boolean isPastDate(String str)
    {

        boolean flag = false;
        Date nowDate = new Date();
        Date pastDate = null;
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        //在日期字符串非空时执行
        if (str != null && !"".equals(str))
        {
            try
            {
                //将字符串转为日期格式，如果此处字符串为非合法日期就会抛出异常。
                pastDate = sdf.parse(str);
                //调用Date里面的before方法来做判断
                flag = pastDate.before(nowDate);
                if (flag)
                {
                    System.out.println("该日期早于今日");
                } else
                {
                    System.out.println("该日期晚于今日");

                }
            } catch (ParseException e)
            {
                e.printStackTrace();
            }
        } else
        {
            System.out.println("日期参数不可为空");
        }
        Calendar calendar = Calendar.getInstance();

        int ny = calendar.get(Calendar.YEAR);
        int nm = calendar.get(Calendar.MONTH) + 1;
        int nd = calendar.get(Calendar.DAY_OF_MONTH);

        String nowday = ny + "-" + nm + "-" + nd;
        if (nowday.equals(str))
        {
            flag = false;
        }
        return flag;
    }


//    private static class EnableOneToTenDecorator implements DayViewDecorator
//    {
//        Calendar cal = Calendar.getInstance();
//
//        /**
//         * 对<=10的日期，设置效果
//         *
//         * @param day {@linkplain CalendarDay} to possibly decorate
//         * @return
//         */
//        @Override
//        public boolean shouldDecorate(CalendarDay day)
//        {
//            return false;
//        }
//
//        /**
//         * 具体实现的效果
//         *
//         * @param view View to decorate
//         */
//        @Override
//        public void decorate(DayViewFacade view)
//        {
//            view.addSpan(new Textspan(""));
//        }
//    }

    @SuppressLint("WrongConstant")
    private void checkDate()
    {
        calendarView.setSelectionMode(2);

        // calendarView.addDecorator(new MySelectorDecorator(RoomDetailActivity.this));
        calendarView.setOnDateChangedListener(new OnDateSelectedListener()
        {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected)
            {
                Log.i(TAG, "onRangeSelected: " + "asds");
                Log.i(TAG, "onRangeSelected: " + date);


                CalendarDay select_date = widget.getSelectedDate();
                if (select_date == null)
                {
                    return;
                }
                int select_year = select_date.getYear();
                int select_month = select_date.getMonth() + 1;
                int select_day = select_date.getDay();
                if (isPastDate(select_year + "-" + select_month + "-" + select_day))
                {
                    Toast.makeText(RoomDetailActivity.this, "入住时间不能小于今天", Toast.LENGTH_SHORT).show();
                    calendarView.clearSelection();
                    startday = null;
                    endday = null;
                    return;
                } else
                {

                }

                if (startday == null)
                {
                    startday = date;
                    ruzhuTime.setText(startday.getYear() + "-" + (startday.getMonth() + 1) + "-" + startday.getDay());
                } else
                {
                    if (compareDay(startday, date))//选择时间小于开始时间返回true
                    {
//                        CalendarDay temp = startday;
//                        startday = date;
//                        endday = temp;
//                        leaveTime.setText(endday.getYear() + "-" + (endday.getMonth() + 1) + "-" + endday.getDay());
//                        ruzhuTime.setText(startday.getYear() + "-" + (startday.getMonth() + 1) + "-" + startday.getDay());
                        endday = startday;
                        startday = date;
                        leaveTime.setText(endday.getYear() + "-" + (endday.getMonth() + 1) + "-" + endday.getDay());
                        ruzhuTime.setText(startday.getYear() + "-" + (startday.getMonth() + 1) + "-" + startday.getDay());
                    } else
                    {
                        if (endday != null)
                        {
                            if (!compareDay(startday, date) && compareDay(endday, date))
                            {
                                startday = date;
                            } else
                            {
                                endday = date;
                            }
                        } else
                        {
                            endday = date;
                        }
                    }

                    if (endday != null)
                    {
                        if (compareDay(startday, endday))//后者小于前者返回true
                        {
                            ruzhuTime.setText(endday.getYear() + "-" + (endday.getMonth() + 1) + "-" + endday.getDay());
                            leaveTime.setText(startday.getYear() + "-" + (startday.getMonth() + 1) + "-" + startday.getDay());
                        } else if (!compareDay(startday, endday))
                        {
                            leaveTime.setText(endday.getYear() + "-" + (endday.getMonth() + 1) + "-" + endday.getDay());
                            ruzhuTime.setText(startday.getYear() + "-" + (startday.getMonth() + 1) + "-" + startday.getDay());
                        } else
                        {
                            ruzhuTime.setText(startday.getYear() + "-" + (startday.getMonth() + 1) + "-" + startday.getDay());
                            leaveTime.setText("请输入离开时间");
                            totalDay.setText("共" + 0 + "晚");
                        }
                    }


                }


                if (endday != null)
                {
                    getTotalDays(startday.getYear() + "-" + (startday.getMonth() + 1) + "-" + startday.getDay(), endday.getYear() + "-" + (endday.getMonth() + 1) + "-" + endday.getDay());
                    //  leaveTime.setText(endday.getYear() + "-" + (endday.getMonth() + 1) + "-" + endday.getDay());
                }
                calendarView.removeDecorators();
                selectedDates = calendarView.getSelectedDates();

                List<CalendarDay> data = new ArrayList<>();

                for (CalendarDay day : selectedDates)
                {
                    data.add(day);
                }

                for (CalendarDay day : data)
                {
                    calendarView.setDateSelected(day, false);
                }

                calendarView.setDateSelected(startday, true);
                if (endday != null)
                {
                    calendarView.setDateSelected(endday, true);
                }

                // calendarView.setSe

                if (endday == null)
                {
                    calendarView.addDecorators(new MySelector2Decorator(RoomDetailActivity.this), new AllDecorator(RoomDetailActivity.this));
                } else
                {
                    calendarView.addDecorators(new MySelectorDecorator(RoomDetailActivity.this)
                            , new MySelector2Decorator(RoomDetailActivity.this), new MySelector3Decorator(RoomDetailActivity.this), new AllDecorator(RoomDetailActivity.this));
                }
            }
        });


    }

    private Boolean compareeql(CalendarDay startday, CalendarDay date)
    {
        return startday.getYear() == date.getYear() && startday.getMonth() == date.getMonth() && startday.getDay() == date.getDay();
    }

    private Boolean compareDay(CalendarDay startday, CalendarDay date)
    {
        if (date.getYear() < startday.getYear())//选择时间小于开始时间
        {
            return true;
        } else if (date.getYear() > startday.getYear())//选择时间大于开始时间返回false
        {
            return false;
        } else
        {
            if (date.getMonth() < startday.getMonth())
            {
                return true;
            } else if (date.getMonth() > startday.getMonth())
            {
                return false;
            } else
            {
                if (date.getDay() < startday.getDay())
                {
                    return true;
                } else if (date.getDay() > startday.getDay())
                {
                    return false;
                } else
                {
                    return true;
                }
            }
        }
    }

    private void call(String phone)
    {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void loadViewLayout()
    {
        setContentView(R.layout.activity_room_detail);
    }

    @Override
    protected Context getActivityContext()
    {
        return this;
    }

    private CallBack callBack = new CallBack()
    {
        @Override
        public void onSuccess(int what, Response<String> result)
        {
            switch (what)
            {
                case 0x001:
                    try
                    {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        String msg = jsonObject.getString("msg");
                        if (code == 200)
                        {
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
                            String address_detail = jsonObject1.getString("address_detail");
                            if (address_detail == null || address_detail.equals("null"))
                            {
                                address_detail = "";
                            }
                            calendarView.addDecorators(new AllDecorator(RoomDetailActivity.this));
                            user_id = jsonObject1.getInt("user_id");
                            ArrayList<FacilitiesBean> sheshiList = new ArrayList<>();
                            String[] split = status.split(",");
                            for (String aSplit : split)
                            {
                                sheshiList.add(DataProvider.getFacilitiesList().get(Integer.parseInt(aSplit) - 1));
                            }
                            Log.i(TAG, "onSuccess: " + sheshiList);
                            FacilitiesAdapter facilitiesAdapter = new FacilitiesAdapter(R.layout.item_facilities, sheshiList);
                            recyclerView.setAdapter(facilitiesAdapter);
                            JSONArray house_img = jsonObject1.getJSONArray("house_img");
                            List<String> bannerImage = new ArrayList<>();
                            for (int i = 0; i < house_img.length(); i++)
                            {
                                bannerImage.add(Constant.BASE2_URL + house_img.get(i));
                            }
                            adapter = new BGABannerAdapter(RoomDetailActivity.this);
                            bannerPager.setAdapter(adapter);
                            bannerPager.setData(bannerImage, null);

                            adapter.setItemClick(new BGABannerAdapter.ItemClick()
                            {
                                @Override
                                public void onItemClick(View view)
                                {
                                    if (typeq != null && typeq.equals("xiugai"))
                                    {
                                        Intent intent = new Intent(RoomDetailActivity.this, XiuGaiHouseActivity.class);
                                        intent.putExtra("token", tokenId);
                                        intent.putExtra("house_id", house_id);
                                        startActivityForResult(intent, 10);
                                    }

                                }
                            });
                            adapter.setLongClick(new BGABannerAdapter.OnLongClick()
                            {
                                @Override
                                public void itemLongClick(int position)
                                {

                                }
                            });
                            h_mobile = jsonObject1.getString("h_mobile");
                            String head_pic = jsonObject1.getString("head_pic");
                            String com_count = jsonObject1.getString("com_count");
                            collect = jsonObject1.getInt("collect");
                            detailRoomName.setText(title);
                            detailRoomDescription.setText(house_info);
//                            detailConnectPhone.setText(h_mobile);
                            detailUserName.setText(h_name);
                            detailRoomCommentNumber.setText(com_count + "条评价");
                            detailRoomAddress.setText(province + " " + city + " " + district + " " + town + address_detail);
                            detailRoomPrice.setText("￥" + house_price + "/天");
                            if (head_pic == null || head_pic.equals("") || head_pic.equals("null"))
                            {
                                Glide.with(RoomDetailActivity.this)
                                        .load(R.mipmap.avatar_default)
                                        .into(detailUserImg);
                                return;
                            } else
                            {
                                try
                                {
                                    Glide.with(RoomDetailActivity.this)
                                            .load(Constant.BASE2_URL + head_pic)
                                            .into(detailUserImg);
                                } catch (IllegalArgumentException e)
                                {

                                }
                            }
                            if (collect == 1)
                            {
                                //已收藏detailRoomCollect
                                Glide.with(RoomDetailActivity.this)
                                        .load(R.mipmap.collected)
                                        .into(detailRoomCollect);
                            } else if (collect == 0)
                            {
                                //未收藏
                                Glide.with(RoomDetailActivity.this)
                                        .load(R.mipmap.collect)
                                        .into(detailRoomCollect);
                            }
                            detailRoomCollect.setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View view)
                                {
                                    if (collect == 1)//已收藏
                                    {
                                        MinSuApi.cancelCollect(0x005, tokenId, house_id, callBack);
                                    } else
                                    {
                                        MinSuApi.addCollect(0x006, tokenId, house_id, callBack);
                                    }
                                }
                            });
                        } else if (code == 111)
                        {
                            ToastManager.show(msg);
                        }
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    break;
                case 0x002://获取订单id
                    try
                    {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        String msg = jsonObject.getString("msg");
                        if (code == 200)
                        {
                            ToastManager.show(msg);
                            order_id = jsonObject.getString("data");
                            Intent intent = new Intent(RoomDetailActivity.this, OrderSubmitActivity.class);
                            intent.putExtra("order_id", order_id + "");
                            startActivityForResult(intent, 111);
                        } else if (code == 211)
                        {
                            ToastManager.show(msg);
                        }
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    break;
                case 0x003:
                    try
                    {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        String msg = jsonObject.getString("msg");
                        if (code == 200)
                        {
//                            ToastManager.show(msg);
                            data = jsonObject.getString("data");//聊天列表id
                            StorageUtil.setKeyValue(RoomDetailActivity.this, "tarid", data);
                            //todo
                            // 进入聊天界面

                            MinSuApi.getRongyunToken(RoomDetailActivity.this, 0x004, tokenId, Integer.parseInt(data), callBack);
                        } else if (code == 211)
                        {
                            ToastManager.show(msg);
                        }
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    break;
                case 0x004:
                    try
                    {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        if (code == 200)
                        {
                            JSONObject data1 = jsonObject.getJSONObject("data1");
                            JSONObject jsonObject1 = new JSONObject(data1.toString());
                            String token = jsonObject1.getString("token");
                            JSONObject data2 = jsonObject.getJSONObject("data2");
                            JSONObject jsonObject2 = new JSONObject(data2.toString());
                            final String userId = jsonObject2.getString("userId");//聊天人id
                            final String nicknae=jsonObject2.getString("nickname");
                            head_pic = jsonObject2.getString("head_pic");
                            if (!head_pic.startsWith("http"))
                            {
                                head_pic=Constant.BASE2_URL+head_pic;
                            }
                            Log.i("token111", token);
                            RongIM.setUserInfoProvider(new RongIM.UserInfoProvider()
                            {
                                @Override
                                public UserInfo getUserInfo(String s)
                                {
                                    return new UserInfo(userId, nicknae, Uri.parse(head_pic));
                                }
                            }, true);
                            RongIM.getInstance().refreshUserInfoCache(new UserInfo(userId, nicknae, Uri.parse(head_pic)));
                            isReconnect(token, userId);
                        }
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    break;
                case 0x005:
                    try
                    {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        if (code == 200)
                        {
                            collect = 0;
                            StorageUtil.setKeyValue(RoomDetailActivity.this, "isquxiao", "is");
                            ToastManager.show("取消成功");
                            //未收藏
                            Glide.with(RoomDetailActivity.this)
                                    .load(R.mipmap.collect)
                                    .into(detailRoomCollect);
                        }
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    break;
                case 0x006:
                    try
                    {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        if (code == 200)
                        {
                            collect = 1;
                            ToastManager.show("收藏成功");
                            StorageUtil.setKeyValue(RoomDetailActivity.this, "isquxiao", "no");
                            //已收藏detailRoomCollect
                            Glide.with(RoomDetailActivity.this)
                                    .load(R.mipmap.collected)
                                    .into(detailRoomCollect);
                        }
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    break;
                case 0x007:
                    try
                    {
                        JSONObject jsonObject = new JSONObject(result.body());
                        String code = jsonObject.getString("code");
                        if (code.equals("200"))
                        {
                            quickReserve.setEnabled(true);
                            quickReserve.setFocusableInTouchMode(true);
                            String isfd = StorageUtil.getValue(RoomDetailActivity.this, "isfd");
                            if (isfd.equals("yes"))
                            {
                                ToastManager.show("您现在是房东状态，不可订房");
                                return;
                            }
//                final String ruzhu_time = ruzhuTime.getText().toString();
//                final String leave_time = leaveTime.getText().toString();
                            if (startday == null || endday == null)
                            {
                                ToastManager.show("请选择日期");
                                return;
                            }
                            final String ruzhu_time = getStringData(startday);
                            final String leave_time = getStringData(endday);
                            getTotalDays(ruzhu_time, leave_time);
                            ruzhuTime.setText(ruzhu_time);
                            leaveTime.setText(leave_time);
                            Log.i(TAG, "checkDate: " + ruzhu_time + "asd");
                            if (ruzhu_time.equals(""))
                            {
                                ToastManager.show("入住时间不能为空");
                                return;
                            }
                            if (leave_time.equals(""))
                            {
                                ToastManager.show("离开时间不能为空");
                                return;
                            }
                            if (days == 0)
                            {
                                return;
                            }
                            if (house_price.equals(""))
                            {
                                return;
                            }
                            MinSuApi.orderSubmit(RoomDetailActivity.this, 0x002, tokenId, Integer.parseInt(house_id), ruzhu_time, leave_time, days, house_price, callBack);
                        } else
                        {
                            quickReserve.setEnabled(true);
                            quickReserve.setFocusableInTouchMode(true);
                            ToastManager.show("房间已定完，您来晚了");
                            return;
                        }

                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    break;
                case 8:
                    try
                    {
                        JSONObject object = new JSONObject(result.body());
                        String code = object.getString("code");
                        if (code.equals("200"))
                        {
                            StorageUtil.setKeyValue(RoomDetailActivity.this, "islt", "");
                            Log.i("deleteeee", "删除成功");
                        }
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    break;
            }
        }

        @Override
        public void onFail(int what, Response<String> result)
        {

        }

        @Override
        public void onFinish(int what)
        {

        }
    };

    private void isReconnect(String token, final String userId)
    {
        String name = getApplicationInfo().packageName + "------" + App.getCurProcessName(getApplicationContext());
        if (getApplicationInfo().packageName.equals(App.getCurProcessName(getApplicationContext())))
        {

            if (RongIM.getInstance().getRongIMClient().getCurrentConnectionStatus() == RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)
            {
                RongIM.connect(token, new RongIMClient.ConnectCallback()
                {
                    @Override
                    public void onTokenIncorrect()
                    {

                    }

                    @Override
                    public void onSuccess(final String user_id)
                    {
                        Log.i(TAG, "onSuccess: " + "融云连接成功");
                        String chat_title = StorageUtil.getValue(RoomDetailActivity.this, "chat_title");
                        if (RongIM.getInstance() != null)
                        {
                            RongIM.getInstance().enableUnreadMessageIcon(true);//显示未读消息数目
                            final String nickkk = StorageUtil.getValue(RoomDetailActivity.this, "nickkk");
                            final String imgkkk = StorageUtil.getValue(RoomDetailActivity.this, "imgkkk");
                            RongIM.setUserInfoProvider(new RongIM.UserInfoProvider()
                            {
                                @Override
                                public UserInfo getUserInfo(String s)
                                {
                                    return new UserInfo(userId, nickkk, Uri.parse(imgkkk));
                                }
                            }, true);
                            RongIM.getInstance().refreshUserInfoCache(new UserInfo(userId, nickkk, Uri.parse(imgkkk)));
//                        UserInfo userInfo = new UserInfo(user_id, StorageUtil.getValue(RoomDetailActivity.this, "nickname"), Uri.parse(StorageUtil.getValue(RoomDetailActivity.this, "head_pic")));
//                        RongIM.getInstance().setCurrentUserInfo(userInfo);
                            RongIM.getInstance().startPrivateChat(RoomDetailActivity.this, userId, chat_title);
                            StorageUtil.setKeyValue(RoomDetailActivity.this, "islt", "no");
                        }

//                        RongIM.getInstance().setSendMessageListener(new RongIM.OnSendMessageListener()
//                        {
//                            @Override
//                            public Message onSend(Message message)
//                            {
//                                StorageUtil.setKeyValue(RoomDetailActivity.this, "islt", "yes");
//                                return message;
//                            }
//
//                            @Override
//                            public boolean onSent(Message message, RongIM.SentMessageErrorCode sentMessageErrorCode)
//                            {
//                                return true;
//                            }
//                        });
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode)
                    {

                    }
                });
                //已经连接
            } else if (RongIM.getInstance().getRongIMClient().getCurrentConnectionStatus() == RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED)
            {
                RongIM.getInstance().enableUnreadMessageIcon(true);//显示未读消息数目
                String chat_title = StorageUtil.getValue(RoomDetailActivity.this, "chat_title");
                final String nickkk = StorageUtil.getValue(RoomDetailActivity.this, "nickkk");
                final String imgkkk = StorageUtil.getValue(RoomDetailActivity.this, "imgkkk");
//                UserInfo userInfo = new UserInfo( userId,nickkk, Uri.parse(imgkkk));
//                RongIM.getInstance().setCurrentUserInfo(userInfo);
//                RongIM.getInstance().setMessageAttachedUserInfo(true);
                RongIM.getInstance().startPrivateChat(RoomDetailActivity.this, userId, chat_title);
                StorageUtil.setKeyValue(RoomDetailActivity.this, "islt", "no");
            }

            RongIM.getInstance().setSendMessageListener(new RongIM.OnSendMessageListener()
            {
                @Override
                public Message onSend(Message message)
                {
                    StorageUtil.setKeyValue(RoomDetailActivity.this, "islt", "yes");
                    return message;
                }

                @Override
                public boolean onSent(Message message, RongIM.SentMessageErrorCode sentMessageErrorCode)
                {
                    return false;
                }
            });
        }



    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        String isliaotian = StorageUtil.getValue(RoomDetailActivity.this, "islt");
        if (isliaotian != null && isliaotian.equals("yes"))
        {

        } else if (isliaotian != null && isliaotian.equals("no"))
        {
            int id = Integer.parseInt(data);
            MinSuApi.delete_char(id, 8, callBack);
        } else
        {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        try
        {
            String issuccess = StorageUtil.getValue(RoomDetailActivity.this, "issuccess");
            StorageUtil.setKeyValue(RoomDetailActivity.this, "issuccess", "no");
            if (issuccess.equals("yes"))
            {
                Intent intent = new Intent(RoomDetailActivity.this, OredrDeliActivity.class);
                intent.putExtra("order_id", order_id);
                startActivity(intent);
                finish();
            }

            if (requestCode == 10)
            {
                String xiugai = StorageUtil.getValue(RoomDetailActivity.this, "xiugai");
                if (xiugai != null && xiugai.equals("xiugai"))
                {
                    MinSuApi.houseDetail(this, 0x001, tokenId, house_id, callBack);
                }
            }
        } catch (Exception e)
        {

        }
    }
}
