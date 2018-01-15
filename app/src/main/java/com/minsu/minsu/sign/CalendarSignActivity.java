package com.minsu.minsu.sign;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
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
import com.minsu.minsu.R;
import com.minsu.minsu.api.MinSuApi;
import com.minsu.minsu.api.callback.CallBack;
import com.minsu.minsu.base.BaseActivity;
import com.minsu.minsu.utils.DateUtil;
import com.minsu.minsu.utils.StorageUtil;
import com.minsu.minsu.utils.ToastManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

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
    private ArrayList<Date> signData;

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
        tvSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MinSuApi.clickSign(CalendarSignActivity.this, 0x002, tokenId, callBack);
            }
        });
    }
    public class MySelectorDecorator implements DayViewDecorator {

        private final Drawable drawable;

        public MySelectorDecorator(Activity context) {
            drawable = context.getResources().getDrawable(R.mipmap.sign_2);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return signData.contains(day.getDate());
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.setSelectionDrawable(drawable);
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
                                String sign_time = signBean.data1.get(i).sign_time;
                                Date date = DateUtil.getDate(sign_time);
                                signData.add(date);
                            }
                            Log.i(TAG, "onSuccess: " + signData);
                            for (int i=0;i<signData.size();i++){
                                calendarView.setSelectedDate(signData.get(i));
                            }
                            calendarView.addDecorator(new MySelectorDecorator(CalendarSignActivity.this));
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
