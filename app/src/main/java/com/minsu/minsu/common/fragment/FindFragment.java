package com.minsu.minsu.common.fragment;


import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.minsu.minsu.App;
import com.minsu.minsu.R;
import com.minsu.minsu.api.Constant;
import com.minsu.minsu.base.BaseFragment;
import com.minsu.minsu.common.FragmentBackHandler;
import com.minsu.minsu.common.bean.EvenBean;
import com.minsu.minsu.common.bean.ShareBean;
import com.minsu.minsu.find.DeliteWebViewActivity;
import com.minsu.minsu.utils.NetWorkStateReceiver;
import com.minsu.minsu.utils.StorageUtil;
import com.minsu.minsu.utils.ToastManager;
import com.minsu.minsu.utils.UIThread;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.rong.imkit.RongIM;


/**
 * Created by hpc on 2017/12/1.
 */

public class FindFragment extends BaseFragment implements View.OnLayoutChangeListener, FragmentBackHandler
{

    //    @BindView(R.id.tab)
//    FrameLayout tab;
//    @BindView(R.id.viewpager)
//    ViewPager viewpager;
    Unbinder unbinder;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.image)
    ImageView image;
    private View view;
    private String tokenId;
    private String localUrl;
    private LinearLayout ll_webview;
    private int news;
    private Activity activity;
    private String allurl;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container)
    {

        view = inflater.inflate(R.layout.fragment_find, container, false);
        return view;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventAsync(EvenBean bean)
    {
        if (bean.getType() == 1)//有网
        {
            image.setVisibility(View.GONE);
            if (webView!=null)
            {
                webView.setVisibility(View.VISIBLE);
                webView.loadUrl(Constant.FIND_WEB_URL + tokenId);
            }
        } else
        {//断网
            webView.setVisibility(View.GONE);
            image.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initListener()
    {
        statu();
        refreshLayout.setOnRefreshListener(new OnRefreshListener()
        {
            @Override
            public void onRefresh(RefreshLayout refreshlayout)
            {
                if (webView != null)
                {
                    webView.loadUrl(Constant.FIND_WEB_URL + tokenId);
                    refreshLayout.setEnableRefresh(false);
                }
                if (StorageUtil.getValue(getActivity(), "networks") != null &&
                        StorageUtil.getValue(getActivity(), "networks").equals("no"))
                {
                    refreshlayout.finishRefresh(3000, false);
                    refreshLayout.setEnableRefresh(true);
                } else
                {
                    refreshlayout.finishRefresh();
                }
            }
        });
    }

    @Override
    public void onAttach(Activity activity)
    {
        this.activity = activity;
        super.onAttach(activity);
    }

    private void statu()
    {
        tokenId = StorageUtil.getTokenId(getActivity());
        refreshLayout.setEnableRefresh(false);
        WebSettings webSettings = webView.getSettings();
        // 设置与Js交互的权限
        webSettings.setJavaScriptEnabled(true);
        // 设置允许JS弹窗
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH); // 提高渲染的优先级
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
            webView.getSettings().setLoadsImagesAutomatically(true);
        } else
        {
            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
            webSettings.setLoadsImagesAutomatically(false);
        }
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webView.loadUrl(Constant.FIND_WEB_URL + tokenId);
        webView.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                localUrl = url;
                allurl = url;
                if (localUrl.startsWith("http://www.renbentech.com/api/faxian/yj_html")
                        || localUrl.startsWith("http://www.renbentech.com/api/faxian/gl_html")
                        || localUrl.startsWith("http://www.renbentech.com/api/faxian/fx_html"))
                {
                    view.loadUrl(localUrl);
                } else
                {
                    Intent intent = new Intent(getActivity(), DeliteWebViewActivity.class);
                    intent.putExtra("uri", url);
                    startActivity(intent);
                }
                return true;
            }
        });
        Jsinterface jsinterface = new Jsinterface();
        webView.addJavascriptInterface(jsinterface, "jsInterface");
    }

    @Override
    protected void initData()
    {
        if (StorageUtil.getValue(getActivity(), "networks") != null &&
                StorageUtil.getValue(getActivity(), "networks").equals("no"))
        {
            image.setVisibility(View.VISIBLE);
        }
    }

