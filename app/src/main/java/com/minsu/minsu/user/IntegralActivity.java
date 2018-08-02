package com.minsu.minsu.user;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.minsu.minsu.R;
import com.minsu.minsu.api.Constant;
import com.minsu.minsu.base.BaseActivity;
import com.minsu.minsu.utils.NetWorkStateReceiver;
import com.minsu.minsu.utils.StorageUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.KeyEvent.KEYCODE_BACK;

public class IntegralActivity extends AppCompatActivity
{

    @BindView(R.id.webview)
    WebView webview;
    private String tokenId;
    private String localUrl;
    private ImageView imageView;
    private NetWorkStateReceiver netWorkStateReceiver;
    private boolean isone=false;
    @SuppressLint("JavascriptInterface")
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KEYCODE_BACK) && webview.canGoBack()) {
            webview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }
    @Override
    protected void onResume()
    {
        super.onResume();
        if (netWorkStateReceiver == null) {
            netWorkStateReceiver = new NetWorkStateReceiver();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netWorkStateReceiver, filter);

        netWorkStateReceiver.setINew(new NetWorkStateReceiver.INewWork()
        {
            @Override
            public void yes()//有网
            {
                imageView.setVisibility(View.GONE);
                if (webview!=null)
                {
                    webview.setVisibility(View.VISIBLE);
                    if (isone)
                    {
                        webview.loadUrl(Constant.INTEGRAL_URL + tokenId);
                    }
                }
            }

            @Override
            public void no()//没网
            {
                imageView.setVisibility(View.VISIBLE);
                if (webview!=null)
                {
                    webview.setVisibility(View.GONE);
                    isone=true;
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integral);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        imageView=findViewById(R.id.imaged);
        tokenId = StorageUtil.getTokenId(this);
        WebSettings webSettings = webview.getSettings();
        // 设置与Js交互的权限
        webSettings.setJavaScriptEnabled(true);
        // 设置允许JS弹窗
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
            webview.getSettings().setLoadsImagesAutomatically(true);
        } else
        {
            webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
            webSettings.setLoadsImagesAutomatically(false);
        }

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
        {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webview.loadUrl(Constant.INTEGRAL_URL + tokenId);
        webview.setWebViewClient(new WebViewClient() {


            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                localUrl = url;
                view.loadUrl(url);
                return true;
            }
        });
        Jsinterface jsinterface = new Jsinterface();
        webview.addJavascriptInterface(jsinterface, "jsInterface");
    }

    public class Jsinterface {
        @JavascriptInterface
        public void js_java() {
            Intent intent = new Intent(IntegralActivity.this, AddressActivity.class);
            startActivityForResult(intent, 1);
        }

        @JavascriptInterface
        public void js_back() {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (webview != null) {
                webview.loadUrl(localUrl);
            }
        }

    }


}
