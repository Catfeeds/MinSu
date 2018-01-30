package com.minsu.minsu.user;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.minsu.minsu.R;
import com.minsu.minsu.api.Constant;
import com.minsu.minsu.base.BaseActivity;
import com.minsu.minsu.utils.StorageUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.KeyEvent.KEYCODE_BACK;

public class IntegralActivity extends BaseActivity {

    @BindView(R.id.webview)
    WebView webview;

    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {
        String tokenId = StorageUtil.getTokenId(this);
        WebSettings webSettings = webview.getSettings();
        // 设置与Js交互的权限
        webSettings.setJavaScriptEnabled(true);
        // 设置允许JS弹窗
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webview.loadUrl(Constant.INTEGRAL_URL + tokenId);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KEYCODE_BACK) && webview.canGoBack()) {
            webview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);

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
