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
import com.minsu.minsu.common.CommentActivity;
import com.minsu.minsu.common.CommentSubmitActivity;
import com.minsu.minsu.common.OrderSubmitActivity;
import com.minsu.minsu.common.RoomDetailActivity;
import com.minsu.minsu.common.bean.OrderBean;
import com.minsu.minsu.houseResource.OrderDetailActvity;
import com.minsu.minsu.user.TuiFangApplyActivity;
import com.minsu.minsu.user.TuiKuanApplyActivity;
import com.minsu.minsu.user.adapter.OrderListAdapter;
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

public class AllOrderFragment extends BaseFragment
{
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    Unbinder unbinder;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private String tokenId;
    List<OrderBean.Data> data = new ArrayList<>();
    private List<OrderBean.Data> datas;
   private int page=1;
    private int item;
    private OrderListAdapter orderListAdapter;
    private TextView pingjia;
    private View view;
 private Activity activity;
    @Override
    public void onAttach(Activity context)
    {
        this.activity= (Activity) context;
        super.onAttach(context);
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container)
    {
        view = inflater.inflate(R.layout.fragment_all_order, container, false);
        return view;
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
                MinSuApi.allMyOrder(getActivity(), 0x001, tokenId, 1,callBack);
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
                MinSuApi.allMyOrder(getActivity(), 0x004, tokenId, page,callBack);
                if (StorageUtil.getValue(getActivity(),"networks")!=null&&
                        StorageUtil.getValue(getActivity(),"networks").equals("no"))
                {
                    refreshlayout.finishLoadmore(3000,false);
                }
            }
        });
        refreshLayout.autoRefresh();

    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser)
        {
            if (activity!=null)
            {
                MinSuApi.allMyOrder(activity, 0x001, tokenId,1, callBack);
            }

        }
    }
    @Override
    protected void initData()
    {
       // MinSuApi.allMyOrder(getActivity(), 0x001, tokenId,1, callBack);
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
                            if (data != null)
                            {
                                data.clear();
                                data.addAll(orderBean.data);
                            } else
                            {
                                return;
                            }
                            showlist(data);
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
                        String msg = jsonObject.getString("msg");
                        if (code == 200)
                        {
                            ToastManager.show(msg);
                            MinSuApi.allMyOrder(getActivity(), 0x001, tokenId,1, callBack);
                        } else if (code == 211)
                        {
                            ToastManager.show(msg);
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
                            MinSuApi.allMyOrder(getActivity(), 0x001, tokenId,1, callBack);
                        } else if (code == 211)
                        {
                            ToastManager.show(msg);
                        }
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    break;
                case 0x004:
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
                            if (data != null)
                            {
                                data.addAll(orderBean.data);
                                orderListAdapter.notifyDataSetChanged();
                            } else
                            {
                                return;
                            }
                           // showlist(data);
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

    private void showlist(final List<OrderBean.Data> data)
    {
        recyclerView=view.findViewById(R.id.recyclerView);
        orderListAdapter = new OrderListAdapter(R.layout.item_order, data);
        if (recyclerView==null)
        {
            return;
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
            public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position)
            {
                item = position;
                switch (view.getId())
                {
                    case R.id.order_pay:
                        Intent intent4 = new Intent(getActivity(), OrderSubmitActivity.class);
                        intent4.putExtra("order_id", orderListAdapter.getItem(position).order_id + "");
                        startActivityForResult(intent4,3);
                        break;
                    case R.id.tiqian_tuifang:
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


                        mCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alertDialog.dismiss();
                            }
                        });
                        mSure.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent1 = new Intent(getActivity(), TuiFangApplyActivity.class);
                                intent1.putExtra("order_id", orderListAdapter.getItem(item).order_id + "");
                                startActivityForResult(intent1,2);
                                alertDialog.dismiss();
                            }
                        });
                        alertDialogBuilder
                                .setCancelable(true);
                        // show it
                        alertDialog.show();

                        break;
                    case R.id.order_cancel:
                        LayoutInflater lis = LayoutInflater.from(getActivity());
                        View promptsViews = lis.inflate(R.layout.tuikuan_dialog, null);

                        final AlertDialog.Builder alertDialogBuilders = new AlertDialog.Builder(getActivity());

                        // set prompts.xml to alertdialog builder
                        alertDialogBuilders.setView(promptsViews);
                        // create alert dialog
                        final AlertDialog alertDialogs = alertDialogBuilders.create();
                        final TextView inputNames = promptsViews.findViewById(R.id.input_name);
                        final TextView mCancels = promptsViews.findViewById(R.id.cancel);
                        final TextView mSures = promptsViews.findViewById(R.id.sure);
                        inputNames.setText("您确定要取消吗");

                        mCancels.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alertDialogs.dismiss();
                            }
                        });
                        mSures.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                MinSuApi.cancelOrder(getActivity(), 0x002, tokenId, orderListAdapter.getItem(item).order_id, callBack);
                                alertDialogs.dismiss();
                            }
                        });
                        alertDialogBuilders
                                .setCancelable(true);
                        // show it
                        alertDialogs.show();
                        break;
                    case R.id.order_delete:
                        LayoutInflater liss = LayoutInflater.from(getActivity());
                        View promptsViewss = liss.inflate(R.layout.tuikuan_dialog, null);

                        final AlertDialog.Builder alertDialogBuilderss = new AlertDialog.Builder(getActivity());

                        // set prompts.xml to alertdialog builder
                        alertDialogBuilderss.setView(promptsViewss);
                        // create alert dialog
                        final AlertDialog alertDialogss = alertDialogBuilderss.create();
                        final TextView inputNamess = promptsViewss.findViewById(R.id.input_name);
                        final TextView mCancelss = promptsViewss.findViewById(R.id.cancel);
                        final TextView mSuress = promptsViewss.findViewById(R.id.sure);
                        inputNamess.setText("您确定要删除吗");

                        mCancelss.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alertDialogss.dismiss();
                            }
                        });
                        mSuress.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                MinSuApi.deleteOrder(getActivity(), 0x003, tokenId, orderListAdapter.getItem(position).order_id, callBack);
                                alertDialogss.dismiss();
                            }
                        });
                        alertDialogBuilderss
                                .setCancelable(true);
                        // show it
                        alertDialogss.show();

                        break;
                    case R.id.yudin_again:
                        Intent intent = new Intent(getActivity(), RoomDetailActivity.class);
                        intent.putExtra("house_id", orderListAdapter.getItem(position).house_id + "");
                        startActivity(intent);
                        break;
                    case R.id.pingjia:
                       // ToastManager.show("评价");
                        pingjia = (TextView) orderListAdapter.getViewByPosition(recyclerView,position, R.id.pingjia);
                        if (pingjia.getText().equals("评价"))
                        {
                            Intent intent3 = new Intent(getActivity(), CommentSubmitActivity.class);
                            intent3.putExtra("houseId", orderListAdapter.getItem(position).house_id + "");
                            intent3.putExtra("order_id",orderListAdapter.getItem(position).order_id);
                            startActivityForResult(intent3,10);
                        }else {
                            Intent intent7 = new Intent(getActivity(), CommentActivity.class);
                            intent7.putExtra("house_id", orderListAdapter.getItem(position).house_id+"");
                            startActivity(intent7);
                        }


