package com.zhuye.minsu.common;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.baidu.location.LocationClient;
import com.zhuye.minsu.R;
import com.zhuye.minsu.base.BaseActivity;
import com.zhuye.minsu.common.fragment.FragmentController;
import com.zhuye.minsu.utils.StorageUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;


public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    private static final String TAG_PAGE_HOME = "首页";
    private static final String TAG_PAGE_FIND = "发现";
    private static final String TAG_PAGE_MESSAGE = "消息";
    private static final String TAG_PAGE_ORDER = "订单";
    private static final String TAG_PAGE_USER = "我的";
    @BindView(R.id.frame_layout)
    FrameLayout frameLayout;
    @BindView(R.id.rb_home)
    RadioButton rbHome;
    @BindView(R.id.rb_find)
    RadioButton rbFind;
    @BindView(R.id.rb_message)
    RadioButton rbMessage;
    @BindView(R.id.rb_order)
    RadioButton rbOrder;
    @BindView(R.id.rb_me)
    RadioButton rbMe;
    @BindView(R.id.hometab_radio)
    RadioGroup hometabRadio;
    private FragmentController controller;

    private static final String TAG = "MainActivity";
    private String token;

    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {
        token = StorageUtil.getTokenId(this);
        hometabRadio = (RadioGroup) findViewById(R.id.hometab_radio);
        hometabRadio.setOnCheckedChangeListener(this);
        rbOrder.setVisibility(View.GONE);
//        rbHome.setVisibility(View.GONE);
        //融云连接
        connect(token);
    }

    private void connect(String token) {
//        if (getApplicationInfo().packageName.equals(App.getCurProcessName(getApplicationContext()))) {

        RongIM.connect(token, new RongIMClient.ConnectCallback() {

            /**
             * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
             *                  2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
             */
            @Override
            public void onTokenIncorrect() {
                Log.i(TAG, "onTokenIncorrect: " + "融云token错误");
            }

            /**
             * 连接融云成功
             * @param userid 当前 token 对应的用户 id
             */
            @Override
            public void onSuccess(String userid) {
                Log.d("MainActivity", "--onSuccess" + userid);

            }

            /**
             * 连接融云失败
             * @param errorCode 错误码，可到官网 查看错误码对应的注释
             */
            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.i(TAG, "onError: errorCode" + errorCode);
            }
        });
//        }
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_main);
        controller = FragmentController.getInstance(MainActivity.this, R.id.frame_layout);
        controller.showFragment(0);
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
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        switch (checkedId) {
            case R.id.rb_home:
                controller.showFragment(0);
                break;
            case R.id.rb_find:
                controller.showFragment(1);
                break;
            case R.id.rb_message:
                controller.showFragment(2);
                break;
            case R.id.rb_order:
                controller.showFragment(3);
                break;
            case R.id.rb_me:
                controller.showFragment(4);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
