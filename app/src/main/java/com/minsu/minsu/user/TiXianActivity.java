package com.minsu.minsu.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.okgo.model.Response;
import com.minsu.minsu.R;
import com.minsu.minsu.api.MinSuApi;
import com.minsu.minsu.api.callback.CallBack;
import com.minsu.minsu.base.BaseActivity;
import com.minsu.minsu.utils.StorageUtil;
import com.minsu.minsu.utils.ToastManager;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TiXianActivity extends BaseActivity implements View.OnClickListener {


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
    @BindView(R.id.tag)
    TextView tag;
    @BindView(R.id.tag1)
    TextView tag1;
    @BindView(R.id.ketixian)
    TextView ketixian;
    @BindView(R.id.tixian_all)
    TextView tixianAll;
    @BindView(R.id.ll_bankcard)
    RelativeLayout llBankcard;
    @BindView(R.id.confirm_tixian)
    TextView confirmTixian;
    @BindView(R.id.shouxufei)
    TextView shouxufei;
    @BindView(R.id.tixian_account)
    TextView tixianAccount;
    @BindView(R.id.tixian_jine)
    EditText tixianJine;
    private String tokenId;
    private String user_money;
    private String id;
    private String tx_bl;
    private static final int REQUEST_CODE = 0x001;
    private String bank_name;
    private String bank_code;

    @Override
    protected void processLogic() {
        MinSuApi.tixianPage(this, 0x001, tokenId, callBack);
    }

    @Override
    protected void setListener() {

        tokenId = StorageUtil.getTokenId(this);
        toolbarTitle.setText("提现");
        ivLeft.setVisibility(View.VISIBLE);
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tixianAll.setOnClickListener(this);
        llBankcard.setOnClickListener(this);
        confirmTixian.setOnClickListener(this);
        tixianAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tixianJine.setText(user_money);
            }
        });
        confirmTixian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String jine = tixianJine.getText().toString();
                Double obj1 = new Double(jine);
                if (obj1 < 100) {
                    ToastManager.show("提现金额小于100");
                    return;
                }
                MinSuApi.tixianApply(TiXianActivity.this, 0x002, tokenId, id, jine, tx_bl, callBack);
            }
        });
        tixianAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TiXianActivity.this, BankCardListActivity.class);
                intent.putExtra("type", "tixian");
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_ti_xian);
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
                            JSONObject data = jsonObject.getJSONObject("data");
                            JSONObject jsonObject1 = new JSONObject(data.toString());
                            String bank_name = jsonObject1.getString("bank_name");

                            id = jsonObject1.getString("id");
                            String bank_code = jsonObject1.getString("bank_code");

                            tx_bl = jsonObject1.getString("tx_bl");
                            String tx_money = jsonObject1.getString("tx_money");

                            user_money = jsonObject1.getString("user_money");
                            ketixian.setText(user_money + "(最低提现金额为￥" + tx_money + ")");
                            shouxufei.setText("每笔提现将收取提现金额的" + tx_bl + "作为手续费");
                            tixianAccount.setText(bank_name + bank_code);
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
                            finish();
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && REQUEST_CODE == requestCode) {
            bank_name = data.getExtras().getString("bank_name");
            bank_code = data.getExtras().getString("bank_code");
            tixianAccount.setText(bank_name + bank_code);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tixian_all:

                break;
            case R.id.ll_bankcard:
                startActivity(new Intent(TiXianActivity.this, BankCardListActivity.class));
                break;
            case R.id.confirm_tixian:
//                    MinSuApi.tixianApply(TiXianActivity.this,0x001,tokenId,);
                break;
        }
    }
}
