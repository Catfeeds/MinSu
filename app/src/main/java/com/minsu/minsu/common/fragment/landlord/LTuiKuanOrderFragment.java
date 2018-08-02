package com.minsu.minsu.common.fragment.landlord;

import android.app.Activity;
import android.app.AlertDialog;
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
import com.minsu.minsu.common.fragment.landlord.adapter.FTuiKuanOrderListAdapter;
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

public class LTuiKuanOrderFragment extends BaseFragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    Unbinder unbinder;
    private View view;
    private String tokenId;
    List<OrderBean.Data> data = new ArrayList<>();
    private int page=1;
    private FTuiKuanOrderListAdapter orderListAdapter;
  private Activity activity;
    private int item;

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
              MinSuApi.tuikuanApply(activity, 0x001, tokenId, 1,callBack);
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
                MinSuApi.tuikuanApplyq(getActivity(), 0x001, tokenId, 1,callBack);
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
                MinSuApi.tuikuanApply(getActivity(), 0x004, tokenId, page,callBack);
                if (StorageUtil.getValue(getActivity(),"networks")!=null&&
                        StorageUtil.getValue(getActivity(),"networks").equals("no"))
                {
                    refreshlayout.finishLoadmore(3000,false);
                }
            }
        });
    }

    @Override
    public void onPause()
    {
        super.onPause();
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
                            }else {
                                return;
                            }
                            recyclerView=view.findViewById(R.id.recyclerView);
                            orderListAdapter = new FTuiKuanOrderListAdapter(R.layout.item_landlord_tuikuan_order, data);
                            if (recyclerView==null)
                            {
                                return;
                            }
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            recyclerView.setAdapter(orderListAdapter);
                            if (data.size() == 0) {
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
                                public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
                                    item = position;
                                    switch (view.getId()) {
                                        case R.id.refuse_tuikuan:
                                            MinSuApi.sureTuiKuan(getActivity(), 0x002, tokenId, orderListAdapter.getItem(position).order_id, -1, callBack);
                                            break;
                                        case R.id.agree_tuikuan:
                                            MinSuApi.sureTuiKuan(getActivity(), 0x003, tokenId, orderListAdapter.getItem(position).order_id, 1, callBack);
                                            break;
                                        case R.id.delete_tuikuan:
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
                                                    MinSuApi.deleteOrder(getActivity(), 0x009, tokenId, orderListAdapter.getItem(position).order_id, callBack);
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
                        } else if (code == 201) {
                            recyclerView=view.findViewById(R.id.recyclerView);
                            orderListAdapter = new FTuiKuanOrderListAdapter(R.layout.item_landlord_tuikuan_order, data);
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
                            MinSuApi.tuikuanApply(getActivity(), 0x001, tokenId,1, callBack);
                        } else if (code == 211) {
                            //ToastManager.show(msg);
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
                            MinSuApi.tuikuanApply(getActivity(), 0x001, tokenId, 1,callBack);
                        } else if (code == 211) {
                            //ToastManager.show(msg);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 0x004:

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

//                            final FTuiKuanOrderListAdapter orderListAdapter = new FTuiKuanOrderListAdapter(R.layout.item_landlord_tuikuan_order, data);
//                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//                            recyclerView.setAdapter(orderListAdapter);
                            if (data.size() == 0) {
                                orderListAdapter.setEmptyView(R.layout.empty, recyclerView);
                            }
                            orderListAdapter.notifyDataSetChanged();
//                            orderListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
//                                @Override
//                                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//                                    switch (view.getId()) {
//                                        case R.id.refuse_tuikuan:
//                                            MinSuApi.sureTuiKuan(getActivity(), 0x002, tokenId, orderListAdapter.getItem(position).order_id, -1, callBack);
//                                            break;
//                                        case R.id.agree_tuikuan:
//                                            MinSuApi.sureTuiKuan(getActivity(), 0x003, tokenId, orderListAdapter.getItem(position).order_id, 1, callBack);
//                                            break;
//                                    }
//                                }
//                            });
                        } else if (code == 201) {
                            //  ToastManager.show(msg);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
                case 0x009:

                    try {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        String msg = jsonObject.getString("msg");
                        if (code == 200) {
                            ToastManager.show(msg);
                            orderListAdapter.remove(item);
                            orderListAdapter.notifyDataSetChanged();
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
