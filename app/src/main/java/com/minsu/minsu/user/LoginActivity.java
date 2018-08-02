package com.minsu.minsu.user;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.okgo.model.Response;

import com.minsu.minsu.App;
import com.minsu.minsu.common.lanuch.SplashActivity;
import com.minsu.minsu.user.setting.SettingActivity;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.utils.Log;
import com.minsu.minsu.R;
import com.minsu.minsu.api.MinSuApi;
import com.minsu.minsu.api.callback.CallBack;
import com.minsu.minsu.base.BaseActivity;
import com.minsu.minsu.common.MainActivity;
import com.minsu.minsu.utils.CheckUtil;
import com.minsu.minsu.utils.RegexUtils;
import com.minsu.minsu.utils.StorageUtil;
import com.minsu.minsu.utils.ToastManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import io.rong.imkit.RongIM;

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
    @BindView(R.id.qq)
    ImageView qq;
    @BindView(R.id.weibo)
    ImageView weibo;
    @BindView(R.id.weixin)
    ImageView weixin;
    private String edMobile;
    private String edPassword;
    private int status = 1;
    private MyCountDownTimer myCountDownTimer;
    private static final String TAG = "LoginActivity";
    private String type;
    private PopupWindow popWindow;
    private int i=0;

    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {
        dongtai_Login.setOnClickListener(this);
        pass_Login.setOnClickListener(this);
        bt_logoin.setOnClickListener(this);
        buttonYanzhengma.setOnClickListener(this);
        qq.setOnClickListener(this);
        weibo.setOnClickListener(this);
        weixin.setOnClickListener(this);
        forgetPassword.setOnClickListener(this);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
          if (i==0)
          {
              tt();
          }
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
        App.getInstance().exit(1);
    }

    private void tt()
    {
        String exit=StorageUtil.getValue(LoginActivity.this,"exit");
        StorageUtil.setKeyValue(LoginActivity.this,"exit","2");
        type=getIntent().getStringExtra("type");
        if ((type!=null&&type.equals("app"))||exit!=null&&exit.equals("1"))
        {
            StorageUtil.setKeyValue(LoginActivity.this, "is_name", "");
            StorageUtil.setKeyValue(LoginActivity.this, "role", "");
            final RelativeLayout layout= (RelativeLayout) LayoutInflater.from(LoginActivity.this).inflate(R.layout.exit,null);
            TextView textView=layout.findViewById(R.id.zhidao);
            popWindow = new PopupWindow(layout,
                    RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT,
                    true);
            RongIM.getInstance().logout();
            textView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    layout.setVisibility(View.GONE);
                    i=1;
                    popWindow.dismiss();
                   // startActivity(new Intent(LoginActivity.this,LoginActivity.class));
                }
            });

            popWindow.setFocusable(true);
            // 设置允许在外点击消失
            popWindow.setOutsideTouchable(true);
            // 设置背景，这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
            popWindow.setBackgroundDrawable(new BitmapDrawable());
            //软键盘不会挡着popupwindow
            popWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

            //设置SelectPicPopupWindow弹出窗体的背景
//        WindowManager.LayoutParams lp = getWindow().getAttributes();
//        lp.alpha = 0.7f;
//        getWindow().setAttributes(lp);
            //设置菜单显示的位置
            popWindow.showAtLocation(findViewById(R.id.weixin), Gravity.CENTER, 0, 0);
            //监听菜单的关闭事件
            popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
