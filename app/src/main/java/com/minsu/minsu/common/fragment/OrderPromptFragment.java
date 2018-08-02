package com.minsu.minsu.common.fragment;

import android.app.AlertDialog;
import android.content.Context;
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
import com.minsu.minsu.common.bean.Isvisible;
import com.minsu.minsu.common.bean.OrderBean2;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.minsu.minsu.R;
import com.minsu.minsu.api.MinSuApi;
import com.minsu.minsu.api.callback.CallBack;
import com.minsu.minsu.base.BaseFragment;
import com.minsu.minsu.common.adapter.OrderPromptAdapter;
import com.minsu.minsu.utils.StorageUtil;
import com.minsu.minsu.utils.ToastManager;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by hpc on 2017/12/6.
 */

public class OrderPromptFragment extends BaseFragment
{
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    Unbinder unbinder;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private View view;
    private String tokenId;
    private int page = 1;
    private List<OrderBean2> datas = new ArrayList<>();
    private OrderPromptAdapter orderPromptAdapter;
    private int item;
    private String isfd;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container)
    {
        view = inflater.inflate(R.layout.fragment_system, container, false);
        return view;
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        super.setUserVisibleHint(isVisibleToUser);
        if (getActivity() != null)
        {

            isfd = StorageUtil.getValue(getActivity(), "isfd");
            if (recyclerView != null)
            {
                if (isfd.equals("yes"))
                {
                   MinSuApi.orderfangdong(getActivity(),0x001,tokenId,page,callBack);
                } else
                {
                    recyclerView.setVisibility(View.VISIBLE);
                    MinSuApi.orderPrompt(getActivity(), 0x001, tokenId, page, callBack);
                }

            }
        }
    }

    public void shuxin()
    {
        try
        {
            isfd = StorageUtil.getValue(getActivity(), "isfd");
        }catch (Exception e)
        {

        }
        if (recyclerView != null)
        {
            if (isfd!=null&&isfd.equals("yes"))
            {
                MinSuApi.orderfangdong(getActivity(),0x001,tokenId,page,callBack);
            } else
            {
                recyclerView.setVisibility(View.VISIBLE);
                MinSuApi.orderPrompt(getActivity(), 0x001, tokenId, page, callBack);
            }

        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(Isvisible isvisible)
    {
        if (isvisible == null)
        {
            return;
        }
        isfd = StorageUtil.getValue(getActivity(), "isfd");
        if (recyclerView != null)
        {
            if (isfd.equals("yes"))
            {
                MinSuApi.orderfangdong(getActivity(),0x001,tokenId,page,callBack);
            } else
            {
                recyclerView.setVisibility(View.VISIBLE);
                MinSuApi.orderPrompt(getActivity(), 0x001, tokenId, page, callBack);
            }

        }
    }


//    @Override
//    public void onHiddenChanged(boolean hidden)
//    {
//        super.onHiddenChanged(hidden);
//        String isfd = StorageUtil.getValue(getActivity(), "isfd");
//        if (recyclerView != null)
//        {
//            if (isfd.equals("yes"))
//            {
//                recyclerView.setVisibility(View.GONE);
//            } else
//            {
//                recyclerView.setVisibility(View.VISIBLE);
//                MinSuApi.orderPrompt(getActivity(), 0x001, tokenId, page, callBack);
//            }
//
//        }
//    }

    @Override
    protected void initListener()
    {

        tokenId = StorageUtil.getTokenId(getActivity());
        StorageUtil.setKeyValue(getActivity(),"ttt","");
    }


    @Override
    public void onStart()
    {
        super.onStart();
    }

    @Override
    protected void initData()
    {
         isfd = StorageUtil.getValue(getActivity(), "isfd");
        recyclerView = view.findViewById(R.id.recyclerView);
        refreshLayout.setOnRefreshListener(new OnRefreshListener()
        {
            @Override
            public void onRefresh(RefreshLayout refreshlayout)
            {
                isfd = StorageUtil.getValue(getActivity(), "isfd");
                if (recyclerView != null)
                {
                    if (isfd.equals("yes"))
                    {
                        MinSuApi.orderfangdong(getActivity(),0x001,tokenId,page,callBack);
                    } else
                    {
                        recyclerView.setVisibility(View.VISIBLE);
                        MinSuApi.orderPrompt(getActivity(), 0x001, tokenId, page, callBack);
                    }

                }
                if (StorageUtil.getValue(getActivity(),"networks")!=null&&
                        StorageUtil.getValue(getActivity(),"networks").equals("no"))
                {
                    refreshlayout.finishRefresh(3000,false);
                }
            }
        });
        StorageUtil.setKeyValue(getActivity(),"selectview","22");
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener()
        {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout)
            {
                isfd = StorageUtil.getValue(getActivity(), "isfd");
                if (recyclerView != null)
                {
                    if (isfd.equals("yes"))
                    {
                        MinSuApi.orderfangdong(getActivity(),0x001,tokenId,page,callBack);
                    } else
                    {
                        recyclerView.setVisibility(View.VISIBLE);
                        MinSuApi.orderPrompt(getActivity(), 0x001, tokenId, page, callBack);
                    }

                }
                if (StorageUtil.getValue(getActivity(),"networks")!=null&&
                        StorageUtil.getValue(getActivity(),"networks").equals("no"))
                {
                    refreshlayout.finishLoadmore(3000,false);
                }
            }

        });
        isfd = StorageUtil.getValue(getActivity(), "isfd");
        if (recyclerView != null)
        {
            if (isfd.equals("yes"))
            {
                MinSuApi.orderfangdong(getActivity(),0x001,tokenId,page,callBack);
            } else
            {
                recyclerView.setVisibility(View.VISIBLE);
                MinSuApi.orderPrompt(getActivity(), 0x001, tokenId, page, callBack);
            }

        }
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
                        String msg = jsonObject.getString("msg");
                        String data = jsonObject.getString("data");
                        if (refreshLayout != null)
                        {
                            refreshLayout.finishLoadmore();
                            refreshLayout.finishRefresh();
                        }
                        if (code == 200)
                        {
                            page =1;
                            OrderBean2[] orderBean = new Gson().fromJson(data, OrderBean2[].class);
                            List list = Arrays.asList(orderBean);
                            List<OrderBean2> bean2s = new ArrayList<>(list);
                            if (datas != null)
                            {
                                datas.clear();
                                datas.addAll(bean2s);
                            } else
                            {
                                return;
                            }
                            orderPromptAdapter = new OrderPromptAdapter(R.layout.item_order_prompt, datas);
                            recyclerView = view.findViewById(R.id.recyclerView);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            recyclerView.setAdapter(orderPromptAdapter);
                            if (datas.size() == 0)
                            {
                                orderPromptAdapter.setEmptyView(R.layout.empty, recyclerView);
                            }
                          orderPromptAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener()
                          {
                              @Override
                              public boolean onItemLongClick(BaseQuickAdapter adapter, View view, final int position)
                              {
                                  item = position;
                                  LayoutInflater li = LayoutInflater.from(getActivity());
                                  View promptsView = li.inflate(R.layout.delete_message, null);
                                  promptsView.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));
                                  final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                                     TextView view1=promptsView.findViewById(R.id.delete_t);
                                     view1.setText("删除订单提示");
                                  // set prompts.xml to alertdialog builder
                                  alertDialogBuilder.setView(promptsView);
                                  // create alert dialog
                                  final AlertDialog alertDialog = alertDialogBuilder.create();
                                  promptsView.setOnClickListener(new View.OnClickListener()
                                  {
                                      @Override
                                      public void onClick(View view)
                                      {
                                          MinSuApi.deleteorderprompt(getActivity(), 3, tokenId, orderPromptAdapter.getItem(position).getLog_id(), callBack);
                                          alertDialog.dismiss();
                                      }
                                  });
                                  alertDialogBuilder
                                          .setCancelable(true);
                                  // show it
                                  alertDialog.show();
                                  return false;
                              }
                          });

                        } else if (code == 111)
                        {
                            ToastManager.show(msg);
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
                        String data = jsonObject.getString("data");
                        if (refreshLayout != null)
                        {
                            refreshLayout.finishLoadmore();
                            refreshLayout.finishRefresh();
                        }
                        if (code == 200)
                        {
                            page = page + 1;
                            OrderBean2[] orderBean = new Gson().fromJson(data, OrderBean2[].class);
                            List list = Arrays.asList(orderBean);
                            List<OrderBean2> bean2s = new ArrayList<>(list);
                            if (datas != null)
                            {
                                datas.addAll(bean2s);
                            } else
                            {
                                return;
                            }
//                            OrderPromptAdapter orderPromptAdapter = new OrderPromptAdapter(R.layout.item_order_prompt, datas);
//                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//                            recyclerView.setAdapter(orderPromptAdapter);
                            if (datas.size() == 0)
                            {
                                orderPromptAdapter.setEmptyView(R.layout.empty, recyclerView);
                            }

                        } else if (code == 111)
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
                        JSONObject object=new JSONObject(result.body());
                        if (object.getString("code").equals("200"))
                        {
                            orderPromptAdapter.remove(item);
                            orderPromptAdapter.notifyDataSetChanged();
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
        try
        {
            EventBus.getDefault().register(this);
        } catch (Exception e)
        {

        }
        return rootView;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        unbinder.unbind();
    }

}
