package com.minsu.minsu.common.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lzy.okgo.model.Response;
import com.minsu.minsu.App;
import com.minsu.minsu.R;
import com.minsu.minsu.api.Constant;
import com.minsu.minsu.api.MinSuApi;
import com.minsu.minsu.api.callback.CallBack;
import com.minsu.minsu.base.BaseFragment;
import com.minsu.minsu.common.FragmentBackHandler;
import com.minsu.minsu.common.bean.Isvisible;
import com.minsu.minsu.houseResource.HouseResourceActivity;
import com.minsu.minsu.user.AccountActivity;
import com.minsu.minsu.user.CollectActivity;
import com.minsu.minsu.user.CouponActivity;
import com.minsu.minsu.user.HelpActivity;
import com.minsu.minsu.user.IntegralActivity;
import com.minsu.minsu.user.LandlordAuthenticationActivity;
import com.minsu.minsu.user.OrderActivity;
import com.minsu.minsu.user.UserInfoActivity;
import com.minsu.minsu.user.setting.SettingActivity;
import com.minsu.minsu.utils.StorageUtil;
import com.minsu.minsu.utils.ToastManager;
import com.minsu.minsu.utils.UIThread;
import com.minsu.minsu.widget.ActionBarClickListener;
import com.minsu.minsu.widget.RoundedCornerImageView;
import com.minsu.minsu.widget.TranslucentScrollView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.jpush.android.api.JPushInterface;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;


/**
 * Created by hpc on 2017/12/1.
 */

public class MeFragment extends BaseFragment implements View.OnClickListener, TranslucentScrollView.TranslucentChangedListener, ActionBarClickListener, FragmentBackHandler
{


