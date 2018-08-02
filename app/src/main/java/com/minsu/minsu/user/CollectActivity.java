package com.minsu.minsu.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.lzy.okgo.model.Response;
import com.minsu.minsu.R;
import com.minsu.minsu.api.MinSuApi;
import com.minsu.minsu.api.callback.CallBack;
import com.minsu.minsu.base.BaseActivity;
import com.minsu.minsu.common.RoomDetailActivity;
import com.minsu.minsu.houseResource.adapter.HouseListAdapter;
import com.minsu.minsu.houseResource.bean.HouseListBean;
import com.minsu.minsu.utils.StorageUtil;
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

public class CollectActivity extends BaseActivity
{


    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.iv_left)
    ImageView ivLeft;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.empty)
    ImageView empty;
    private String tokenId;
    private HouseListBean houseListBean;
    private HouseListAdapter houseListAdapter;
    private int posit;
    private List<HouseListBean.Data> data=new ArrayList<>();
    private int page=1;

    @Override
    protected void processLogic()
    {

    }

    @Override
    protected void setListener()
    {
        tokenId = StorageUtil.getTokenId(this);
        toolbarTitle.setText("我的收藏");
        ivLeft.setVisibility(View.VISIBLE);
        ivLeft.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener()
        {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout)
            {
                MinSuApi.myCollect(CollectActivity.this, 0x003, tokenId,page, callBack);
                if (StorageUtil.getValue(CollectActivity.this,"networks")!=null&&
                        StorageUtil.getValue(CollectActivity.this,"networks").equals("no"))
                {
                    refreshlayout.finishLoadmore(3000,false);
                }
            }
        });
        refreshLayout.setOnRefreshListener(new OnRefreshListener()
        {
            @Override
            public void onRefresh(RefreshLayout refreshlayout)
            {
                MinSuApi.myCollect(CollectActivity.this, 0x001, tokenId,1, callBack);
                if (StorageUtil.getValue(CollectActivity.this,"networks")!=null&&
                        StorageUtil.getValue(CollectActivity.this,"networks").equals("no"))
                {
                    refreshlayout.finishRefresh(3000,false);
                }
            }
        });
        MinSuApi.myCollect(this, 0x001, tokenId,1, callBack);
    }

    @Override
    protected void loadViewLayout()
    {
        setContentView(R.layout.activity_collect);
    }

    @Override
    protected Context getActivityContext()
    {
        return this;
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
                            refreshLayout.finishRefresh();
                        }
                        if (code == 200)
                        {
                            page=page+1;
                            houseListBean = new Gson().fromJson(result.body(), HouseListBean.class);
//                            if(houseListBean.data.size()==0){
//                                empty.setVisibility(View.VISIBLE);
//                            }else {
//                                empty.setVisibility(View.GONE);
//                                recyclerView.setVisibility(View.VISIBLE);
//                            }
                            for (int i = 0; i < houseListBean.data.size(); i++)
                            {
                                houseListBean.data.get(i).collect = 1;
                            }
                            if (data!=null)
                            {
                                data.clear();
                                data.addAll(houseListBean.data);
                            }
                            houseListAdapter = new HouseListAdapter(R.layout.item_home_list, data, "collect");
                            recyclerView.setAdapter(houseListAdapter);
                            //houseListAdapter.addData(houseListBean.data);
                            if (houseListBean.data.size() == 0) {
                                houseListAdapter.setEmptyView(R.layout.empty, recyclerView);
                            }
                            houseListAdapter.notifyDataSetChanged();
                            houseListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
                            {
                                @Override
                                public void onItemClick(BaseQuickAdapter adapter, View view, int position)
                                {
                                    Intent intent = new Intent(CollectActivity.this, RoomDetailActivity.class);
                                    String hourse_is = houseListAdapter.getItem(position).house_id;
                                    intent.putExtra("house_id", hourse_is);
                                    startActivity(intent);
                                }
                            });


                            houseListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener()
                            {
                                @Override
                                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position)
                                {
                                    posit = position;
                                    switch (view.getId())
                                    {
                                        case R.id.focus_room:
                                            MinSuApi.cancelCollect(0x002, tokenId, houseListBean.data.get(position).house_id, callBack);
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
                    JSONObject jsonObject = null;
                    try
                    {
                        jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        if (code == 200)
                        {
                            //houseListBean.data.remove(posit);
                            houseListAdapter.remove(posit);
                            houseListAdapter.notifyDataSetChanged();
                            if(houseListBean.data.size()==0){
                                houseListAdapter.setEmptyView(R.layout.empty, recyclerView);
                            }
                            houseListAdapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                    break;
                case 0x003:

                    try
                    {
                        JSONObject jsonObject1 = new JSONObject(result.body());
                        int code = jsonObject1.getInt("code");
                        if (refreshLayout!=null)
                        {
                            refreshLayout.finishRefresh();
                        }
                        if (code == 200)
                        {
                            page=page+1;
                            houseListBean = new Gson().fromJson(result.body(), HouseListBean.class);
//                            if(houseListBean.data.size()==0){
//                                empty.setVisibility(View.VISIBLE);
//                            }else {
//                                empty.setVisibility(View.GONE);
//                                recyclerView.setVisibility(View.VISIBLE);
//                            }
                            for (int i = 0; i < houseListBean.data.size(); i++)
                            {
                                houseListBean.data.get(i).collect = 1;
                            }
                            if (data!=null)
                            {
                                data.clear();
                                data.addAll(houseListBean.data);
                            }
                            houseListAdapter = new HouseListAdapter(R.layout.item_home_list, data, "collect");
                            recyclerView.setAdapter(houseListAdapter);
                            //houseListAdapter.addData(houseListBean.data);
                            if (houseListBean.data.size() == 0) {
                                houseListAdapter.setEmptyView(R.layout.empty, recyclerView);
                            }
                            houseListAdapter.notifyDataSetChanged();
                            houseListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
                            {
                                @Override
                                public void onItemClick(BaseQuickAdapter adapter, View view, int position)
                                {
                                    Intent intent = new Intent(CollectActivity.this, RoomDetailActivity.class);
                                    String hourse_is = houseListAdapter.getItem(position).house_id;
                                    intent.putExtra("house_id", hourse_is);
                                    startActivity(intent);
                                }
                            });


                            houseListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener()
                            {
                                @Override
                                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position)
                                {
                                    posit = position;
                                    switch (view.getId())
                                    {
                                        case R.id.focus_room:
                                            MinSuApi.cancelCollect(0x002, tokenId, houseListBean.data.get(position).house_id, callBack);
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
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
