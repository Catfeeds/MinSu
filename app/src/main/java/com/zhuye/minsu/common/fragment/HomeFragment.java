package com.zhuye.minsu.common.fragment;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.google.gson.Gson;
import com.lzy.okgo.model.Response;
import com.zhuye.minsu.App;
import com.zhuye.minsu.R;
import com.zhuye.minsu.api.Constant;
import com.zhuye.minsu.api.DataProvider;
import com.zhuye.minsu.api.MinSuApi;
import com.zhuye.minsu.api.callback.CallBack;
import com.zhuye.minsu.base.BaseFragment;
import com.zhuye.minsu.common.adapter.BGABannerAdapter;
import com.zhuye.minsu.common.bean.HomeBean;
import com.zhuye.minsu.utils.StorageUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bingoogolapple.bgabanner.BGABanner;


/**
 * Created by hpc on 2017/12/1.
 */

public class HomeFragment extends BaseFragment {


    @BindView(R.id.banner_pager)
    BGABanner bannerPager;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.search_tag)
    ImageView searchTag;
    @BindView(R.id.iv_sign)
    ImageView ivSign;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    Unbinder unbinder;

    private View view;
    private LocationClient mLocationClick=null;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        SDKInitializer.initialize(App.getInstance());
        mLocationClick = new LocationClient(App.getInstance());
        mLocationClick.registerLocationListener(new MyLocationListener());
        SDKInitializer.initialize(App.getInstance());
        view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    protected void initListener() {

        List<String> permission = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            permission.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
        {
            permission.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            permission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permission.isEmpty())
        {
            String[] permissions = permission.toArray(new String[permission.size()]);//将集合转化成数组
            //@onRequestPermissionsResult会接受次函数传的数据
            ActivityCompat.requestPermissions(getActivity(), permissions, 1);
        } else
        {
            initLoaction();
        }

        String tokenId = StorageUtil.getTokenId(getActivity());
        MinSuApi.homeShow(getActivity(),0x001,tokenId,"",callBack);
    }
private CallBack callBack=new CallBack() {
    @Override
    public void onSuccess(int what, Response<String> result) {
        try {
            JSONObject jsonObject = new JSONObject(result.body());
            int code = jsonObject.getInt("code");
            if (code==200){
                HomeBean homeBean = new Gson().fromJson(result.body(), HomeBean.class);
                List<HomeBean.Data1> data1 = homeBean.data1;
                List<HomeBean.Data2> data2 = homeBean.data2;
                getBannerData(data2);
            }
        } catch (JSONException e) {
            e.printStackTrace();
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
    protected void initData() {

    }

    private void getBannerData(List<HomeBean.Data2> data) {
        bannerPager = view.findViewById(R.id.banner_pager);
        bannerPager.setAdapter(new BGABannerAdapter(getActivity()));
//        ArrayList<Integer> bannerImageData = DataProvider.getBannerImage();
        List<String> bannerImage = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
        bannerImage.add(Constant.BASE2_URL+data.get(i).ad_code);
        }
        bannerPager.setData(bannerImage, null);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        mLocationClick.stop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    private String city;
    public class MyLocationListener implements BDLocationListener
    {

        @Override
        public void onReceiveLocation(BDLocation location){
            //获取城市
            city = location.getCity();
            Toast.makeText(getActivity(),city,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        switch (requestCode)
        {
            case 1:
                if (grantResults.length > 0)
                {
                    for (int result : grantResults)
                    {
                        if (result != PackageManager.PERMISSION_GRANTED)
                        {
                            Toast.makeText(getActivity(), "" +
                                    "必须统一授权才能使用本程序", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                  initLoaction();
                } else
                {
                    Toast.makeText(getActivity(), "" +
                            "发生未知错误", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }
    //初始化定位信息并开始定位
    private void initLoaction()
    {
        //LocationClientOption必须初始化
        LocationClientOption option=new LocationClientOption();
        option.setScanSpan(2000);//设置实时更新当前数据，
        // 但是活动被销毁的时候要调用mLocationClick.stop
        option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);//将定位模式指定
        //成传感器模式也就是说只能使用GPS进行定位
        option.setIsNeedAddress(true);//是否获取详细信息
        mLocationClick.setLocOption(option);
        mLocationClick.start();
    }
}