    @BindView(R.id.user_avatar)
    RoundedCornerImageView userAvatar;
    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.renzheng)
    TextView renzheng;
    @BindView(R.id.ll_order)
    LinearLayout llOrder;
    @BindView(R.id.ll_account)
    LinearLayout llAccount;
    @BindView(R.id.ll_integral)
    LinearLayout llIntegral;
    @BindView(R.id.ll_coupon)
    LinearLayout llCoupon;
    @BindView(R.id.ll_help)
    LinearLayout llHelp;
    @BindView(R.id.ll_collect)
    LinearLayout llCollect;
    @BindView(R.id.landlord)
    Button landlord;
    Unbinder unbinder;
    @BindView(R.id.img_setting)
    ImageView imgSetting;
    @BindView(R.id.lay_header)
    RelativeLayout layHeader;
    @BindView(R.id.ll_house_resource)
    LinearLayout llHouseResource;
    @BindView(R.id.xi)
    ImageView xi;
    @BindView(R.id.xt)
    TextView xt;
    @BindView(R.id.xii)
    ImageView xii;
    @BindView(R.id.xtt)
    TextView xtt;
    private View view;
    private String token;
    private int is_house;
    private int type = 1;
    private String isfd;
    private String defaultimg,nickname;
    private String rongtoken;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container)
    {
        view = inflater.inflate(R.layout.fragment_me, container, false);
        return view;
    }



    @Override
    protected void initListener()
    {
        llOrder.setVisibility(View.VISIBLE);
        llHouseResource.setVisibility(View.GONE);
        roleInterface.changeRole(2);
        token = StorageUtil.getTokenId(getActivity());
        llOrder.setOnClickListener(this);
        llAccount.setOnClickListener(this);
        llIntegral.setOnClickListener(this);
        imgSetting.setOnClickListener(this);
        llCoupon.setOnClickListener(this);
        llCollect.setOnClickListener(this);
        llHelp.setOnClickListener(this);
        userAvatar.setOnClickListener(this);
        llHouseResource.setOnClickListener(this);
        landlord.setOnClickListener(this);
    }

    @Override
    protected void initData()
    {
        rongtoken = StorageUtil.getValue(getActivity(),"rongtoken");
        MinSuApi.userCenterq(getActivity(), 0x001, token, callBack);
        try
        {
//            isfd = StorageUtil.getValue(getActivity(), "isfd");
//            if (isfd.equals("yes"))
//            {
//                type = 2;
//                landlord.setText("转变为房客");
//            } else
//            {
//                type = 1;
//                landlord.setText("转变为房东");
//            }
        } catch (Exception e)
        {

        }

    }

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

    private CallBack callBack = new CallBack()
    {
        @Override
        public void onSuccess(int what, Response<String> result)
        {
            switch (what)
            {
                case 0x001:
                    try
                    {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        if (code == 200)
                        {
                            JSONObject data = jsonObject.getJSONObject("data");
                            JSONObject userData = new JSONObject(data.toString());
                              nickname = userData.getString("nickname");
                            StorageUtil.setKeyValue(getActivity(), "nickname", nickname);
                            String head_pic = userData.getString("head_pic");

                            int is_name = userData.getInt("is_name");

                            is_house = userData.getInt("is_house");
                            int user_id = userData.getInt("user_id");
                            if (is_house == 1)
                            {
                                //已经认证通过成为房东
                                renzheng.setText("已房东认证");
                                StorageUtil.setKeyValue(getActivity(), "role", "landlord");
                                String jiguang= StorageUtil.getValue(getActivity(),"jiguang");
                                JPushInterface.setAlias(getActivity().getApplicationContext(),0,"F"+jiguang);
                            } else if (is_house==2)
                            {
                                StorageUtil.setKeyValue(getActivity(), "role", "user");
                                renzheng.setText("房东认证审核中");
                               // roleInterface.changeRole(2);
                                type = 1;
                                EventBus.getDefault().post(new Isvisible(2));
                                StorageUtil.setKeyValue(getActivity(), "isfd", "no");
                            }else if (is_house==-1)
                            {
                                renzheng.setText("房东认证失败");
                                StorageUtil.setKeyValue(getActivity(), "isfd", "no");
                                StorageUtil.setKeyValue(getActivity(), "role", "lsb");
                            }
                            username.setText(nickname);
                            if (is_name == 1)
                            {
                                StorageUtil.setKeyValue(getActivity(), "is_name", "isname");
                                renzheng.setText("已实名认证");
                            } else if (is_name == 2)
                            {
                                StorageUtil.setKeyValue(getActivity(), "is_name", "shz");
                                renzheng.setText("实名认证审核中");
                            }else if (is_name==-1)
                            {
                                renzheng.setText("实名认证失败");
                                StorageUtil.setKeyValue(getActivity(), "is_name", "shsb");
                            }

                            if (is_name==-1)
                            {
                                renzheng.setText("实名认证失败");
                            }
                            if (is_house==-1)
                            {
                                renzheng.setText("房东认证失败");
                            }
                            if (head_pic == null || head_pic.equals("null") || head_pic.equals(""))
                            {
                                defaultimg=head_pic;
                                Glide.with(App.getInstance())
                                        .load(R.mipmap.avatar_default)
                                        .into(userAvatar);
                            } else
                            {
                                if (head_pic.contains("http"))
                                {
                                    defaultimg=head_pic;
                                    StorageUtil.setKeyValue(getActivity(), "head_pic", head_pic);
                                    Glide.with(App.getInstance())
                                            .load(head_pic)
//                                    .placeholder(R.mipmap.ic_launcher)
                                            .into(userAvatar);
                                } else
                                {
                                    defaultimg=Constant.BASE2_URL + head_pic;
                                    StorageUtil.setKeyValue(getActivity(), "head_pic", Constant.BASE2_URL + head_pic);
                                    Glide.with(App.getInstance())
                                            .load(Constant.BASE2_URL + head_pic)
//                                    .placeholder(R.mipmap.ic_launcher)
                                            .into(userAvatar);
                                }
                            }
//                            RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
//
//                                @Override
//                                public UserInfo getUserInfo(String userId) {
//
//                                    return findUserById(userId);//根据 userId 去你的用户系统里查询对应的用户信息返回给融云 SDK。
//                                }
//
//                            }, true);
                            if (rongtoken==null||rongtoken.equals(""))
                            {
                                return;
                            }
                            StorageUtil.setKeyValue(getActivity(),"nickkk",nickname);
                            StorageUtil.setKeyValue(getActivity(),"imgkkk",defaultimg);
//
                            RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {

                                @Override
                                public UserInfo getUserInfo(String userId) {

                                    return new UserInfo(rongtoken,nickname, Uri.parse(defaultimg));//根据 userId 去你的用户系统里查询对应的用户信息返回给融云 SDK。
                                }

                            }, true);
                            RongIM.getInstance().refreshUserInfoCache(new UserInfo(rongtoken,
                                    nickname, Uri.parse(defaultimg)));




//                            UserInfo userInfo = new UserInfo(rongtoken,nickname, Uri.parse(defaultimg));
//                            RongIM.getInstance().setCurrentUserInfo(userInfo);
//                            RongIM.getInstance().setMessageAttachedUserInfo(true);

                        }
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

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.ll_order:
                startActivity(new Intent(getActivity(), OrderActivity.class));
                break;
            case R.id.ll_account://账户
                startActivityForResult(new Intent(getActivity(), AccountActivity.class),1);
                break;
            case R.id.ll_integral:
                startActivity(new Intent(getActivity(), IntegralActivity.class));
                break;
            case R.id.ll_coupon://优惠
                Intent intent = new Intent(getActivity(), CouponActivity.class);
                intent.putExtra("type", "");
                startActivity(intent);
                break;
            case R.id.ll_collect:
                startActivity(new Intent(getActivity(), CollectActivity.class));
                break;
            case R.id.img_setting:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.ll_help:
                startActivity(new Intent(getActivity(), HelpActivity.class));
                break;
            case R.id.user_avatar://头像
                startActivityForResult(new Intent(getActivity(), UserInfoActivity.class),11);
                break;
            case R.id.ll_house_resource:
                startActivity(new Intent(getActivity(), HouseResourceActivity.class));
                break;
            case R.id.landlord:

                if (is_house == 1)
                {
                    if (type==1)
                    {
                        roleInterface.changeRole(1);
                        type = 2;
                        landlord.setText("转变为房客");
                        StorageUtil.setKeyValue(getActivity(), "isfd", "yes");
                        EventBus.getDefault().post(new Isvisible(1));
                        xi.setVisibility(View.GONE);
                        xii.setVisibility(View.GONE);
                        xt.setVisibility(View.GONE);
                        xtt.setVisibility(View.GONE);
                        llOrder.setVisibility(View.GONE);
                        llCollect.setEnabled(false);
                        llCoupon.setEnabled(false);
                        llHouseResource.setVisibility(View.VISIBLE);
                    }else if (type==2){

                            roleInterface.changeRole(2);
                            type = 1;
                            EventBus.getDefault().post(new Isvisible(2));
                            landlord.setText("转变为房东");
                            xi.setVisibility(View.VISIBLE);
                            xii.setVisibility(View.VISIBLE);
                            xt.setVisibility(View.VISIBLE);
                            xtt.setVisibility(View.VISIBLE);
                            llCollect.setEnabled(true);
                            llCoupon.setEnabled(true);
                            StorageUtil.setKeyValue(getActivity(), "isfd", "no");
                            llOrder.setVisibility(View.VISIBLE);
                            llHouseResource.setVisibility(View.GONE);

                    }

                        //startActivity(new Intent(getActivity(),LandlordAuthenticationActivity.class));
                } else
                {
                  startActivityForResult(new Intent(getActivity(),LandlordAuthenticationActivity.class),101);
                }
                break;
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();
    }

    @Override
    public void onTranslucentChanged(int transAlpha)
    {
    }

    @Override
    public void onLeftClick()
    {

    }

    @Override
    public void onRightClick()
    {

    }

    @Override
    public void onResume()
    {
        super.onResume();
//        MinSuApi.userCenter(getActivity(), 0x001, token, callBack);
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

    public interface RoleInterface
    {

        void changeRole(int role);


    }

    private RoleInterface roleInterface;

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        if (activity instanceof RoleInterface)
        {
            roleInterface = (RoleInterface) activity;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        try
        {


            String is_name=StorageUtil.getValue(getActivity(),"is_name");
            if (is_name==null)
            {
                return;
            }
//            if (is_name.equals("shz"))//实名
//            {
//                renzheng.setText("实名认证审核中");
//            }else if (is_name.equals("isname"))
//            {
//                renzheng.setText("已实名认证");
//            } else if (is_name.equals("shsb"))
//            {
//                renzheng.setText("实名认证失败");
//            }
            MinSuApi.userCenterq(getActivity(), 0x001, token, callBack);
            String land=StorageUtil.getValue(getActivity(),"role");
//            if (land==null)
//            {
//                return;
//            }
//            if (land.equals("lsb"))
//            {
//                renzheng.setText("房东认证失败");
//            }else if (land.equals("landlord"))
//            {
//                renzheng.setText("已房东认证");
//            }else if (land.equals("user"))
//            {
//                renzheng.setText("房东认证审核中...");
//            }
//
//
//
//            if (is_name.equals("shsb"))
//            {
//                renzheng.setText("实名认证失败.");
//            }
//            if (is_name.equals("isname"))
//            {
//                renzheng.setText("已实名认证");
//            }
//            if (is_name.equals("shz"))
//            {
//                renzheng.setText("实名认证审核中...");
//            }
//            if (land.equals("landlord"))
//            {
//                renzheng.setText("已房东认证");
//            }
//            if (is_name.equals("shsb"))
//            {
//                renzheng.setText("实名认证失败.");
//            }
//            if (land.equals("lsb"))
//            {
//                renzheng.setText("房东认证失败");
//            }


            if (requestCode==101)
            {
                String landlord=StorageUtil.getValue(getActivity(),"role");
                StorageUtil.setKeyValue(getActivity(),"role","");
                if (landlord!=null&&landlord.equals("landlord"))
                {
                    statu();
                    String jiguang= StorageUtil.getValue(getActivity(),"jiguang");
                    JPushInterface.setAlias(getActivity(),0,"F"+jiguang);
                }
            }

        }catch (Exception e)
        {

        }
    }

    @Override
    public void onStop()
    {
        super.onStop();
    }

    @Override
    public void onPause()
    {
        super.onPause();
    }

    @Override
    public void onHiddenChanged(boolean hidden)
    {
        super.onHiddenChanged(hidden);
    }

    private void statu()
    {
        roleInterface.changeRole(1);
        type = 2;
        landlord.setText("转变为房客");
        StorageUtil.setKeyValue(getActivity(), "isfd", "yes");
        EventBus.getDefault().post(new Isvisible(1));
        xi.setVisibility(View.GONE);
        xii.setVisibility(View.GONE);
        xt.setVisibility(View.GONE);
        xtt.setVisibility(View.GONE);
        llOrder.setVisibility(View.GONE);
        llCollect.setEnabled(false);
        llCoupon.setEnabled(false);
        llHouseResource.setVisibility(View.VISIBLE);
    }
private Activity activity;
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        super.setUserVisibleHint(isVisibleToUser);
        if (activity!=null)
        {
            MinSuApi.userCenterq(getActivity(), 0x001, token, callBack);
        }
    }

    @Override
    public void onAttach(Context context)
    {
        this.activity= (Activity) context;
        super.onAttach(context);
    }
}
