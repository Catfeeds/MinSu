package com.minsu.minsu.common.lanuch;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.minsu.minsu.App;
import com.minsu.minsu.R;
import com.minsu.minsu.common.MainActivity;
import com.minsu.minsu.common.lanuch.timer.BaseTimerTask;
import com.minsu.minsu.common.lanuch.timer.ITimerListener;
import com.minsu.minsu.user.LoginActivity;
import com.minsu.minsu.utils.StorageUtil;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

/**
 * Created by ll on 2017/10/11.
 * desc 闪屏页
 */

public class SplashActivity extends AppCompatActivity implements View.OnClickListener, ITimerListener {

    private Timer mTimer = null;
    private int mCount = 1;
    private TextView tv_launcher_timer;

    private ILauncherListener mILauncherListener = null;
    private LocationClient mLocationClick = null;
    private String city;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_launcher);

        mLocationClick = new LocationClient(App.getInstance());
        mLocationClick.registerLocationListener(new MyLocationListener());
//        StatusBarUtils.setColor(this,R.color.colorPrimary);
        List<String> permission = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permission.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permission.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permission.isEmpty()) {
            String[] permissions = permission.toArray(new String[permission.size()]);//将集合转化成数组
            //@onRequestPermissionsResult会接受次函数传的数据
            ActivityCompat.requestPermissions(this, permissions, 1);
        } else {
            initLoaction();
        }

    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //获取城市
            city = location.getCity();
            //初始化倒计时控件
            tv_launcher_timer = (TextView) findViewById(R.id.tv_launcher_timer);
            tv_launcher_timer.setOnClickListener(SplashActivity.this);
            //开始倒计时
            initTimer();
//            MinSuApi.homeShow(this, 0x001, tokenId, city, callBack);
            StorageUtil.setKeyValue(SplashActivity.this, "location_city", city);
//            ToastManager.show(city);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "" +
                                    "必须统一授权才能使用本程序", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    initLoaction();
                } else {
                    Toast.makeText(this, "" +
                            "发生未知错误", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    private void initLoaction() {
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(2000);//设置实时更新当前数据，
        // 但是活动被销毁的时候要调用mLocationClick.stop
        option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);//将定位模式指定
        //成传感器模式也就是说只能使用GPS进行定位
        option.setIsNeedAddress(true);//是否获取详细信息
        mLocationClick.setLocOption(option);
        mLocationClick.start();
    }

    private void initTimer() {
        mTimer = new Timer();
        final BaseTimerTask task = new BaseTimerTask(this);
        mTimer.schedule(task, 0, 1000);
    }

    @Override
    public void onClick(View view) {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
            //检查是否第一次进入APP，如果是显示轮播图，不是进入首页
            checkIsShowScroll();
        }
    }


    //判断是否显示滑动启动页
    private void checkIsShowScroll() {
        Intent intent;
//        boolean is_guide_show = SPUtils.getInstance().getBoolean("is_guide_show", false);
        boolean is_guide_show = StorageUtil.getBoolean(this, "is_guide_show", false);
        if (is_guide_show) {
            //已经做过新手导航，判断用户是否登陆

            String token = StorageUtil.getTokenId(this);
            if (!token.equals("")) {
                intent = new Intent(this, MainActivity.class);
            } else {
                intent = new Intent(this, LoginActivity.class);
            }
        } else {
            //没有做过新手导航
            intent = new Intent(SplashActivity.this, GuideActivity.class);
        }
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onTimer() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (tv_launcher_timer != null) {
                    tv_launcher_timer.setText(MessageFormat.format("跳过\n{0}s", mCount));
                    mCount--;
                    if (mCount < 0) {
                        if (mTimer != null) {
                            mTimer.cancel();
                            mTimer = null;
                            //检查是否第一次进入APP，如果是显示轮播图，不是进入首页
                            checkIsShowScroll();
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClick.stop();
    }
}
