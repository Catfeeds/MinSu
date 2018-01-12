package com.zhuye.minsu.user;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.okgo.model.Response;
import com.zhuye.minsu.R;
import com.zhuye.minsu.api.MinSuApi;
import com.zhuye.minsu.api.callback.CallBack;
import com.zhuye.minsu.base.BaseActivity;
import com.zhuye.minsu.utils.CheckUtil;
import com.zhuye.minsu.utils.RegexUtils;
import com.zhuye.minsu.utils.ToastManager;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FindPasswordActivity extends BaseActivity implements View.OnClickListener {

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
    @BindView(R.id.login_tag1)
    TextView loginTag1;
    @BindView(R.id.mobile)
    EditText mobile;
    @BindView(R.id.rl_tag1)
    RelativeLayout rlTag1;
    @BindView(R.id.login_tag2)
    TextView loginTag2;
    @BindView(R.id.button_yanzhengma)
    Button buttonYanzhengma;
    @BindView(R.id.smsCode)
    EditText smsCode;
    @BindView(R.id.rl_tag2)
    RelativeLayout rlTag2;
    @BindView(R.id.login_tag3)
    TextView loginTag3;
    @BindView(R.id.new_password)
    EditText newPassword;
    @BindView(R.id.rl_tag3)
    RelativeLayout rlTag3;
    @BindView(R.id.submit)
    Button submit;
    private String etMobile;
    private MyCountDownTimer myCountDownTimer;

    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {
        toolbarTitle.setText("找回密码");
        ivLeft.setVisibility(View.VISIBLE);
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        submit.setOnClickListener(this);
        buttonYanzhengma.setOnClickListener(this);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_find_password);
    }

    @Override
    protected Context getActivityContext() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit:
                etMobile = mobile.getText().toString();
                String mSmsCode = smsCode.getText().toString();
                String mNewPassword = newPassword.getText().toString();
                boolean password = CheckUtil.isPassword(mNewPassword);
                if (!password){
                    ToastManager.show("密码输入格式有误");
                    return;
                }
                if (etMobile.equals("")){
                    ToastManager.show("手机号不能为空");
                    return;
                }
                if (mSmsCode.equals("")){
                    ToastManager.show("验证码不能为空");
                    return;
                }
                if (mNewPassword.equals("")){
                    ToastManager.show("密码不能为空");
                    return;
                }
                MinSuApi.forgetPassword(FindPasswordActivity.this, 0x001, etMobile, mSmsCode, mNewPassword, callBack);
                break;
            case R.id.button_yanzhengma:
                etMobile = mobile.getText().toString();
                if (RegexUtils.isMobileExact(etMobile)) {
                    MinSuApi.getSmsCode(0x002, etMobile, 1, callBack);
                    myCountDownTimer = new MyCountDownTimer(60000, 1000);
                    myCountDownTimer.start();
                } else {
                    ToastManager.show("您输入的手机号格式不正确");
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
            mobile.removeTextChangedListener(mTextWatcher);
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
            mobile.addTextChangedListener(mTextWatcher);
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
                            String msg = jsonObject.getString("msg");
                            ToastManager.show(msg);
                            finish();
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
            }
        }

        @Override
        public void onFail(int what, Response<String> result) {

        }

        @Override
        public void onFinish(int what) {

        }
    };
}
