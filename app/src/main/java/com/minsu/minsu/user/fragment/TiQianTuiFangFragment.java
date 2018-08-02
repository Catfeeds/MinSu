package com.minsu.minsu.user.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.lzy.okgo.model.Response;
import com.minsu.minsu.R;
import com.minsu.minsu.api.MinSuApi;
import com.minsu.minsu.api.callback.CallBack;
import com.minsu.minsu.base.BaseFragment;
import com.minsu.minsu.common.bean.OrderBean;
import com.minsu.minsu.houseResource.OrderDetailActvity;
import com.minsu.minsu.user.TuiFangApplyActivity;
import com.minsu.minsu.user.TuiKuanApplyActivity;
import com.minsu.minsu.user.adapter.TiQianTuiFangOrderListAdapter;
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
 * Created by hpc on 2018/1/15.
 */

public class TiQianTuiFangFragment extends BaseFragment
{
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    Unbinder unbinder;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private String tokenId;
    private View view;
    private int page = 1;
    private List<OrderBean.Data> data = new ArrayList<>();
    private TiQianTuiFangOrderListAdapter orderListAdapter;
   private Activity activity;
    private int item;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container)
    {
        view = inflater.inflate(R.layout.fragment_all_order, container, false);
        return view;
    }

    @Override
    public void onAttach(Context context)
    {
        this.activity= (Activity) context;
        super.onAttach(context);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser)
        {
           if (activity!=null)
           {
               MinSuApi.tiqianTuifang(getActivity(), 0x001, tokenId, 1,callBack);
           }
        }
    }
    @Override
    protected void initListener()
    {
        tokenId = StorageUtil.getTokenId(getActivity());
        refreshLayout.setOnRefreshListener(new OnRefreshListener()
        {
            @Override
            public void onRefresh(RefreshLayout refreshlayout)
            {
                MinSuApi.tiqianTuifang(getActivity(), 0x001, tokenId, 1,callBack);
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
                MinSuApi.tiqianTuifang(getActivity(), 0x002, tokenId, page,callBack);
                if (StorageUtil.getValue(getActivity(),"networks")!=null&&
                        StorageUtil.getValue(getActivity(),"networks").equals("no"))
                {
                    refreshlayout.finishLoadmore(3000,false);
                }
            }
        });
        refreshLayout.autoRefresh();
        MinSuApi.tiqianTuifang(getActivity(), 0x001, tokenId,1, callBack);
    }

    @Override
    protected void initData()
    {

    }

    private CallBack callBack = new CallBack()
    {
        @Override
        public void onSuccess(int what, Response<String> result)
        {
            switch (what)
            {
                case 0x001:
                    try
                    {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        if (refreshLayout!=null)
                        {
                            refreshLayout.finishLoadmore();
                            refreshLayout.finishRefresh();
                        }
                        if (code == 200)
                        {
                            page=1;
                            page=page+1;
                            OrderBean orderBean = new Gson().fromJson(result.body(), OrderBean.class);
                            if (data!=null)
                            {
                                data.clear();
                                data.addAll(orderBean.data);
                            }else {
                                return;
                            }
                            orderListAdapter = new TiQianTuiFangOrderListAdapter(R.layout.item_order_tiqian_tuifang, data);
                            recyclerView = view.findViewById(R.id.recyclerView);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            recyclerView.setAdapter(orderListAdapter);
                            if (data.size() == 0)
                            {
                                orderListAdapter.setEmptyView(R.layout.empty, recyclerView);
                            }
                            orderListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
                            {
                                @Override
                                public void onItemClick(BaseQuickAdapter adapter, View view, int position)
                                {
                                    Intent intent=new Intent(getActivity(), OrderDetailActvity.class);
                                    intent.putExtra("bean",data.get(position));
                                    startActivity(intent);
                                }
                            });
                            orderListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener()
                            {
                                @Override
                                public void onItemChildClick(final BaseQuickAdapter adapter, View view, final int position)
                                {
                                    item = position;
                                    switch (view.getId())
                                    {
//                                        case R.id.tuikuan_apply:
//                                            //申请退款
//                                            LayoutInflater li = LayoutInflater.from(getActivity());
//                                            View promptsView = li.inflate(R.layout.tuikuan_dialog, null);
//
//                                            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
//
//                                            // set prompts.xml to alertdialog builder
//                                            alertDialogBuilder.setView(promptsView);
//                                            // create alert dialog
//                                            final AlertDialog alertDialog = alertDialogBuilder.create();
//                                            final TextView inputName = promptsView.findViewById(R.id.input_name);
//                                            final TextView mCancel = promptsView.findViewById(R.id.cancel);
//                                            final TextView mSure = promptsView.findViewById(R.id.sure);
//
//                                            inputName.setText("您确定要退房吗");
//                                            mCancel.setOnClickListener(new View.OnClickListener() {
//                                                @Override
//                                                public void onClick(View view) {
//                                                    alertDialog.dismiss();
//                                                }
//                                            });
//                                            mSure.setOnClickListener(new View.OnClickListener() {
//                                                @Override
//                                                public void onClick(View view) {
//                                                    Intent intent = new Intent(getActivity(), TuiKuanApplyActivity.class);
//                                                    intent.putExtra("order_id", orderListAdapter.getItem(item).order_id + "");
//                                                    startActivityForResult(intent,1);
//                                                    alertDialog.dismiss();
//                                                }
//                                            });
//                                            alertDialogBuilder
//                                                    .setCancelable(true);
//                                            // show it
//                                            alertDialog.show();
//                                           // ToastManager.show("申请退款");
//                                            break;
                                        case R.id.delete_ordertqtf:

                                            LayoutInflater li = LayoutInflater.from(getActivity());
                                            View promptsView = li.inflate(R.layout.tuikuan_dialog, null);

                                            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

                                            // set prompts.xml to alertdialog builder
                                            alertDialogBuilder.setView(promptsView);
                                            // create alert dialog
                                            final AlertDialog alertDialog = alertDialogBuilder.create();
                                            final TextView inputName = promptsView.findViewById(R.id.input_name);
                                            final TextView mCancel = promptsView.findViewById(R.id.cancel);
                                            final TextView mSure = promptsView.findViewById(R.id.sure);

                                            inputName.setText("您确定要删除吗");
                                            mCancel.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    alertDialog.dismiss();
                                                }
                                            });
                                            mSure.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    MinSuApi.deleteOrder(getActivity(), 0x003, tokenId, orderListAdapter.getItem(position).order_id, callBack);
                                                    alertDialog.dismiss();
                                                }
                                            });
                                            alertDialogBuilder
                                                    .setCancelable(true);
                                            // show it
                                            alertDialog.show();
                                            break;
                                    }
                                }
                            });
                        }
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    break;
                case 0x002:
                    try
                    {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        if (refreshLayout!=null)
                        {
                            refreshLayout.finishLoadmore();
                            refreshLayout.finishRefresh();
                        }
                        if (code == 200)
                        {
                            page=page+1;
                            OrderBean orderBean = new Gson().fromJson(result.body(), OrderBean.class);
                            if (data!=null)
                            {
                                data.addAll(orderBean.data);
                            }else {
                                return;
                            }
//                            final TiQianTuiFangOrderListAdapter orderListAdapter = new TiQianTuiFangOrderListAdapter(R.layout.item_order_tiqian_tuifang, data);
//                            recyclerView = view.findViewById(R.id.recyclerView);
//                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//                            recyclerView.setAdapter(orderListAdapter);
                            if (data.size() == 0)
                            {
                                orderListAdapter.setEmptyView(R.layout.empty, recyclerView);
                            }
                            orderListAdapter.notifyDataSetChanged();
//                            orderListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener()
//                            {
//                                @Override
//                                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position)
//                                {
//                                    switch (view.getId())
//                                    {
//                                        case R.id.tuikuan_apply:
//                                            //申请退款
//                                            ToastManager.show("申请退款");
//                                            Intent intent = new Intent(getActivity(), TuiKuanApplyActivity.class);
//                                            intent.putExtra("order_id", orderListAdapter.getItem(position).order_id + "");
//                                            startActivity(intent);
//                                            break;
//                                    }
//                                }
//                            });
                        }
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    break;
                case 0x003:
                    try
                    {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        String msg = jsonObject.getString("msg");

                        if (code == 200)
                        {
                            ToastManager.show(msg);
                            orderListAdapter.remove(item);
                            orderListAdapter.notifyDataSetChanged();
                           // MinSuApi.yiquxiao(getActivity(), 0x001, tokenId, 1,callBack);
                        } else if (code == 211)
                        {
                            ToastManager.show(msg);
                        }
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    break;
            }
        }

        @Override
        public void onFail(int what, Response<String> result)
        {

        }

        @Override
        public void onFinish(int what)
        {

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        MinSuApi.tiqianTuifang(getActivity(), 0x001, tokenId,1, callBack);
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        unbinder.unbind();
    }

}