//                        Intent intent3 = new Intent(getActivity(), CommentSubmitActivity.class);
//                        intent3.putExtra("houseId", orderListAdapter.getItem(position).house_id + "");
//                        startActivityForResult(intent3,10);
                        break;
                    case R.id.tuikuan_apply:
                        //申请退款
                        LayoutInflater l = LayoutInflater.from(getActivity());
                        View promptsVie = l.inflate(R.layout.tuikuan_dialog, null);

                        final AlertDialog.Builder alertDialogBuilde = new AlertDialog.Builder(getActivity());

                        // set prompts.xml to alertdialog builder
                        alertDialogBuilde.setView(promptsVie);
                        // create alert dialog
                        final AlertDialog alertDialo= alertDialogBuilde.create();
                        final TextView inputNam = promptsVie.findViewById(R.id.input_name);
                        final TextView mCance = promptsVie.findViewById(R.id.cancel);
                        final TextView mSur = promptsVie.findViewById(R.id.sure);

                        inputNam.setText("您确定要退款吗");
                        mCance.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alertDialo.dismiss();
                            }
                        });
                        mSur.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent2 = new Intent(getActivity(), TuiKuanApplyActivity.class);
                                intent2.putExtra("order_id", orderListAdapter.getItem(item).order_id + "");
                                startActivityForResult(intent2,1);
                                alertDialo.dismiss();
                            }
                        });
                        alertDialogBuilde
                                .setCancelable(true);
                        // show it
                        alertDialo.show();
                        break;
                    case R.id.order_dells:

                        LayoutInflater lisss = LayoutInflater.from(getActivity());
                        View promptsViewsss = lisss.inflate(R.layout.tuikuan_dialog, null);

                        final AlertDialog.Builder alertDialogBuildersss = new AlertDialog.Builder(getActivity());

                        // set prompts.xml to alertdialog builder
                        alertDialogBuildersss.setView(promptsViewsss);
                        // create alert dialog
                        final AlertDialog alertDialogsss = alertDialogBuildersss.create();
                        final TextView inputNamesss = promptsViewsss.findViewById(R.id.input_name);
                        final TextView mCancelsss = promptsViewsss.findViewById(R.id.cancel);
                        final TextView mSuresss = promptsViewsss.findViewById(R.id.sure);
                        inputNamesss.setText("您确定要删除吗");

                        mCancelsss.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alertDialogsss.dismiss();
                            }
                        });
                        mSuresss.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                MinSuApi.deleteOrder(getActivity(), 0x003, tokenId, orderListAdapter.getItem(position).order_id, callBack);
                                alertDialogsss.dismiss();
                            }
                        });
                        alertDialogBuildersss
                                .setCancelable(true);
                        // show it
                        alertDialogsss.show();
                        break;
                }
            }
        });
        recyclerView.setAdapter(orderListAdapter);
    }


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
        String ispj=StorageUtil.getValue(getActivity(),"ispj");
        StorageUtil.setKeyValue(getActivity(),"ispj","no");
        if (ispj!=null&&ispj.equals("yes"))
        {
            pingjia.setText("查看评价");
        }else {
            //pingjia.setText("评价");
        }
        if (requestCode==3)
        {

        }else {
            MinSuApi.allMyOrder(getActivity(), 0x001, tokenId,1, callBack);
        }
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        unbinder.unbind();
    }
}
