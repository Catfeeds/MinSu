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
import com.minsu.minsu.user.adapter.PassengerAdapter;
import com.minsu.minsu.user.bean.PassengerListBean;
import com.minsu.minsu.utils.StorageUtil;
import com.minsu.minsu.utils.ToastManager;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PassengerListActivity extends BaseActivity {


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
    private  String tokenId;
    @Override
    protected void processLogic() {
        MinSuApi.passengerList(this,0x001,tokenId,callBack);
    }

    @Override
    protected void setListener() {

        tokenId = StorageUtil.getTokenId(this);
        toolbarTitle.setText("常用旅客信息");
        ivLeft.setVisibility(View.VISIBLE);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("新增");
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PassengerListActivity.this, AddPassengerActivity.class);
                intent.putExtra("passenger_id","");
                intent.putExtra("type","1");
                startActivity(intent);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_passenger_list);
    }

    @Override
    protected Context getActivityContext() {
        return this;
    }
    private CallBack callBack=new CallBack() {
        @Override
        public void onSuccess(int what, Response<String> result) {
            switch (what){
                case 0x001:
                    PassengerListBean passengerListBean = new Gson().fromJson(result.body(), PassengerListBean.class);
                    if (passengerListBean.code==200){
                        final PassengerAdapter passengerAdapter = new PassengerAdapter(R.layout.item_passenger,passengerListBean.data);
                        recyclerView.setAdapter(passengerAdapter);
                        if (passengerListBean.data.size()==0){
                            passengerAdapter.setEmptyView(R.layout.empty,recyclerView);
                        }
                        passengerAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                            @Override
                            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                                switch (view.getId()){
                                    case R.id.passenger_edit:
                                        Intent intent = new Intent(PassengerListActivity.this, AddPassengerActivity.class);
                                        intent.putExtra("passenger_id",passengerAdapter.getItem(position).id+"");
                                        intent.putExtra("type","2");
                                        startActivity(intent);
                                        break;
                                    case R.id.passenger_delete:
                                        MinSuApi.deletePassenger(PassengerListActivity.this,0x002,tokenId,passengerAdapter.getItem(position).id,callBack);
                                        break;
                                }
                            }
                        });
                    }
                    break;
                case 0x002:
                    try {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        String msg = jsonObject.getString("msg");
                        if (code==200){
                            ToastManager.show(msg);
                            MinSuApi.passengerList(PassengerListActivity.this,0x001,tokenId,callBack);
                        }else if (code==211){
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
    protected void onResume() {
        super.onResume();
        MinSuApi.passengerList(this,0x001,tokenId,callBack);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