//                WindowManager.LayoutParams lp = getWindow().getAttributes();
//                lp.alpha = 1f;
//                getWindow().setAttributes(lp);
                }
            });
            //监听触屏事件
            popWindow.setTouchInterceptor(new View.OnTouchListener() {
                public boolean onTouch(View view, MotionEvent event) {
                    return false;
                }
            });
        }

    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_login);
    }

    @Override
    protected Context getActivityContext() {
        return null;
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
                ed_password.setHint("请输入验证码");
                break;
            case R.id.password_login:
                status = 2;
                pass_Login.setBackgroundResource(R.color.steelblue);
                dongtai_Login.setBackgroundResource(R.color.white);
                pass_Login.setTextColor(getResources().getColor(R.color.white));
                dongtai_Login.setTextColor(getResources().getColor(R.color.black));
                buttonYanzhengma.setVisibility(View.GONE);
                ed_password.setHint("请输入密码");
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
                boolean mobile = CheckUtil.isMobile(edMobile);
                boolean password = CheckUtil.isPassword(edPassword);
                if (!mobile) {
                    ToastManager.show("手机号输入不对");
                    return;
                }
                if (edMobile.equals("")) {
                    ToastManager.show("手机号不能为空");
                    return;
                }
                if (status == 1) {
                    if (edPassword.equals("")) {
                        ToastManager.show("验证码不能为空");
                        return;
                    }
                    MinSuApi.codeLogin(this, 0x001, edMobile, "666666", edPassword, callBack);
                } else if (status == 2) {
                    if (edPassword.equals("")) {
                        ToastManager.show("密码不能为空");
                        return;
                    }
                    if (!password) {
                        ToastManager.show("密码输入格式有误");
                        return;
                    }

                    MinSuApi.Login(this, 0x003, edMobile, edPassword, callBack);
                }

                break;
            //---------------三方登录-------------//
            case R.id.qq:
                authorization(SHARE_MEDIA.QQ);
                break;
            case R.id.weibo:
                authorization(SHARE_MEDIA.SINA);
                break;
            case R.id.weixin:
                authorization(SHARE_MEDIA.WEIXIN);
                break;
            case R.id.login_forget_password:
                startActivity(new Intent(LoginActivity.this, FindPasswordActivity.class));
                break;
        }
    }

    //授权
    private void authorization(SHARE_MEDIA share_media) {
        UMShareConfig config = new UMShareConfig();
        config.isNeedAuthOnGetUserInfo(true);
        UMShareAPI.get(LoginActivity.this).setShareConfig(config);
        UMShareAPI.get(this).getPlatformInfo(this, share_media, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                Log.d(TAG, "onStart " + "授权开始");
            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                Log.d(TAG, "onComplete: " + "授权完成");
                String open_id = null;
                String sex;
                //sdk是6.4.4的,但是获取值的时候用的是6.2以前的(access_token)才能获取到值,未知原因
                String uid = map.get("uid");
                String openid = map.get("openid");//微博没有
                String unionid = map.get("unionid");//微博没有
                String access_token = map.get("access_token");
                String refresh_token = map.get("refresh_token");//微信,qq,微博都没有获取到
                String expires_in = map.get("expires_in");
                String name = map.get("name");                  //用户昵称
                String gender = map.get("gender");              //用户性别
                String iconurl = map.get("iconurl");            //用户头像
                if (SHARE_MEDIA.QQ == share_media || SHARE_MEDIA.WEIXIN == share_media) {
                    open_id = openid;
                } else if (SHARE_MEDIA.SINA == share_media) {
                    open_id = uid;
                }
                if (gender.equals("男")) {
                    sex = "1";
                } else if (gender.equals("女")) {
                    sex = "2";
                } else {
                    sex = "0";
                }
                //拿到信息去请求登录接口...
                Log.e("open_id=" + open_id + "name=" + name + ",sex=" + sex + ",iconurl=" + iconurl);
//                DreamApi.login(RegisterActivity.this, LOGIN_WHAT, "0", "", "", open_id, iconurl, sex, name, callBack);
                MinSuApi.thirdLogin(LoginActivity.this, 0x004, open_id, name,iconurl, callBack);
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                Log.d(TAG, "onError " + "授权失败");
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                Log.d(TAG, "onCancel " + "授权取消");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
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
                    try {//动态登录
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        String msg = jsonObject.getString("msg");
                        if (code == 200) {
                            JSONObject data = jsonObject.getJSONObject("data");
                            JSONObject loginData = new JSONObject(data.toString());
                            String jiguang=loginData.getString("jiguang");
                            StorageUtil.setKeyValue(LoginActivity.this,"jiguang",jiguang);
                            JPushInterface.setAlias(getApplicationContext(),0,jiguang);
                            parseData(loginData);
                        } else if (code == 210) {
                            ToastManager.show(msg + "初始密码为666666");
                            JSONObject data = jsonObject.getJSONObject("data");
                            JSONObject loginData = new JSONObject(data.toString());
                            String jiguang=loginData.getString("jiguang");
                            StorageUtil.setKeyValue(LoginActivity.this,"jiguang",jiguang);
                            JPushInterface.setAlias(getApplicationContext(),0,jiguang);
                            parseData(loginData);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 0x002:
                    try {//发送验证码
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
                    try {//密码登录
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        if (code == 200) {

                            JSONObject data = jsonObject.getJSONObject("data");
                            JSONObject loginData = new JSONObject(data.toString());
                            String jiguang=loginData.getString("jiguang");
                            StorageUtil.setKeyValue(LoginActivity.this,"jiguang",jiguang);
                            JPushInterface.setAlias(getApplicationContext(),0,jiguang);
                            parseData(loginData);

                        } else if (code == 111) {
                            String msg = jsonObject.getString("msg");
                            ToastManager.show(msg);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 0x004:
                    try {//第三方登录
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        String msg = jsonObject.getString("msg");
                        if (code == 200) {
                            ToastManager.show(msg);
                            JSONObject data = jsonObject.getJSONObject("data");
                            JSONObject loginData = new JSONObject(data.toString());
                            String jiguang=loginData.getString("jiguang");
                            StorageUtil.setKeyValue(LoginActivity.this,"jiguang",jiguang);
                            JPushInterface.setAlias(getApplicationContext(),0,jiguang);
                            String youmengToken = loginData.getString("token");
//                            StorageUtil.setKeyValue(LoginActivity.this, "youmengToken", youmengToken);
                            StorageUtil.setKeyValue(LoginActivity.this, "token", youmengToken);
                            StorageUtil.setKeyValue(LoginActivity.this, "nickname", loginData.getString("nickname"));
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else if (code == 111) {

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
