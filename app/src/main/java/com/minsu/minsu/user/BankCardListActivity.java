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
import com.minsu.minsu.user.adapter.BankListAdapter;
import com.minsu.minsu.user.bean.BankListBean;
import com.minsu.minsu.utils.StorageUtil;
import com.minsu.minsu.utils.ToastManager;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BankCardListActivity extends BaseActivity {


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
    private String type;

    @Override
    protected void processLogic() {
        MinSuApi.bankcardList(this, 0x001, tokenId, callBack);
    }

    @Override
    protected void setListener() {

        type = getIntent().getStringExtra("type");
        tokenId = StorageUtil.getTokenId(this);
        toolbarTitle.setText("银行卡");
        ivLeft.setVisibility(View.VISIBLE);
        ivRight.setVisibility(View.VISIBLE);
        ivRight.setImageResource(R.mipmap.add_01);
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BankCardListActivity.this, AddBankCardActivity.class));
            }
        });
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_bank_card_list);
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
                        String msg = jsonObject.getString("msg");
                        if (code == 200) {
                            final BankListBean bankListBean = new Gson().fromJson(result.body(), BankListBean.class);
                            final BankListAdapter bankListAdapter = new BankListAdapter(R.layout.item_bankcard, bankListBean.data);
                            recyclerView.setAdapter(bankListAdapter);
                            if (bankListBean.data.size() == 0) {
                                bankListAdapter.setEmptyView(R.layout.empty, recyclerView);
                            }
                            bankListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                    if (type==null)
                                    {
                                        return;
                                    }
                                    if (type.equals("tixian")){
                                        Intent intent = new Intent();
                                        //把返回数据存入Intent
                                        intent.putExtra("bank_name", bankListAdapter.getItem(position).bank_name);
                                        intent.putExtra("bank_code", bankListAdapter.getItem(position).bank_code);
                                        intent.putExtra("bank_id",bankListAdapter.getItem(position).id);
                                        BankCardListActivity.this.setResult(RESULT_OK, intent);
                                        //关闭Activity
                                        BankCardListActivity.this.finish();
                                    }
                                }
                            });
                        } else if (code == 289) {
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MinSuApi.bankcardList(this, 0x001, tokenId, callBack);
    }
}
