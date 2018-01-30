package com.minsu.minsu.user;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
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
import com.minsu.minsu.utils.StorageUtil;
import com.minsu.minsu.utils.ToastManager;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.KeyEvent.KEYCODE_BACK;

public class HelpActivity extends BaseActivity {


    @BindView(R.id.webView)
    WebView webView;
    private String tokenId;

    @Override
    protected void processLogic() {
//        MinSuApi.help(this, 0x001, tokenId, callBack);
    }

    @Override
    protected void setListener() {
        tokenId = StorageUtil.getTokenId(this);
        WebSettings webSettings = webView.getSettings();
        // 设置与Js交互的权限
        webSettings.setJavaScriptEnabled(true);
        // 设置允许JS弹窗
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webView.loadUrl(Constant.HELP_WEB_URL + tokenId);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }
    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_help);
    }

    @Override
    protected Context getActivityContext() {
        return this;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
