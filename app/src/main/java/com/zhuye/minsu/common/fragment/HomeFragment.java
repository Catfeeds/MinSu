package com.zhuye.minsu.common.fragment;


import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.model.Response;
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


    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    protected void initListener() {
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }
}
