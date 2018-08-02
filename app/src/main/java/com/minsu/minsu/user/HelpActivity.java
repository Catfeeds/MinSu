package com.minsu.minsu.user;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzy.okgo.model.Response;
import com.minsu.minsu.R;
import com.minsu.minsu.api.Constant;
import com.minsu.minsu.api.MinSuApi;
import com.minsu.minsu.api.callback.CallBack;
import com.minsu.minsu.base.BaseActivity;
import com.minsu.minsu.common.fragment.FaBuActivity;
import com.minsu.minsu.common.fragment.FindFragment;
import com.minsu.minsu.utils.NetWorkStateReceiver;
import com.minsu.minsu.utils.StorageUtil;
import com.minsu.minsu.utils.ToastManager;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.KeyEvent.KEYCODE_BACK;

public class HelpActivity extends AppCompatActivity
{


    @BindView(R.id.webView)
    WebView webView;
    private String tokenId;
   private ImageView imageView;
   private boolean isone=false;
    private NetWorkStateReceiver netWorkStateReceiver;


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }


//    private CallBack callBack = new CallBack() {
//        @Override
//        public void onSuccess(int what, Response<String> result) {
//            switch (what){
//                case 0x001:
//                    try {
//                        JSONObject jsonObject = new JSONObject(result.body());
//                        int code = jsonObject.getInt("code");
//                        String msg = jsonObject.getString("msg");
//                        if (code == 200) {
//                            JSONObject data = jsonObject.getJSONObject("data");
//                            JSONObject jsonObject1 = new JSONObject(data.toString());
//                            String content = jsonObject1.getString("content");
//                            webView.loadDataWithBaseURL(null, content, "text/html" , "utf-8", null);
//                        } else if (code == 111) {
//                            ToastManager.show(msg);
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    break;
//            }
//
//        }
//
//        @Override
//        public void onFail(int what, Response<String> result) {
//
//        }
//
//        @Override
//        public void onFinish(int what) {
//
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        setContentView(R.layout.activity_help);
        ButterKnife.bind(this);
        imageView = findViewById(R.id.imagee);
        tokenId = StorageUtil.getTokenId(this);
        WebSettings webSettings = webView.getSettings();
        // 设置与Js交互的权限
        webSettings.setJavaScriptEnabled(true);
        // 设置允许JS弹窗
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webView.loadUrl(Constant.HELP_WEB_URL + tokenId);
        webView.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                view.loadUrl(url);
                return true;
            }
        });
        Jsinterface jsinterface = new Jsinterface();
        webView.addJavascriptInterface(jsinterface, "jsInterface");
    }

    public class Jsinterface {
        @JavascriptInterface
        public void js_back() {
          finish();
        }
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
                if (webView!=null)
                {
                    webView.setVisibility(View.VISIBLE);
                    if (isone)
                    {
                        webView.loadUrl(Constant.HELP_WEB_URL + tokenId);
                    }
                }
            }

            @Override
            public void no()//没网
            {
                imageView.setVisibility(View.VISIBLE);
                if (webView!=null)
                {
                    webView.setVisibility(View.GONE);
                    isone=true;
                }
            }
        });
    }
}
