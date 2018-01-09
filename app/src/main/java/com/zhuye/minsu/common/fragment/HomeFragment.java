package com.zhuye.minsu.common.fragment;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.lzy.okgo.model.Response;
import com.zhuye.minsu.App;
import com.zhuye.minsu.R;
import com.zhuye.minsu.api.Constant;
import com.zhuye.minsu.api.MinSuApi;
import com.zhuye.minsu.api.callback.CallBack;
import com.zhuye.minsu.base.BaseFragment;
import com.zhuye.minsu.common.RoomDetailActivity;
import com.zhuye.minsu.common.adapter.BGABannerAdapter;
import com.zhuye.minsu.common.adapter.HomeListAdapter;
import com.zhuye.minsu.common.bean.HomeBean;
import com.zhuye.minsu.search.SearchActivity;
import com.zhuye.minsu.sign.CalendarSignActivity;
import com.zhuye.minsu.utils.StorageUtil;
import com.zhuye.minsu.utils.ToastManager;

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

public class HomeFragment extends BaseFragment implements View.OnClickListener {


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
    private LocationClient mLocationClick = null;
    private String city;
    private String tokenId;

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
        tokenId = StorageUtil.getTokenId(getActivity());
        List<String> permission = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permission.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permission.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permission.isEmpty()) {
            String[] permissions = permission.toArray(new String[permission.size()]);//将集合转化成数组
            //@onRequestPermissionsResult会接受次函数传的数据
            ActivityCompat.requestPermissions(getActivity(), permissions, 1);
        } else {
            initLoaction();
        }
        toolbarTitle.setOnClickListener(this);
        ivSign.setOnClickListener(this);

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
                            HomeBean homeBean = new Gson().fromJson(result.body(), HomeBean.class);
                            List<HomeBean.Data1> data1 = homeBean.data1;
                            List<HomeBean.Data2> data2 = homeBean.data2;
                            getBannerData(data2);
                            showHomeList(data1);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 0x002:
                    try {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        if (code == 200) {
                            ToastManager.show("取消成功");
                            MinSuApi.homeShow(getActivity(), 0x001, tokenId, city, callBack);
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
                            ToastManager.show("收藏成功");
                            MinSuApi.homeShow(getActivity(), 0x001, tokenId, city, callBack);
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

    private void showHomeList(List<HomeBean.Data1> data1) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        final HomeListAdapter homeListAdapter = new HomeListAdapter(R.layout.item_home_list, data1);
        homeListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.focus_room:
                        if (homeListAdapter.getItem(position).collect == 1) {

                            MinSuApi.cancelCollect(0x002, tokenId, homeListAdapter.getItem(position).house_id, callBack);
                        } else if (homeListAdapter.getItem(position).collect == 0) {
                            MinSuApi.addCollect(0x003, tokenId, homeListAdapter.getItem(position).house_id, callBack);
                        }
                        break;
                }
            }
        });
        homeListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), RoomDetailActivity.class);
                intent.putExtra("house_id", homeListAdapter.getItem(position).house_id);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(homeListAdapter);
    }

    @Override
    protected void initData() {

    }

    private void getBannerData(List<HomeBean.Data2> data) {
        bannerPager = view.findViewById(R.id.banner_pager);
        bannerPager.setAdapter(new BGABannerAdapter(getActivity()));
//        ArrayList<Integer> bannerImageData = DataProvider.getBannerImage();
        List<String> bannerImage = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            bannerImage.add(Constant.BASE2_URL + data.get(i).ad_code);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_title:
                startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
            case R.id.iv_sign:
                startActivity(new Intent(getActivity(), CalendarSignActivity.class));
                break;
        }
    }


    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //获取城市
            city = location.getCity();
            MinSuApi.homeShow(getActivity(), 0x001, tokenId, city, callBack);

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
                            Toast.makeText(getActivity(), "" +
                                    "必须统一授权才能使用本程序", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    initLoaction();
                } else {
                    Toast.makeText(getActivity(), "" +
                            "发生未知错误", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    //初始化定位信息并开始定位
    private void initLoaction() {
        //LocationClientOption必须初始化
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(2000);//设置实时更新当前数据，
        // 但是活动被销毁的时候要调用mLocationClick.stop
        option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);//将定位模式指定
        //成传感器模式也就是说只能使用GPS进行定位
        option.setIsNeedAddress(true);//是否获取详细信息
        mLocationClick.setLocOption(option);
        mLocationClick.start();
    }
}
