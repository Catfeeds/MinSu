package com.minsu.minsu.user;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyco.dialog.entity.DialogMenuItem;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalListDialog;
import com.lzy.okgo.model.Response;
import com.minsu.minsu.R;
import com.minsu.minsu.api.MinSuApi;
import com.minsu.minsu.api.callback.CallBack;
import com.minsu.minsu.base.BaseActivity;
import com.minsu.minsu.utils.StorageUtil;
import com.minsu.minsu.utils.ToastManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private RelativeLayout rlchengren,rlErtong;
    private  String passenger_id;
    private ArrayList<DialogMenuItem> mMenuItems = new ArrayList<>();
    private Button bt_ert;
    private Button bt_cheng;
    private String tvtype;
    private int type=1;

    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {
        rlchengren=findViewById(R.id.rl_chengren);
        rlErtong=findViewById(R.id.rl_ertong);
        bt_ert = findViewById(R.id.bt_ert);
        bt_cheng = findViewById(R.id.bt_cheng);
        rlchengren.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                bt_cheng.setBackgroundResource(R.mipmap.anniu1);
                bt_ert.setBackgroundResource(R.mipmap.anniu0);
                type=1;
                tvNumber.setEnabled(false);
                etNumber.setFocusable(true);
                if (etNumber.getText().toString().length()==0)
                {
                    etNumber.setHint("请输入证件号码");
                }
                etNumber.setFocusableInTouchMode(true);
            }
        });
        rlErtong.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                bt_cheng.setBackgroundResource(R.mipmap.anniu0);
                bt_ert.setBackgroundResource(R.mipmap.anniu1);
                tvNumber.setEnabled(true);
                type=0;
                etNumber.setFocusable(false);
                etNumber.setFocusableInTouchMode(false);
            }
        });
        etName.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View view, boolean b)
            {
                if (b)
                {
                    etName.setText("  ");
                }
            }
        });
        etNumber.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View view, boolean b)
            {
                if (b)
                {
                    etNumber.setText("  ");
                }
            }
        });
        String types=getIntent().getStringExtra("type");
        if (types!=null&&types.equals("1"))
        {
            toolbarTitle.setText("新增常用旅客");
        }else if (types!=null&&types.equals("2"))
        {
            toolbarTitle.setText("常用旅客修改");
        }

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
                if (type==1)
                {
                    tvtype ="成人";
                }else if (type==0)
                {
                    tvtype ="儿童";
                }
                if (etName==null||etName.length()==0)
                {
                    ToastManager.show("请输入名字");
                    return;
                }
                Pattern p = Pattern.compile(".*\\d+.*");
                Matcher m = p.matcher(mEtName);
                if (m.matches())
                {
                    ToastManager.show("请输入合法的名字");
                    return;
                }
                if(mEtNumber==null)
                {
                    if (type==1)
                    {
                        ToastManager.show("请输入合法的身份证号码");
                        return;
                    }
                }
                if (mEtNumber.trim().length()!=18)
                {
                    if (type==1)
                    {
                        ToastManager.show("请输入合法的身份证号码");
                        return;
                    }
                }
                if (type==0)
                {
                    mEtNumber="";
                }
                if (!passenger_id.equals("")&&passenger_id!=null&& tvtype !=null&& tvtype.length()!=0){
                    MinSuApi.editPassenger(AddPassengerActivity.this,0x003,tokenId,Integer.parseInt(passenger_id),mEtName,"身份证",mEtNumber, tvtype,callBack);
                }else{
                    if (tvtype!=null)
                    {
                        if (tvtype.equals("儿童"))
                        {
                            if (tvtype !=null&&mEtNumber!=null&&mEtName!=null&mEtName.length()!=0)
                            {
                                MinSuApi.addPassenger(AddPassengerActivity.this,0x001,tokenId,mEtName,"身份证","", tvtype,callBack);
                            }
                        }else {
                            if (tvtype !=null)
                            {

                                if (mEtNumber!=null&&mEtNumber.trim().length()==18)
                                {
                                    if (mEtName!=null&&mEtName.length()!=0)
                                    {
                                        MinSuApi.addPassenger(AddPassengerActivity.this,0x001,tokenId,mEtName,"身份证",mEtNumber, tvtype,callBack);
                                    }else {
                                        ToastManager.show("请输入名字");
                                    }
                                }else {
                                    ToastManager.show("请输入正确的身份证号");
                                }

                            }
                        }
                    }


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
                            if (lk_type.equals("成人"))
                            {
                                bt_cheng.setBackgroundResource(R.mipmap.anniu1);
                                bt_ert.setBackgroundResource(R.mipmap.anniu0);
                                etNumber.setFocusable(true);
                                etNumber.setFocusableInTouchMode(true);
                            }else {
                                etNumber.setFocusable(false);
                                etNumber.setText("");
                                etNumber.setFocusableInTouchMode(false);
                                bt_cheng.setBackgroundResource(R.mipmap.anniu0);
                                bt_ert.setBackgroundResource(R.mipmap.anniu1);
                            }

                            etNumber.setText(zj_code);
                            etName.setText(name);
                            tvType.setText(zj_type);
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
