package com.zhuye.minsu.user;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zhuye.minsu.R;
import com.zhuye.minsu.base.BaseActivity;

import butterknife.BindView;

public class LoginActivity extends BaseActivity
{
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
    private String edMobile;
    private String edPassword;
    @Override
    protected void processLogic()
    {

    }

    @Override
    protected void setListener()
    {

    }

    @Override
    protected void loadViewLayout()
    {
        setContentView(R.layout.activity_login);
        edMobile=ed_mobile.getText().toString().trim();
        edPassword=ed_password.getText().toString().trim();


    }

    @Override
    protected Context getActivityContext()
    {
        return this;
    }
}
