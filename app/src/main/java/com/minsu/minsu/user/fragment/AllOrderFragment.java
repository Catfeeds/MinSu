package com.minsu.minsu.user.fragment;

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
import com.minsu.minsu.user.TuiKuanApplyActivity;
import com.minsu.minsu.user.adapter.OrderListAdapter;
import com.minsu.minsu.utils.StorageUtil;
import com.minsu.minsu.utils.ToastManager;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by hpc on 2018/1/15.
 */

public class AllOrderFragment extends BaseFragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    Unbinder unbinder;
    private String tokenId;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_all_order, container, false);
    }

    @Override
    protected void initListener() {
        tokenId = StorageUtil.getTokenId(getActivity());

    }

    @Override
    protected void initData() {
        MinSuApi.allMyOrder(getActivity(), 0x001, tokenId, callBack);
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
                            OrderBean orderBean = new Gson().fromJson(result.body(), OrderBean.class);
                            final OrderListAdapter orderListAdapter = new OrderListAdapter(R.layout.item_order, orderBean.data);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            recyclerView.setAdapter(orderListAdapter);
                            if (orderBean.data.size() == 0) {
                                orderListAdapter.setEmptyView(R.layout.empty, recyclerView);
                            }
                            orderListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                                @Override
                                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                                    switch (view.getId()) {
                                        case R.id.order_pay:

                                            break;
                                        case R.id.order_cancel:
                                            MinSuApi.cancelOrder(getActivity(), 0x002, tokenId, orderListAdapter.getItem(position).order_id, callBack);
                                            break;
                                        case R.id.order_delete:
                                            MinSuApi.deleteOrder(getActivity(), 0x003, tokenId, orderListAdapter.getItem(position).order_id, callBack);
                                            break;
                                        case R.id.yudin_again:
//                                            MinSuApi.cancelOrder(getActivity(), 0x002, tokenId, orderListAdapter.getItem(position).order_id, callBack);
                                            break;
                                        case R.id.tuikuan_apply:
                                            //申请退款
                                            ToastManager.show("申请退款");
                                            Intent intent = new Intent(getActivity(), TuiKuanApplyActivity.class);
                                            intent.putExtra("order_id", orderListAdapter.getItem(position).order_id + "");
                                            startActivity(intent);
                                            break;
                                    }
                                }
                            });
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
                            MinSuApi.allMyOrder(getActivity(), 0x001, tokenId, callBack);
                        } else if (code == 211) {
                            ToastManager.show(msg);
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
                        if (code == 200) {
                            ToastManager.show(msg);
                            MinSuApi.allMyOrder(getActivity(), 0x001, tokenId, callBack);
                        } else if (code == 211) {
                            ToastManager.show(msg);
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
