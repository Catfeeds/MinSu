package com.zhuye.minsu.sign;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.model.Response;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.zhuye.minsu.R;
import com.zhuye.minsu.api.MinSuApi;
import com.zhuye.minsu.api.callback.CallBack;
import com.zhuye.minsu.base.BaseActivity;
import com.zhuye.minsu.utils.StorageUtil;
import com.zhuye.minsu.utils.ToastManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CalendarSignActivity extends BaseActivity {


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
    @BindView(R.id.tv_sign)
    TextView tvSign;
    @BindView(R.id.tv_integral)
    TextView tvIntegral;
    @BindView(R.id.calendarView)
    MaterialCalendarView calendarView;
    private String tokenId;
    private static final String TAG = "CalendarSignActivity";
    private String sign_time;
    private ArrayList<String> signData;

    protected void processLogic() {
        MinSuApi.signPage(this, 0x001, tokenId, callBack);
    }

    @Override
    protected void setListener() {

        tokenId = StorageUtil.getTokenId(this);
        toolbarTitle.setText("签到");
        ivLeft.setVisibility(View.VISIBLE);
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                Log.i(TAG, "onDateSelected: " + date);
            }
        });
        calendarView.addDecorator(new PrimeDayDisableDecorator());
        calendarView.addDecorator(new EnableOneToTenDecorator());
        Calendar calendar = Calendar.getInstance();
//        calendarView.setSelectedDate(calendar.getTime());

        tvSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MinSuApi.clickSign(CalendarSignActivity.this, 0x002, tokenId, callBack);
            }
        });
    }

    private static class PrimeDayDisableDecorator implements DayViewDecorator {

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return PRIME_TABLE[day.getDay()];
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.setDaysDisabled(true);
        }

        private static boolean[] PRIME_TABLE = {
                true,  // 0?
                true,
                true, // 2
                true, // 3
                true,
                true, // 5
                true,
                true, // 7
                true,
                true,
                true,
                true, // 11
                true,
                true, // 13
                true,
                true,
                true,
                true, // 17
                true,
                true, // 19
                true,
                true,
                true,
                true, // 23
                true,
                true,
                true,
                true,
                true,
                true, // 29
                true,
                true, // 31
                true,
                true,
                true, //PADDING
        };
    }

    private static class EnableOneToTenDecorator implements DayViewDecorator {

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return day.getDay() <= 10;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.setDaysDisabled(false);
        }
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_calendar_sign);
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
                            String data2 = jsonObject.getString("data2");
                            tvIntegral.setText("积分:" + data2);
                            SignBean signBean = new Gson().fromJson(result.body(), SignBean.class);

                            signData = new ArrayList<>();
                            for (int i = 0; i < signBean.data1.size(); i++) {
                                signData.add(signBean.data1.get(i).sign_time);
                            }
                            Log.i(TAG, "onSuccess: "+signData);

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
                            MinSuApi.signPage(CalendarSignActivity.this, 0x001, tokenId, callBack);
                            ToastManager.show(msg);
                            tvSign.setText("已签到");
                        } else if (code == 111) {
                            ToastManager.show(msg);
                        } else if (code == 101) {
                            ToastManager.show(msg);
                            tvSign.setText("已签到");
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