//    @Override
//    public boolean onBackPressed() {
//        if (webView.canGoBack()) {
//            webView.goBack();
//            return true;
//        }
//        return false;
//    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        String fabu = StorageUtil.getValue(getActivity(), "fabu");
        StorageUtil.setKeyValue(getActivity(), "fabu", "4");
        if (fabu != null && !fabu.equals("") && webView != null)
        {
            if (fabu.equals("1"))
            {
                webView.loadUrl(Constant.FIND_WEB_URL + tokenId);
            } else if (fabu.equals("2"))
            {
                webView.loadUrl(allurl);
            } else if (fabu.equals("3"))
            {
                webView.loadUrl(allurl);
            }
        }
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right,
                               int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom)
    {
        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        int height = metric.heightPixels;
        Log.i("aaaaaaaaaaaaaaaa", "oldTop" + oldTop + "--" + "oldBottom" + "---" + "bottom" + bottom + "top" + top);
//old是改变前的左上右下坐标点值，没有old的是改变后的左上右下坐标点值
        int old = bottom;
        final AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        final RelativeLayout layout = appCompatActivity.findViewById(R.id.ll_dd);
        final RadioGroup group = appCompatActivity.findViewById(R.id.hometab_radio);
        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100);
        //现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起
        if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > height / 3))
        {
            //getActivity().getWindow().findViewById(R.id.ll_dd).setVisibility(View.GONE);
//            params.setMargins(0,100,0,0);
//            layout.setLayoutParams(params);
//            group.setVisibility(View.GONE);
//            layout.setVisibility(View.GONE);


        } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > height / 3))
        {
            // getActivity().getWindow().findViewById(R.id.ll_dd).setVisibility(View.VISIBLE);
//            layout.setVisibility(View.VISIBLE);
//            group.setVisibility(View.VISIBLE);
//            params.setMargins(0,0,0,0);
//            layout.setLayoutParams(params);
//            Toast.makeText(getActivity(), "监听到软件盘关闭...", Toast.LENGTH_SHORT).show();

        }
    }

    private boolean isDoubleClick = false;

    @Override
    public boolean onBackPressed()
    {
        if (isDoubleClick)
        {
            RongIM.getInstance().disconnect();
            App.getInstance().exit(2);
        } else
        {
            ToastManager.show("再次点击一次退出程序");
            isDoubleClick = true;
            UIThread.getInstance().postDelay(new Runnable()
            {
                @Override
                public void run()
                {
                    isDoubleClick = false;
                }
            }, 1000);
        }
        return true;

    }

    public class Jsinterface
    {
        @JavascriptInterface
        public void js_java()
        {
            if (localUrl==null)
            {
                Intent intent = new Intent(getActivity(), FaBuActivity.class);
                intent.putExtra("type", "发现");
                startActivityForResult(intent, 1);
                localUrl=null;
                return;
            }
            String type = "";
            if (localUrl.startsWith("http://minsu.zyeo.net/index.php/Api/faxian/yj_html"))//游记
            {
                type = "游记";
            } else if (localUrl.startsWith("http://minsu.zyeo.net/index.php/Api/faxian/gl_html"))//攻略
            {
                type = "攻略";
            }
            Intent intent = new Intent(getActivity(), FaBuActivity.class);
            intent.putExtra("type", type);
            startActivityForResult(intent, 1);
            localUrl=null;
        }

        @JavascriptInterface
        public void share()
        {
            String[] ids = localUrl.split("article_id=");
            String trueid = ids[1];
            if (trueid.length() > 15)
            {
                String[] idss = trueid.split("&token");
                trueid = idss[0];
            }
            goShare(trueid);
        }
    }


    private void goShare(String trueid)
    {
        OkGo.<String>post(Constant.SHARE)
                .params("article_id", trueid)
                .execute(new StringCallback()
                {
                    @Override
                    public void onSuccess(Response<String> response)
                    {
                        ShareBean bean = new Gson().fromJson(response.body(), ShareBean.class);
                        UMImage image = new UMImage(getActivity(), Constant.BASE_URL + bean.getData().getArticle_img());//网络图片
                        //UMWeb web = new UMWeb(bean.getData().getUrl());
                        UMWeb web = new UMWeb(localUrl);
                        Log.i("asd", bean.getData().getUrl());
                        web.setTitle(bean.getData().getTitle());//标题
                        web.setThumb(image);  //缩略图
                        // web.setDescription("my description");//描述
                        new ShareAction(getActivity())
                                .withMedia(web)
                                .setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN)
                                .setCallback(shareListener)
                                .open();
                    }

                    //.setCallback(shareListener)
                    @Override
                    public void onStart(Request<String, ? extends Request> request)
                    {
                        super.onStart(request);
//                        Log.i("as", response.body());
//                        // MinSuApi.fenxiang(getActivity(),FENXIANGBA,id,callBack);
//                        Toast.makeText(getActivity(), response.body(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(getActivity()).onActivityResult(requestCode, resultCode, data);
    }

    private UMShareListener shareListener = new UMShareListener()
    {
        @Override
        public void onStart(SHARE_MEDIA share_media)
        {

        }

        @Override
        public void onResult(SHARE_MEDIA share_media)
        {
            Log.i("asdonResult", share_media.getsharestyle(true));
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable)
        {

            Log.i("asdonError", throwable.getMessage() + share_media.toString());
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media)
        {

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        unbinder.unbind();
    }
}
