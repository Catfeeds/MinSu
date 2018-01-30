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
import com.minsu.minsu.common.RoomDetailActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.minsu.minsu.R;
import com.minsu.minsu.api.MinSuApi;
import com.minsu.minsu.api.callback.CallBack;
import com.minsu.minsu.base.BaseActivity;
import com.minsu.minsu.houseResource.adapter.HouseListAdapter;
import com.minsu.minsu.houseResource.bean.HouseListBean;
import com.minsu.minsu.utils.StorageUtil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CollectActivity extends BaseActivity {


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
    private String tokenId;

    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {
        tokenId = StorageUtil.getTokenId(this);
        toolbarTitle.setText("我的收藏");
        ivLeft.setVisibility(View.VISIBLE);
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MinSuApi.myCollect(this, 0x001, tokenId, callBack);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_collect);
    }

    @Override
    protected Context getActivityContext() {
        return this;
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
                            HouseListBean houseListBean = new Gson().fromJson(result.body(), HouseListBean.class);
                            final HouseListAdapter houseListAdapter = new HouseListAdapter(R.layout.item_home_list, houseListBean.data,"collect");
                            recyclerView.setAdapter(houseListAdapter);
                            houseListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                    Intent intent = new Intent(CollectActivity.this, RoomDetailActivity.class);
                                    intent.putExtra("house_id", houseListAdapter.getItem(position).house_id);
                                    startActivity(intent);
                                }
                            });
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
