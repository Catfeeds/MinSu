package com.zhuye.minsu.common;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.zhuye.minsu.App;
import com.zhuye.minsu.R;
import com.zhuye.minsu.base.BaseActivity;
import com.zhuye.minsu.common.fragment.FragmentController;
import com.zhuye.minsu.utils.StorageUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    private static final String TAG_PAGE_HOME = "首页";
    private static final String TAG_PAGE_FIND = "发现";
    private static final String TAG_PAGE_MESSAGE = "消息";
    private static final String TAG_PAGE_ORDER = "订单";
    private static final String TAG_PAGE_USER = "我的";
    @BindView(R.id.frame_layout)
    FrameLayout frameLayout;
    @BindView(R.id.rb_home)
    RadioButton rbHome;
    @BindView(R.id.rb_find)
    RadioButton rbFind;
    @BindView(R.id.rb_message)
    RadioButton rbMessage;
    @BindView(R.id.rb_order)
    RadioButton rbOrder;
    @BindView(R.id.rb_me)
    RadioButton rbMe;
    @BindView(R.id.hometab_radio)
    RadioGroup hometabRadio;
    private FragmentController controller;

    private LocationClient mLocationClick = null;
    private String city;
    private static final String TAG = "MainActivity";

    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {
        hometabRadio = (RadioGroup) findViewById(R.id.hometab_radio);
        hometabRadio.setOnCheckedChangeListener(this);
        rbOrder.setVisibility(View.GONE);
//        rbHome.setVisibility(View.GONE);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_main);
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
//            MinSuApi.homeShow(this, 0x001, tokenId, city, callBack);
            StorageUtil.setKeyValue(MainActivity.this, "location_city", city);
            controller = FragmentController.getInstance(MainActivity.this, R.id.frame_layout);
            controller.showFragment(0);
            Log.i(TAG, "onReceiveLocation: " + city);
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

    @Override
    protected Context getActivityContext() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        switch (checkedId) {
            case R.id.rb_home:
//                toolbarTitle.setText("动态");
                controller.showFragment(0);
                break;
            case R.id.rb_find:
                controller.showFragment(1);
                break;
            case R.id.rb_message:
                controller.showFragment(2);
                break;
            case R.id.rb_order:
                controller.showFragment(3);
                break;
            case R.id.rb_me:
                controller.showFragment(4);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClick.stop();
    }
}
