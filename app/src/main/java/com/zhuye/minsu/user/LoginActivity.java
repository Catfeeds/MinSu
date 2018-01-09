package com.zhuye.minsu.user;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzy.okgo.model.Response;
import com.zhuye.minsu.R;
import com.zhuye.minsu.api.MinSuApi;
import com.zhuye.minsu.api.callback.CallBack;
import com.zhuye.minsu.base.BaseActivity;
import com.zhuye.minsu.common.MainActivity;
import com.zhuye.minsu.utils.RegexUtils;
import com.zhuye.minsu.utils.StorageUtil;
import com.zhuye.minsu.utils.ToastManager;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.login_mobile)
    EditText ed_mobile;
    @BindView(R.id.login_password)
    EditText ed_password;
    @BindView(R.id.logoin)
    Button bt_logoin;
    @BindView(R.id.login_forget_password)
    TextView forgetPassword;
    @BindView(R.id.dongtai_login)
    TextView dongtai_Login;
    @BindView(R.id.password_login)
    TextView pass_Login;
    @BindView(R.id.ll_dynamic_code)
    LinearLayout llDynamicCode;
    @BindView(R.id.button_yanzhengma)
    Button buttonYanzhengma;
    private String edMobile;
    private String edPassword;
    private int status = 1;
    private MyCountDownTimer myCountDownTimer;
    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {
        dongtai_Login.setOnClickListener(this);
        pass_Login.setOnClickListener(this);
        bt_logoin.setOnClickListener(this);
        buttonYanzhengma.setOnClickListener(this);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_login);


    }

    @Override
    protected Context getActivityContext() {
        return this;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dongtai_login:
                status = 1;
                dongtai_Login.setBackgroundResource(R.color.steelblue);
                pass_Login.setBackgroundResource(R.color.white);
                buttonYanzhengma.setVisibility(View.VISIBLE);
                dongtai_Login.setTextColor(getResources().getColor(R.color.white));
                pass_Login.setTextColor(getResources().getColor(R.color.black));
                break;
            case R.id.password_login:
                status = 2;
                pass_Login.setBackgroundResource(R.color.steelblue);
                dongtai_Login.setBackgroundResource(R.color.white);
                pass_Login.setTextColor(getResources().getColor(R.color.white));
                dongtai_Login.setTextColor(getResources().getColor(R.color.black));
                buttonYanzhengma.setVisibility(View.GONE);
                break;
            case R.id.button_yanzhengma:
                edMobile = ed_mobile.getText().toString();
                if (RegexUtils.isMobileExact(edMobile)) {
                    MinSuApi.getSmsCode(0x002, edMobile, 1, callBack);
                    myCountDownTimer = new MyCountDownTimer(60000, 1000);
                    myCountDownTimer.start();
                } else {
                    ToastManager.show("您输入的手机号格式不正确");
                }

                break;

            case R.id.logoin:

                edMobile = ed_mobile.getText().toString();
                edPassword = ed_password.getText().toString();
                if (status == 1) {
                    ToastManager.show(status + "");
                    MinSuApi.codeLogin(this, 0x001, edMobile, "666666", edPassword, callBack);
                } else if (status == 2) {
                    ToastManager.show(status + "");
                    MinSuApi.Login(this, 0x003, edMobile, edPassword, callBack);
                }

                break;
        }
    }
    //复写倒计时
    private class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        //计时过程
        @Override
        public void onTick(long l) {
            //防止计时过程中重复点击
            ed_mobile.removeTextChangedListener(mTextWatcher);
            buttonYanzhengma.setBackgroundResource(R.drawable.register_login_button_yanzhengma_shape_nophone);
            buttonYanzhengma.setClickable(false);
            buttonYanzhengma.setText(l / 1000 + "s");
        }

        //计时完毕的方法
        @Override
        public void onFinish() {
            //重新给Button设置文字
            buttonYanzhengma.setText("重新获取");
            //设置可点击
            ed_mobile.addTextChangedListener(mTextWatcher);
            buttonYanzhengma.setClickable(true);
            buttonYanzhengma.setBackgroundResource(R.drawable.register_login_button_yanzhengma_shape);
        }
    }
    //监听输入的手机号
    TextWatcher mTextWatcher = new TextWatcher() {
        private CharSequence temp;

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            temp = s;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (temp.length() == 11) {//改变验证码背景颜色
                buttonYanzhengma.setBackgroundResource(R.drawable.register_login_button_yanzhengma_shape);
                buttonYanzhengma.setEnabled(true);
                buttonYanzhengma.setClickable(true);
            } else {
                buttonYanzhengma.setBackgroundResource(R.drawable.register_login_button_yanzhengma_shape_nophone);
                buttonYanzhengma.setClickable(false);
            }
        }
    };
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
                            JSONObject loginData = new JSONObject(data.toString());
                            parseData(loginData);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 0x002:
                    try {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        if (code == 200) {
                            String msg = jsonObject.getString("msg");
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
                        if (code == 200) {

                            JSONObject data = jsonObject.getJSONObject("data");
                            JSONObject loginData = new JSONObject(data.toString());
                            parseData(loginData);

                        } else if (code == 111) {
                            String msg = jsonObject.getString("msg");
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

    private void parseData(JSONObject data) {
        try {
            String token = data.getString("token");
            String nickname = data.getString("nickname");
            StorageUtil.setKeyValue(LoginActivity.this, "token", token);
            StorageUtil.setKeyValue(LoginActivity.this, "nickname", nickname);
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
