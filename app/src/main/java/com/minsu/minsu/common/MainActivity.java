package com.minsu.minsu.common;

import android.Manifest;
import android.annotation.SuppressLint;

import cn.jpush.android.api.JPushInterface;
import feature.Callback;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.minsu.minsu.App;
import com.minsu.minsu.R;
import com.minsu.minsu.api.Constant;
import com.minsu.minsu.api.MinSuApi;
import com.minsu.minsu.api.callback.CallBack;
import com.minsu.minsu.common.bean.EvenBean;
import com.minsu.minsu.common.bean.MessageEvent;
import com.minsu.minsu.common.fragment.FragmentController;
import com.minsu.minsu.common.fragment.MeFragment;
import com.minsu.minsu.user.LoginActivity;
import com.minsu.minsu.utils.NetWorkStateReceiver;
import com.minsu.minsu.utils.StorageUtil;
import com.minsu.minsu.utils.ToastManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import customview.ConfirmDialog;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.TextMessage;
import util.UpdateAppUtils;


public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, MeFragment.RoleInterface
{
    private String tokenId;
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
    private String types;
    private static FragmentController controller;
    private int localRole;
    long start = 0;
    long end = 0;

    private final String TAG = "MainActivity";
    private String token;
    private TextView tv_message_counts;
    private String isfd;
    private String nickname;
    private String uri;
    private String userId;

    private void initListener()
    {
        String selectview = getIntent().getStringExtra("selectview");
        if (controller == null)
        {
            controller = FragmentController.getInstance(this, R.id.frame_layout);
            hometabRadio.setOnCheckedChangeListener(this);
            if (selectview != null && selectview.equals("1"))
            {
                StorageUtil.setKeyValue(MainActivity.this,"selectview","2");
                controller.showFragment(3);
                hometabRadio.check(R.id.rb_message);
            } else if (selectview!=null&&selectview.equals("3"))
            {
                StorageUtil.setKeyValue(MainActivity.this,"selectview","3");
                controller.showFragment(3);
                hometabRadio.check(R.id.rb_message);
            }else
            {
                controller.showFragment(0);
            }

        }
        try
        {
            isfd = StorageUtil.getValue(MainActivity.this, "isfd");
//            if (isfd==null||isfd.equals("")||isfd.equals("no"))
//            {
//                controller.showFragment(0);
//                rbHome.setVisibility(View.VISIBLE);
//                rbOrder.setVisibility(View.GONE);
//            } else if (isfd.equals("yes"))
//            {
//                controller.showFragment(1);
//                rbHome.setVisibility(View.GONE);
//                rbOrder.setVisibility(View.VISIBLE);
//            }
            Log.i("MainActivity1", MainActivity.this.toString());
        } catch (Exception e)
        {

        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
//如果用以下这种做法则不保存状态，再次进来的话会显示默认tab
//总是执行这句代码来调用父类去保存视图层的状态
//super.onSaveInstanceState(outState);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        JPushInterface.init(getApplicationContext());
        StorageUtil.setKeyValue(this, "isfd", "no");
        token = StorageUtil.getTokenId(this);
        hometabRadio = (RadioGroup) findViewById(R.id.hometab_radio);
        hometabRadio.setOnCheckedChangeListener(this);
        rbOrder.setVisibility(View.GONE);
        initData();
        initListener();
        RongIM.setConnectionStatusListener(new MyConnectionStatusListener());
        tv_message_counts = findViewById(R.id.tv_message_counts);
        checkAndUpdate();
        types = StorageUtil.getValue(this, "selectview");
    }

    @Override
    protected void onPause()
    {
        Log.i("mainactivity11", "onPause");
        super.onPause();
    }



    private class MyConnectionStatusListener implements RongIMClient.ConnectionStatusListener {

        @Override
        public void onChanged(ConnectionStatus connectionStatus) {

            switch (connectionStatus){

                case CONNECTED://连接成功。

                    break;
                case DISCONNECTED://断开连接。

                    break;
                case CONNECTING://连接中。

                    break;
                case NETWORK_UNAVAILABLE://网络不可用。

                    break;
                case KICKED_OFFLINE_BY_OTHER_CLIENT://用户账户在其他设备登录，本机会被踢掉线
                    StorageUtil.setKeyValue(App.getInstance(), "token", "");
                    Intent intent=new Intent(MainActivity.this, LoginActivity.class);
                    intent.putExtra("type","app");
                    startActivity(intent);
                    finish();
                    StorageUtil.setKeyValue(MainActivity.this,"exit","1");
                    App.getInstance().exit(1);
                    break;
            }
        }
    }



    NetWorkStateReceiver netWorkStateReceiver;
    @Override
    protected void onResume()
    {
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
                new Thread(){
                    @Override
                    public void run()
                    {
                        EventBus.getDefault().post(new EvenBean(1));
                    }
                }.start();
            }

            @Override
            public void no()//没网
            {
                new Thread(){
                    @Override
                    public void run()
                    {
                        EventBus.getDefault().post(new EvenBean(2));
                    }
                }.start();
            }
        });

        String selectview = getIntent().getStringExtra("selectview");
        String t=StorageUtil.getValue(MainActivity.this,"ttt");
      //  Log.i("mainactivity11", "onResume");
       if (controller==null)
       {
           controller = FragmentController.getInstance(this, R.id.frame_layout);
           hometabRadio.setOnCheckedChangeListener(this);
           if ((selectview != null && selectview.equals("1")))
           {
               StorageUtil.setKeyValue(MainActivity.this,"selectview","2");
               controller.showFragment(3);
               hometabRadio.check(R.id.rb_message);
           } else if ((selectview != null && selectview.equals("3"))||(t!=null&&t.equals("t")))
           {
               StorageUtil.setKeyValue(MainActivity.this,"selectview","3");
               controller.showFragment(3);
               hometabRadio.check(R.id.rb_message);
           }else
           {
               controller.showFragment(0);
           }
       }else {
           hometabRadio.setOnCheckedChangeListener(this);
           if ((selectview != null && selectview.equals("1")))
           {
               StorageUtil.setKeyValue(MainActivity.this,"selectview","2");
//               controller.showFragment(0);
//               hometabRadio.check(R.id.rb_message);
           } else if ((selectview != null && selectview.equals("3"))||(t!=null&&t.equals("t")))
           {
               StorageUtil.setKeyValue(MainActivity.this,"selectview","3");
               controller.showFragment(3);
               hometabRadio.check(R.id.rb_message);
           }else
           {
               if (localRole == 1)//房东
               {
                   rbHome.setVisibility(View.GONE);
                   rbOrder.setVisibility(View.VISIBLE);
               } else if (localRole == 2)//房客
               {
                   rbHome.setVisibility(View.VISIBLE);
                   rbOrder.setVisibility(View.GONE);
               }
               //controller.showFragment(0);
           }

       }
        super.onResume();
    }


    //检查升级
    public void getBB()
    {
        OkGo.<String>post(Constant.VERSION_UPDATA)
                .tag(MainActivity.this)
                .execute(new StringCallback()
                {
                    @Override
                    public void onSuccess(Response<String> response)
                    {
                        try
                        {
                            int old = (int) MainActivity.this.getApplicationContext().getPackageManager().
                                    getPackageInfo(MainActivity.this.getPackageName(), 0).versionCode;
                            JSONObject jsonObject = new JSONObject(response.body());
                            JSONObject object = new JSONObject(jsonObject.getString("data"));
                            int version = object.getInt("version");
                            String content = object.getString("content");
                            String url = object.getString("url");
                            String vname = object.getString("versionname");
                            if (version > old)
                            {
                                update(url, version, vname, content);
                            }
                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                        } catch (PackageManager.NameNotFoundException e)
                        {
                            e.printStackTrace();
                        }

                    }
                });


    }

    private void checkAndUpdate()
    {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
        {
            getBB();
        } else
        {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED)
            {
                getBB();
            } else
            {//申请权限
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    private void update(String apkPath, int code, String versionName, String content)
    {

        UpdateAppUtils.from(this)
                .checkBy(UpdateAppUtils.CHECK_BY_VERSION_CODE) //更新检测方式，默认为VersionCode
                .serverVersionCode(code)
                .serverVersionName(versionName)
                .apkPath(apkPath)
                .showNotification(true) //是否显示下载进度到通知栏，默认为true
                .updateInfo(content)  //更新日志信息 String
                .downloadBy(UpdateAppUtils.DOWNLOAD_BY_BROWSER) //下载方式：app下载、手机浏览器下载。默认app下载
//                .downloadBy(UpdateAppUtils.DOWNLOAD_BY_APP) //下载方式：app下载、手机浏览器下载。默认app下载
                .isForce(false) //是否强制更新，默认false 强制更新情况下用户不同意更新则不能使用app
                .update();
    }

    private void initData()
    {

        String landlord = StorageUtil.getValue(MainActivity.this, "role");
        if (landlord != null && landlord.equals("landlord"))
        {
            String jiguang = StorageUtil.getValue(MainActivity.this, "jiguang");
            if (jiguang != null)
            {
                JPushInterface.setAlias(getApplicationContext(), 0, "F" + jiguang);
            }
        }
        tokenId = StorageUtil.getTokenId(this);
        Log.i("token11", tokenId);
        // isReconnect(token);
        // MinSuApi.addChatList(MainActivity.this, 0x003, tokenId, 3, callBack);
        //  MinSuApi.chatList(MainActivity.this,3,tokenId,callBack);
        MinSuApi.getToken(tokenId, 22, callBack);
    }


    interface IOrder
    {
        void is(boolean b);
    }

    private CallBack callBack = new CallBack()
    {
        @Override
        public void onSuccess(int what, Response<String> result)
        {
            switch (what)
            {
                case 0x003:
                    try
                    {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        String msg = jsonObject.getString("msg");
                        if (code == 200)
                        {
//                            ToastManager.show(msg);
                            String data = jsonObject.getString("data");
                            JSONObject object = new JSONObject(data);
                            String id = object.getString("id");
                            //todo
                            // 进入聊天界面
                            MinSuApi.getRongyunToken(MainActivity.this, 0x004, tokenId, Integer.parseInt(id), callBack);
                        } else if (code == 211)
                        {
                            ToastManager.show(msg);
                        }
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    break;
                case 0x004:
                    try
                    {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        if (code == 200)
                        {
                            JSONObject data1 = jsonObject.getJSONObject("data1");
                            JSONObject jsonObject1 = new JSONObject(data1.toString());
                            String token = jsonObject1.getString("token");
                            JSONObject data2 = jsonObject.getJSONObject("data2");
                            JSONObject jsonObject2 = new JSONObject(data2.toString());
                            String userId = jsonObject2.getString("userId");
                            isReconnect(token);
                        }
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    break;
                case 22:
                    try
                    {
                        JSONObject jsonObject = new JSONObject(result.body());
                        String data1 = jsonObject.getString("data1");
                        JSONObject object = new JSONObject(data1);
                        final String token = object.getString("token");
                        nickname = object.getString("nickname");
                        uri = object.getString("head_pic");
                        userId = object.getString("userId");
                        StorageUtil.setKeyValue(MainActivity.this,"nickkk",nickname);
                        if (!uri.startsWith("http"))
                        {
                           uri= Constant.BASE2_URL+uri;
                        }
                        StorageUtil.setKeyValue(MainActivity.this,"imgkkk",uri);
                        isReconnect(token);
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    break;
            }
        }

        @Override
        public void onFail(int what, Response<String> result)
        {

        }

        @Override
        public void onFinish(int what)
        {

        }
    };

    private void isReconnect(final String token)
    {
        if (getApplicationInfo().packageName.equals(App.getCurProcessName(getApplicationContext())))
        {

            RongIM.connect(token, new RongIMClient.ConnectCallback()
            {
                @Override
                public void onTokenIncorrect()
                {
                    Log.i(TAG, "onTokenIncorrect: " + "融云连接失败");
                }

                @Override
                public void onSuccess(final String user_id)
                {
                    Log.i(TAG, "onSuccess: " + "融云连接成功");
                    StorageUtil.setKeyValue(MainActivity.this,"rongtoken",user_id);
                    RongIMClient.getInstance().getTotalUnreadCount(new RongIMClient.ResultCallback<Integer>()
                    {
                        @Override
                        public void onSuccess(Integer integer)
                        {
                            Log.i("amm9a", integer + "融云连接成功");
//                            RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
////
//                                @Override
//                                public UserInfo getUserInfo(String userId) {
//
//                                    return new UserInfo(token, nickname, Uri.parse(uri));//根据 userId 去你的用户系统里查询对应的用户信息返回给融云 SDK。
//                                }
//
//                            }, true);
//                            RongIM.getInstance().refreshUserInfoCache(new UserInfo(userId, nickname,Uri.parse(uri)));
                            if (integer != 0 && integer != -1)
                            {
                                StorageUtil.setKeyValue(MainActivity.this, "charm", (integer) + "");
                                String order = StorageUtil.getValue(MainActivity.this, "orderm");
                                String sys = StorageUtil.getValue(MainActivity.this, "sysm");
                                if (integer>0)
                                {
                                    tv_message_counts.setText(integer+"");
                                    tv_message_counts.setVisibility(View.VISIBLE);
                                }
//                                if (order != null && !order.equals("") && !order.equals("0"))
//                                {
//                                    tv_message_counts.setVisibility(View.VISIBLE);
//                                    tv_message_counts.setText((integer + Integer.parseInt(order)) + "");
//                                }
//                                if (sys != null && !sys.equals("") && !sys.equals("0"))
//                                {
//                                    tv_message_counts.setVisibility(View.VISIBLE);
//                                    tv_message_counts.setText((integer + Integer.parseInt(sys)) + "");
//                                }
//                                if (order != null && !order.equals("") && !order.equals("0") && sys != null && !sys.equals("") && !sys.equals("0"))
//                                {
//                                    tv_message_counts.setVisibility(View.VISIBLE);
//                                    tv_message_counts.setText((integer + Integer.parseInt(order) + Integer.parseInt(sys)) + "");
//                                }
//                                if ((order == null || order.equals("") || order.equals("0")) && (sys == null || sys.equals("") || sys.equals("0")))
//                                {
//                                    tv_message_counts.setVisibility(View.VISIBLE);
//                                    tv_message_counts.setText(integer + "");
//                                }

                                //StorageUtil.setKeyValue(MainActivity.this, "charm", integer + "");
                            } else
                            {
                                StorageUtil.setKeyValue(MainActivity.this, "charm", "0");
                                tv_message_counts.setVisibility(View.GONE);
                            }

                            RongIM.setOnReceiveMessageListener(new RongIMClient.OnReceiveMessageListener()
                            {
                                @Override
                                public boolean onReceived(Message message, int i)
                                {
                                    Log.i("amm9a", i + "");
                                    Intent mIntent = new Intent("asd");
                                    mIntent.putExtra("id", message.getTargetId());
                                    StorageUtil.setKeyValue(MainActivity.this, "ss", "");
                                    sendBroadcast(mIntent);
                                     message.getTargetId();

                                   // RongIMClient.getInstance().clearConversations(Conversation.ConversationType.PRIVATE);
//                                    UserInfo userInfo = new UserInfo( message.getSenderUserId(),message.getObjectName(), Uri.parse(uri));
//                                    RongIM.getInstance().setCurrentUserInfo(userInfo);
//                                    RongIM.getInstance().setMessageAttachedUserInfo(true);

                                    RongIMClient.getInstance().getTotalUnreadCount(new RongIMClient.ResultCallback<Integer>()
                                    {
                                        @Override
                                        public void onSuccess(Integer integer)
                                        {
                                            if (integer>0)
                                            {
                                                tv_message_counts.setText(integer+"");
                                                tv_message_counts.setVisibility(View.VISIBLE);
                                            }
                                        }

                                        @Override
                                        public void onError(RongIMClient.ErrorCode errorCode)
                                        {

                                        }
                                    });
                                    // StorageUtil.setKeyValue(MainActivity.this,"islt","yes");
                                    return false;
                                }
                            });


                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode)
                        {
                            Log.i("aa", errorCode.getMessage());
                        }
                    });

                    String chat_title = StorageUtil.getValue(MainActivity.this, "chat_title");
                    if (RongIM.getInstance() != null)
                    {
                        RongIM.getInstance().enableUnreadMessageIcon(true);//显示未读消息数目
//
                        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
                            @Override
                            public UserInfo getUserInfo(String s) {
                                return new UserInfo(userId,nickname,Uri.parse(uri));
                            }
                        },true);

                        RongIM.getInstance().refreshUserInfoCache(new UserInfo(userId,
                                nickname, Uri.parse(uri)));

//                        UserInfo userInfo = new UserInfo(user_id,nickname, Uri.parse(uri));
//                        RongIM.getInstance().setCurrentUserInfo(userInfo);
//                        RongIM.getInstance().setMessageAttachedUserInfo(true);
                        // RongIM.getInstance().startPrivateChat(MainActivity.this, userId, chat_title);
                    }


                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode)
                {

                }
            });
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId)
    {
        switch (checkedId)
        {
            case R.id.rb_home:
                controller.showFragment(0);
                break;
            case R.id.rb_order:
                controller.showFragment(1);
                break;
            case R.id.rb_find:
                controller.showFragment(2);
                break;
            case R.id.rb_message:
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
    protected void onDestroy()
    {
        super.onDestroy();
        if (controller != null)
        {
            controller = null;
        }
        EventBus.getDefault().unregister(this);
       // RongIM.getInstance().disconnect();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent)
    {
        if (messageEvent == null)
        {
            return;
        }
        int count = messageEvent.getCount();
        if (count != 0 && count > 0)
        {
            tv_message_counts.setVisibility(View.VISIBLE);
            tv_message_counts.setText(messageEvent.getCount() + "");
        } else
        {
            tv_message_counts.setVisibility(View.GONE);
        }
    }

    @Override
    public void changeRole(int role)
    {

        localRole = role;
        if (localRole == 1)//房东
        {
            rbHome.setVisibility(View.GONE);
            rbOrder.setVisibility(View.VISIBLE);
        } else if (localRole == 2)//房客
        {
            rbHome.setVisibility(View.VISIBLE);
            rbOrder.setVisibility(View.GONE);
        }
    }


    @Override
    public void onBackPressed()
    {

        if (!BackHandlerHelper.handleBackPress(this))
        {
            super.onBackPressed();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode)
        {
            case 1:
                for (int i = 0; i < grantResults.length; i++)
                {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED)
                    {
                        getBB();
                    } else
                    {
                        new ConfirmDialog(this, new Callback()
                        {
                            @Override
                            public void callback(int position)
                            {
                                if (position == 1)
                                {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    intent.setData(Uri.parse("package:" + getPackageName())); // 根据包名打开对应的设置界面
                                    startActivity(intent);
                                }
                            }
                        }).setContent("暂无读写SD卡权限\n是否前往设置？").show();
                    }
                }

                break;
        }

    }

}
