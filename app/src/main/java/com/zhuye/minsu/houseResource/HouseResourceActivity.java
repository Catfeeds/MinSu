package com.zhuye.minsu.houseResource;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.model.Response;
import com.zhuye.minsu.R;
import com.zhuye.minsu.api.MinSuApi;
import com.zhuye.minsu.api.callback.CallBack;
import com.zhuye.minsu.base.BaseActivity;
import com.zhuye.minsu.houseResource.adapter.HouseListAdapter;
import com.zhuye.minsu.houseResource.bean.HouseListBean;
import com.zhuye.minsu.utils.StorageUtil;
import com.zhuye.minsu.utils.ToastManager;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HouseResourceActivity extends BaseActivity implements View.OnClickListener {

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
    private String tokenId;

    @Override
    protected void processLogic() {
        MinSuApi.myHouseResource(this, 0x001, tokenId, callBack);
    }

    @Override
    protected void setListener() {
        tokenId = StorageUtil.getTokenId(this);
        toolbarTitle.setText("我的房源");
        ivLeft.setVisibility(View.VISIBLE);
        ivRight.setVisibility(View.VISIBLE);
        ivRight.setImageResource(R.mipmap.anim_heart);
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ivRight.setOnClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_house_resource);
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
                        String msg = jsonObject.getString("msg");
                        int code = jsonObject.getInt("code");
                        if (code == 289) {
                            ToastManager.show(msg);
                        } else if (code == 101) {
                            ToastManager.show(msg);
                        } else if (code == 200) {
                            HouseListBean houseListBean = new Gson().fromJson(result.body(), HouseListBean.class);
                            HouseListAdapter houseListAdapter = new HouseListAdapter(R.layout.item_home_list, houseListBean.data);
                            recyclerView.setAdapter(houseListAdapter);
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_right:
                startActivity(new Intent(this, AddHouseActivity.class));
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
