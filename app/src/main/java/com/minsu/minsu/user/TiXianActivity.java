package com.minsu.minsu.user;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
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
import com.minsu.minsu.utils.CheckUtil;
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
    private String is_ti;
    private String tx_money;
    private String jine;

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
                jine = tixianJine.getText().toString();
                if (jine ==null|| jine.equals(""))
                {
                    jine ="0";
                }
                Double obj1 = new Double(jine);
                if (obj1==0)
                {
                    ToastManager.show("请重新输入提现金额");
                    return;
                }
                if (tx_money==null|| jine ==null||obj1==null||obj1 < Double.parseDouble(tx_money)) {
                    ToastManager.show("请输入正确金额");
                    return;
                }

                if (obj1>Double.parseDouble(user_money))
                {
                    ToastManager.show("提现金额大于余额");
                    return;
                }
                if (is_ti.equals("1"))
                {
                     ToastManager.show("每月只能提现一次");
                     return;
                }
                if (id==null||id.equals("0"))
                {
                    ToastManager.show("请绑定银行卡");
                    return;
                }



                LayoutInflater liEmail = LayoutInflater.from(TiXianActivity.this);
                View emailView = liEmail.inflate(R.layout.email_dialog, null);

                final AlertDialog.Builder alertDialogBuilderEmail = new AlertDialog.Builder(
                        TiXianActivity.this);

                // set prompts.xml to alertdialog builder
                alertDialogBuilderEmail.setView(emailView);
                // create alert dialog
                final AlertDialog alertDialogEmail = alertDialogBuilderEmail.create();
                final EditText EmailInputName = emailView.findViewById(R.id.input_name);
                final TextView EmailCancel = emailView.findViewById(R.id.cancel);
                final TextView EmailSure = emailView.findViewById(R.id.sure);
                EmailInputName.setText("您确定要提现吗");
                EmailInputName.setTextColor(getResources().getColor(R.color.color_333));
                EmailInputName.setEnabled(false);
                EmailCancel.setText("取消提现");
                EmailSure.setText("确认提现");

                EmailCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialogEmail.dismiss();
                    }
                });
                EmailSure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MinSuApi.tixianApply(TiXianActivity.this, 0x002, tokenId, id, jine, tx_bl, callBack);
                    }
                });
                alertDialogBuilderEmail
                        .setCancelable(true);
                // show it
                alertDialogEmail.show();


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
                            confirmTixian.setVisibility(View.VISIBLE);
                            JSONObject data = jsonObject.getJSONObject("data");
                            JSONObject jsonObject1 = new JSONObject(data.toString());
                            String bank_name = jsonObject1.getString("bank_name");

                            id = jsonObject1.getString("id");
                            String bank_code = jsonObject1.getString("bank_code");

                            tx_bl = jsonObject1.getString("tx_bl");
                            tx_money = jsonObject1.getString("tx_money");
                            is_ti=jsonObject1.getString("month");
                            user_money = jsonObject1.getString("user_money");
                            ketixian.setText(user_money + "(最低提现金额为￥" + tx_money + ")");
                            shouxufei.setText("每笔提现将收取提现金额的" + tx_bl + "作为手续费");
                          if (id.equals("0"))
                          {
                              tixianAccount.setText("未绑定银行卡");
                          }else {
                              tixianAccount.setText(bank_name+bank_code);
                          }
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
                case 0x003:
                    try
                    {
                        JSONObject object=new JSONObject(result.body());
                        if (object.getString("code").equals("200"))
                        {
                            ToastManager.show("提现成功");
                            MinSuApi.tixianPage(TiXianActivity.this, 0x001, tokenId, callBack);
                        }
                    } catch (JSONException e)
                    {
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
            id = data.getExtras().getString("bank_id");
            tixianAccount.setText(bank_name + bank_code);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tixian_all:
                if (Double.parseDouble(user_money)<=0||id==null)
                {
                    ToastManager.show("无法提现");
                    return;
                }
                MinSuApi.tixianApply(TiXianActivity.this,0x003,tokenId,id,user_money,tx_bl,callBack);
                break;
            case R.id.ll_bankcard:
                startActivity(new Intent(TiXianActivity.this, BankCardListActivity.class));
                break;
            case R.id.confirm_tixian:
                    double keyong = Double.parseDouble(user_money);
                    String money= tixianJine.getText().toString();
                    double nwe=Double.parseDouble(money);
                    if (keyong>0&&keyong>=nwe&&id!=null)
                    {
                        MinSuApi.tixianApply(TiXianActivity.this,0x003,tokenId,id,money,tx_bl,callBack);

                    }else {
                        ToastManager.show("无法提现");
                        return;
                    }

                break;
        }
    }
}
