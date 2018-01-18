package com.minsu.minsu.common.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.lzy.okgo.model.Response;
import com.minsu.minsu.R;
import com.minsu.minsu.api.Constant;
import com.minsu.minsu.api.MinSuApi;
import com.minsu.minsu.api.callback.CallBack;
import com.minsu.minsu.base.BaseFragment;
import com.minsu.minsu.common.CitySelectActivity;
import com.minsu.minsu.common.RoomDetailActivity;
import com.minsu.minsu.common.adapter.BGABannerAdapter;
import com.minsu.minsu.common.adapter.HomeListAdapter;
import com.minsu.minsu.common.bean.HomeBean;
import com.minsu.minsu.search.SearchActivity;
import com.minsu.minsu.sign.CalendarSignActivity;
import com.minsu.minsu.utils.StorageUtil;
import com.minsu.minsu.utils.ToastManager;

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
    @BindView(R.id.location_address)
    ImageView locationAddress;

    private View view;
    private String tokenId;
    private String location_city;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    protected void initListener() {
        tokenId = StorageUtil.getTokenId(getActivity());
        location_city = StorageUtil.getValue(getActivity(), "location_city");
        toolbarTitle.setOnClickListener(this);
        ivSign.setOnClickListener(this);
        locationAddress.setOnClickListener(this);

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
                            MinSuApi.homeShow(getActivity(), 0x001, tokenId, location_city, callBack);
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
                            MinSuApi.homeShow(getActivity(), 0x001, tokenId, location_city, callBack);
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
        MinSuApi.homeShow(getActivity(), 0x001, tokenId, location_city, callBack);
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == 0 && data != null) {
            //获取Bundle中的数据
            Bundle bundle = data.getExtras();
            String address = bundle.getString("address");
            //修改编辑框的内容
            ToastManager.show(address);
            MinSuApi.homeShow(getActivity(), 0x001, tokenId, address, callBack);
        }
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
            case R.id.location_address:
                Intent intent = new Intent(getActivity(), CitySelectActivity.class);
                startActivityForResult(intent, 0);
                break;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        MinSuApi.homeShow(getActivity(), 0x001, tokenId, location_city, callBack);
    }
}
