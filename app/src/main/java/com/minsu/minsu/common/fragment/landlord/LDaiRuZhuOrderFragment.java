package com.minsu.minsu.common.fragment.landlord;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.lzy.okgo.model.Response;
import com.minsu.minsu.R;
import com.minsu.minsu.api.MinSuApi;
import com.minsu.minsu.api.callback.CallBack;
import com.minsu.minsu.base.BaseFragment;
import com.minsu.minsu.common.bean.OrderBean;
import com.minsu.minsu.common.fragment.landlord.adapter.FDaiRuZhuOrderListAdapter;
import com.minsu.minsu.houseResource.OrderDetailActvity;
import com.minsu.minsu.utils.StorageUtil;
import com.minsu.minsu.utils.ToastManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by hpc on 2018/1/17.
 */

public class LDaiRuZhuOrderFragment extends BaseFragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    Unbinder unbinder;
    private View view;
    List<OrderBean.Data> data = new ArrayList<>();
    private int page=1;
    private String tokenId;
    private FDaiRuZhuOrderListAdapter orderListAdapter;
    private int item;
    private Activity activity;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.fragment_system, container, false);
        return view;
    }

    @Override
    public void onAttach(Activity activity)
    {
        this.activity=activity;
        super.onAttach(activity);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser)
        {
            if (activity!=null)
            {
                MinSuApi.lanlordDairuzhuOrderList(activity, 0x001, tokenId,1, callBack);
            }
        }
    }

    @Override
    protected void initListener() {
        refreshLayout.autoRefresh();
        tokenId = StorageUtil.getTokenId(getActivity());
        refreshLayout.setOnRefreshListener(new OnRefreshListener()
        {
            @Override
            public void onRefresh(RefreshLayout refreshlayout)
            {
                MinSuApi.lanlordDairuzhuOrderListq(getActivity(), 0x001, tokenId, 1,callBack);
                if (StorageUtil.getValue(getActivity(),"networks")!=null&&
                        StorageUtil.getValue(getActivity(),"networks").equals("no"))
                {
                    refreshlayout.finishRefresh(3000,false);
                }
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener()
        {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout)
            {
                MinSuApi.lanlordDairuzhuOrderList(getActivity(), 0x003, tokenId, page,callBack);
                if (StorageUtil.getValue(getActivity(),"networks")!=null&&
                        StorageUtil.getValue(getActivity(),"networks").equals("no"))
                {
                    refreshlayout.finishLoadmore(3000,false);
                }
            }
        });
    }

    @Override
    protected void initData() {
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
                        if (refreshLayout!=null)
                        {
                            refreshLayout.finishLoadmore();
                            refreshLayout.finishRefresh();
                        }
                        if (code == 200) {
                            page=1;
                            page=page+1;
                            OrderBean orderBean = new Gson().fromJson(result.body(), OrderBean.class);
                            if (data!=null)
                            {
                                data.clear();
                                data.addAll(orderBean.data);
                            }
                            recyclerView=view.findViewById(R.id.recyclerView);
                            orderListAdapter = new FDaiRuZhuOrderListAdapter(R.layout.item_landlord_dairuzhu_order, data);
                            if (recyclerView==null)
                            {
                                return;
                            }
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            recyclerView.setAdapter(orderListAdapter);
                            if (orderBean.data.size() == 0) {
                                orderListAdapter.setEmptyView(R.layout.empty, recyclerView);
                            }
                            orderListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
                            {
                                @Override
                                public void onItemClick(BaseQuickAdapter adapter, View view, int position)
                                {
                                    Intent intent=new Intent(getActivity(), OrderDetailActvity.class);
                                    intent.putExtra("bean",data.get(position));
                                    intent.putExtra("type","1");
                                    startActivity(intent);
                                }
                            });
                            orderListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                                @Override
                                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                                    item = position;
                                    switch (view.getId()) {
                                        case R.id.confirm_ruzhu:
                                            ToastManager.show("待入住");
                                            MinSuApi.sureRuzhu(getActivity(), 0x002, tokenId, orderListAdapter.getItem(position).order_id, callBack);
                                            break;
                                    }
                                }
                            });
                        } else if (code == 201) {
                            recyclerView=view.findViewById(R.id.recyclerView);
                            orderListAdapter = new FDaiRuZhuOrderListAdapter(R.layout.item_landlord_dairuzhu_order, data);
                            if (recyclerView==null)
                            {
                                return;
                            }
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            recyclerView.setAdapter(orderListAdapter);
                            orderListAdapter.setEmptyView(R.layout.empty, recyclerView);
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
                            orderListAdapter.remove(item);
                            orderListAdapter.notifyDataSetChanged();
                           // MinSuApi.lanlordDairuzhuOrderList(getActivity(), 0x001, tokenId, callBack);
                        } else if (code == 211) {
                           // ToastManager.show(msg);
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
                        if (refreshLayout!=null)
                        {
                            refreshLayout.finishLoadmore();
                            refreshLayout.finishRefresh();
                        }
                        if (code == 200) {
                            page=page+1;
                            OrderBean orderBean = new Gson().fromJson(result.body(), OrderBean.class);
                            if (data!=null)
                            {
                                data.addAll(orderBean.data);
                            }else {
                                return;
                            }
//                            final FDaiRuZhuOrderListAdapter orderListAdapter = new FDaiRuZhuOrderListAdapter(R.layout.item_landlord_dairuzhu_order, data);
//                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//                            recyclerView.setAdapter(orderListAdapter);
                            if (orderBean.data.size() == 0) {
                                orderListAdapter.setEmptyView(R.layout.empty, recyclerView);
                            }
                            orderListAdapter.notifyDataSetChanged();
//                            orderListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
//                                @Override
//                                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//                                    switch (view.getId()) {
//                                        case R.id.confirm_ruzhu:
//                                            ToastManager.show("待入住");
//                                            MinSuApi.sureRuzhu(getActivity(), 0x002, tokenId, orderListAdapter.getItem(position).order_id, callBack);
//                                            break;
//                                    }
//                                }
//                            });
                        } else if (code == 201) {
                            // ToastManager.show(msg);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
