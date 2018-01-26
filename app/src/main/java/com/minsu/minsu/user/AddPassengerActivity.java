package com.minsu.minsu.user;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

public class AddPassengerActivity extends BaseActivity {


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
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.rl_name)
    RelativeLayout rlName;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.rl_type)
    RelativeLayout rlType;
    @BindView(R.id.tv_number)
    TextView tvNumber;
    @BindView(R.id.rl_number)
    RelativeLayout rlNumber;
    @BindView(R.id.tv_passenger_type)
    TextView tvPassengerType;
    @BindView(R.id.rl_passenger_type)
    RelativeLayout rlPassengerType;
    @BindView(R.id.confirm)
    TextView confirm;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_number)
    EditText etNumber;
    private String tokenId;
    private  String passenger_id;

    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {
        toolbarTitle.setText("旅客编辑");
        ivLeft.setVisibility(View.VISIBLE);
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tokenId = StorageUtil.getTokenId(this);
        passenger_id = getIntent().getStringExtra("passenger_id");
        if (!passenger_id.equals("")&&passenger_id!=null){
            MinSuApi.editPassengerPage(AddPassengerActivity.this,0x002,tokenId,Integer.parseInt(passenger_id),callBack);
        }
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    tvName.setVisibility(View.VISIBLE);
                } else {
                    tvName.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    tvNumber.setVisibility(View.VISIBLE);
                } else {
                    tvNumber.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        rlType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mEtNumber = etNumber.getText().toString();
                String mEtName = etName.getText().toString();
                if (!passenger_id.equals("")&&passenger_id!=null){
                    MinSuApi.editPassenger(AddPassengerActivity.this,0x003,tokenId,Integer.parseInt(passenger_id),mEtName,"身份证",mEtNumber,"成人",callBack);
                }else{
                    MinSuApi.addPassenger(AddPassengerActivity.this,0x001,tokenId,mEtName,"身份证",mEtNumber,"成人",callBack);
                }
            }
        });
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_add_passenger);
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
                    try {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        String msg = jsonObject.getString("msg");
                        if (code==200){
                            ToastManager.show(msg);
                            finish();
                        }else if (code==111){
                            ToastManager.show(msg);
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
                        if (code==200){
                            JSONObject data = jsonObject.getJSONObject("data");
                            JSONObject jsonObject1 = new JSONObject(data.toString());
                            String name = jsonObject1.getString("name");
                            String zj_type = jsonObject1.getString("zj_type");
                            String zj_code = jsonObject1.getString("zj_code");
                            String lk_type = jsonObject1.getString("lk_type");
                            tvName.setText(name);
                            tvPassengerType.setText(zj_type);
                            tvNumber.setText(zj_code);
                            tvType.setText(lk_type);
                        }else if (code==111){
                            ToastManager.show(msg);
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
                        if (code==200){
                            ToastManager.show(msg);
                            finish();
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
