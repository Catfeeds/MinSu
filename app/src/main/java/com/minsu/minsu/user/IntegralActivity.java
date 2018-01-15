package com.minsu.minsu.user;

import android.content.Context;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.minsu.minsu.R;
import com.minsu.minsu.api.Constant;
import com.minsu.minsu.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IntegralActivity extends BaseActivity {

    @BindView(R.id.webview)
    WebView webview;

    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {
        WebSettings webSettings  = webview.getSettings();
        // 设置与Js交互的权限
        webSettings.setJavaScriptEnabled(true);
        // 设置允许JS弹窗
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webview.loadUrl(Constant.INTEGRAL_URL);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_integral);
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
}
