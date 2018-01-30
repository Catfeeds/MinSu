package com.minsu.minsu.api;

import android.app.Activity;
import android.util.Log;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.minsu.minsu.App;
import com.minsu.minsu.api.callback.CallBack;
import com.minsu.minsu.api.callback.StringDialogCallback;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by hpc on 2017/12/25.
 */

public class MinSuApi {
    private static final String TAG = "MinSuApi";

    //登录
    public static void codeLogin(Activity activity, final int what, String mobile, String password, String verify, final CallBack myCallBack) {
        OkGo.<String>post(Constant.DYNAMIC_CODE_URL)
                .tag(App.getInstance())
                .params("mobile", mobile)
                .params("password", password)
                .params("verify", verify)
                .execute(new StringDialogCallback(activity, "登录中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //获取验证码
    public static void getSmsCode(final int what, String phone, int type, final CallBack myCallBack) {
        OkGo.<String>post(Constant.SEND_SMSCODE_URL)
                .tag(App.getInstance())
                .params("phone", phone)
                .params("type", type)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //密码登录
    public static void Login(Activity activity, final int what, String mobile, String password, final CallBack myCallBack) {
        OkGo.<String>post(Constant.LOGIN_URL)
                .tag(App.getInstance())
                .params("mobile", mobile)
                .params("password", password)
                .execute(new StringDialogCallback(activity, "登录中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //系统消息
    public static void SystemMessage(Activity activity, final int what, final CallBack myCallBack) {
        OkGo.<String>post(Constant.SYSTEM_MESSAGE_URL)
                .tag(App.getInstance())
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //添加地址
    public static void addAddress(Activity activity, final int what, String token, String name,
                                  String mobile, String address, final CallBack myCallBack) {
        OkGo.<String>post(Constant.ADD_ADDRESS_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("name", name)
                .params("mobile", mobile)
                .params("address", address)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //地址列表
    public static void addressList(Activity activity, final int what, String token, final CallBack myCallBack) {
        OkGo.<String>post(Constant.SEND_ADDRESS_LIST_URL)
                .tag(App.getInstance())
                .params("token", token)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //编辑收货地址页面
    public static void editAddress(Activity activity, final int what, String token, int address_id, final CallBack myCallBack) {
        OkGo.<String>post(Constant.EDIT_ADDRESS_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("address_id", address_id)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //编辑收货地址页面
    public static void editAddressSubmit(Activity activity, final int what, String token, int address_id,
                                         String name, String mobile, String address, final CallBack myCallBack) {
        OkGo.<String>post(Constant.EDIT_ADDRESS_SUBMIT_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("address_id", address_id)
                .params("name", name)
                .params("mobile", mobile)
                .params("address", address)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //删除地址
    public static void deleteAddress(Activity activity, final int what, String token, int address_id,
                                     final CallBack myCallBack) {
        OkGo.<String>post(Constant.DELETE_ADDRESS_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("address_id", address_id)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //首页数据展示
    public static void homeShow(Activity activity, final int what, String token, String city,
                                final CallBack myCallBack) {
        OkGo.<String>post(Constant.HOME_PAGE_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("city_name", city)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //个人中心
    public static void userCenter(Activity activity, final int what, String token, final CallBack myCallBack) {
        OkGo.<String>post(Constant.USER_CENTER_URL)
                .tag(App.getInstance())
                .params("token", token)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //个人资料
    public static void userInfo(Activity activity, final int what, String token, final CallBack myCallBack) {
        OkGo.<String>post(Constant.USER_INFO_URL)
                .tag(App.getInstance())
                .params("token", token)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //性别修改
    public static void sexChange(final int what, String token, int sex, final CallBack myCallBack) {
        OkGo.<String>post(Constant.USER_SEX_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("sex", sex)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //生日修改
    public static void birthdayChange(final int what, String token, String birthday, final CallBack myCallBack) {
        OkGo.<String>post(Constant.USER_BIRTHDAY_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("birthday", birthday)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //昵称修改
    public static void nicknameChange(final int what, String token, String nickname, final CallBack myCallBack) {
        OkGo.<String>post(Constant.USER_NICKNAME_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("nickname", nickname)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //邮箱修改
    public static void emailChange(final int what, String token, String email, final CallBack myCallBack) {
        OkGo.<String>post(Constant.USER_EMAIL_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("email", email)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //实名认证提交
    public static void shimingSubmit(Activity activity, final int what, String token, String n_name,
                                     String n_card, String n_mobile, String n_address, File n_zheng_pic,
                                     File n_fan_pic, final CallBack myCallBack) {
        OkGo.<String>post(Constant.SHIMING_SUBMIT_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("n_name", n_name)
                .params("n_card", n_card)
                .params("n_mobile", n_mobile)
                .params("n_address", n_address)
                .params("n_zheng_pic", n_zheng_pic)
                .params("n_fan_pic", n_fan_pic)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }


    //房东认证提交
    public static void landlordSubmit(Activity activity, final int what, String token, String h_name,
                                      String h_card, String h_mobile, String h_address, File h_zheng_pic,
                                      File h_fan_pic, File h_fc_pic, final CallBack myCallBack) {
        OkGo.<String>post(Constant.LANDLORD_SUBMIT_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("h_name", h_name)
                .params("h_card", h_card)
                .params("h_mobile", h_mobile)
                .params("h_address", h_address)
                .params("h_zheng_pic", h_zheng_pic)
                .params("h_fan_pic", h_fan_pic)
                .params("h_fc_pic", h_fc_pic)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //实名认证页面
    public static void renzhenPage(Activity activity, final int what, String token, final CallBack myCallBack) {
        OkGo.<String>post(Constant.SHIMING_PAGE_URL)
                .tag(App.getInstance())
                .params("token", token)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //房东认证页面
    public static void landlordPage(Activity activity, final int what, String token, final CallBack myCallBack) {
        OkGo.<String>post(Constant.LANDLORD_PAGE_URL)
                .tag(App.getInstance())
                .params("token", token)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //我的房源
    public static void myHouseResource(Activity activity, final int what, String token, final CallBack myCallBack) {
        OkGo.<String>post(Constant.MY_HOUSE_LIST_URL)
                .tag(App.getInstance())
                .params("token", token)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //头像修改
    public static void avatarChange(Activity activity, final int what, String token, File head_pic, final CallBack myCallBack) {
        OkGo.<String>post(Constant.USER_AVATAR_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("file", head_pic)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //帮助
    public static void help(Activity activity, final int what, String token, final CallBack myCallBack) {
        OkGo.<String>post(Constant.HELP_URL)
                .tag(App.getInstance())
                .params("token", token)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //关于
    public static void aboutUs(Activity activity, final int what, String token, final CallBack myCallBack) {
        OkGo.<String>post(Constant.ABOUT_US_URL)
                .tag(App.getInstance())
                .params("token", token)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //反馈
    public static void feedback(Activity activity, final int what, String token,
                                String content, String mobile, String email, final CallBack myCallBack) {
        OkGo.<String>post(Constant.FEEDBACK_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("content", content)
                .params("mobile", mobile)
                .params("email", email)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //添加房源页面
    public static void addHouse(final int what, String token, final CallBack myCallBack) {
        OkGo.<String>post(Constant.ADD_HOUSE_URL)
                .tag(App.getInstance())
                .params("token", token)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                        Log.i(TAG, "onSuccess: " + response.body());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                        Log.i(TAG, "onSuccess: " + response.body());
                    }
                });
    }

    //添加房源提交
    public static void addHouseSubmit(Activity activity, final int what, String token,
                                      int house_type_id, int space_type_id, String title,
                                      String house_info, String house_price, String status,
                                      int province, int city, int district, int town,
                                      ArrayList<File> house_img, final CallBack myCallBack) {
        OkGo.<String>post(Constant.ADD_HOUSE_SUBMIT_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("house_type_id", house_type_id)
                .params("space_type_id", space_type_id)
                .params("title", title)
                .params("house_info", house_info)
                .params("house_price", house_price)
                .params("status", status)
                .params("province", province)
                .params("city", city)
                .params("district", district)
                .params("town", town)
                .addFileParams("house_img[]", house_img)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //获取城市
    public static void getCity(final int what, String token, int province, final CallBack myCallBack) {
        OkGo.<String>post(Constant.CITY_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("province", province)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                        Log.i(TAG, "onSuccess: " + response.body());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                        Log.i(TAG, "onSuccess: " + response.body());
                    }
                });
    }

    //获取地区
    public static void getArea(final int what, String token, int city, final CallBack myCallBack) {
        OkGo.<String>post(Constant.AREA_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("city", city)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                        Log.i(TAG, "onSuccess: " + response.body());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                        Log.i(TAG, "onSuccess: " + response.body());
                    }
                });
    }

    //获取乡镇
    public static void gettown(final int what, String token, int district, final CallBack myCallBack) {
        OkGo.<String>post(Constant.TOWN_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("district", district)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                        Log.i(TAG, "onSuccess: " + response.body());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                        Log.i(TAG, "onSuccess: " + response.body());
                    }
                });
    }

    //添加收藏
    public static void addCollect(final int what, String token, String house_id, final CallBack myCallBack) {
        OkGo.<String>post(Constant.COLLECT_ADD_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("house_id", house_id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //取消收藏
    public static void cancelCollect(final int what, String token, String house_id, final CallBack myCallBack) {
        OkGo.<String>post(Constant.COLLECT_CANCEL_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("house_id", house_id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //我的收藏
    public static void myCollect(Activity activity, final int what, String token, final CallBack myCallBack) {
        OkGo.<String>post(Constant.MY_COLLECT_URL)
                .tag(App.getInstance())
                .params("token", token)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //签到页面
    public static void signPage(Activity activity, final int what, String token, final CallBack myCallBack) {
        OkGo.<String>post(Constant.SIGN_PAGE_URL)
                .tag(App.getInstance())
                .params("token", token)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //点击签到
    public static void clickSign(Activity activity, final int what, String token, final CallBack myCallBack) {
        OkGo.<String>post(Constant.SIGN_URL)
                .tag(App.getInstance())
                .params("token", token)
                .execute(new StringDialogCallback(activity, "签到中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //我的优惠券列表
    public static void myCouponList(Activity activity, final int what, String token, final CallBack myCallBack) {
        OkGo.<String>post(Constant.MY_COUPON_LIST_URL)
                .tag(App.getInstance())
                .params("token", token)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //领取优惠券
    public static void getCoupon(final int what, String token, int quan_id, final CallBack myCallBack) {
        OkGo.<String>post(Constant.GET_COUPON_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("quan_id", quan_id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //优惠券列表
    public static void CouponList(Activity activity, final int what, String token, final CallBack myCallBack) {
        OkGo.<String>post(Constant.COUPON_LIST_URL)
                .tag(App.getInstance())
                .params("token", token)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //订单提示
    public static void orderPrompt(Activity activity, final int what, String token, final CallBack myCallBack) {
        OkGo.<String>post(Constant.ORDER_PROMPT_URL)
                .tag(App.getInstance())
                .params("token", token)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //房源详情
    public static void houseDetail(Activity activity, final int what, String token, String house_id, final CallBack myCallBack) {
        OkGo.<String>post(Constant.HOUSE_DETAIL_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("house_id", house_id)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //房源列表
    public static void roomList(Activity activity, final int what, String token, String city,
                                String qy_id, String price_desc, String price_asc, String house_type_id,
                                String title, final CallBack myCallBack) {
        OkGo.<String>post(Constant.ROMM_RESOURCE_LIST_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("city", city)
                .params("qy_id", qy_id)
                .params("price_desc", price_desc)
                .params("price_asc", price_asc)
                .params("house_type_id", house_type_id)
                .params("title", title)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //第三方登录
    public static void thirdLogin(Activity activity, final int what, String openid, String nickname, String head_pic, final CallBack myCallBack) {
        OkGo.<String>post(Constant.THIRD_LOGIN_URL)
                .tag(App.getInstance())
                .params("openid", openid)
                .params("nickname", nickname)
                .params("head_pic", head_pic)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //修改密码
    public static void changePassword(Activity activity, final int what, String token, String pwd_old, String pwd_new, final CallBack myCallBack) {
        OkGo.<String>post(Constant.CHANGE_PASSWORD_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("pwd_old", pwd_old)
                .params("pwd_new", pwd_new)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //修改密码
    public static void forgetPassword(Activity activity, final int what, String mobile, String verify, String password, final CallBack myCallBack) {
        OkGo.<String>post(Constant.FORGET_PASSWORD_URL)
                .tag(App.getInstance())
                .params("mobile", mobile)
                .params("verify", verify)
                .params("password", password)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //订单确认提交
    public static void orderSubmit(Activity activity, final int what, String token, int house_id,
                                   String check_time, String leave_time, int days, String house_price, final CallBack myCallBack) {
        OkGo.<String>post(Constant.ORDER_SUBMIT_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("house_id", house_id)
                .params("check_time", check_time)
                .params("leave_time", leave_time)
                .params("days", days)
                .params("house_price", house_price)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //订单支付页面
    public static void orderPayPage(Activity activity, final int what, String token, String order_id, final CallBack myCallBack) {
        OkGo.<String>post(Constant.ORDER_PAY_PAGE_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("order_id", order_id)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //我的全部订单列表
    public static void allMyOrder(Activity activity, final int what, String token, final CallBack myCallBack) {
        OkGo.<String>post(Constant.ALL_MY_ORDER_URL)
                .tag(App.getInstance())
                .params("token", token)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //订单列表-入住中
    public static void ruzhuzhongOrder(Activity activity, final int what, String token, final CallBack myCallBack) {
        OkGo.<String>post(Constant.RUZHUZHONG_ORDER_URL)
                .tag(App.getInstance())
                .params("token", token)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //订单列表-已退房
    public static void yituifaangOrder(Activity activity, final int what, String token, final CallBack myCallBack) {
        OkGo.<String>post(Constant.YITUIFANG_ORDER_URL)
                .tag(App.getInstance())
                .params("token", token)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //订单取消
    public static void cancelOrder(Activity activity, final int what, String token, int order_id, final CallBack myCallBack) {
        OkGo.<String>post(Constant.ORDER_CANCEL_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("order_id", order_id)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //订单删除
    public static void deleteOrder(Activity activity, final int what, String token, int order_id, final CallBack myCallBack) {
        OkGo.<String>post(Constant.ORDER_DELETE_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("order_id", order_id)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //提交退款
    public static void submitTuikuan(Activity activity, final int what, String token, int order_id, String tuikuan_txt,
                                     String tuikuan_type, final CallBack myCallBack) {
        OkGo.<String>post(Constant.SUBMIT_TUIKUAN_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("order_id", order_id)
                .params("tuikuan_txt", tuikuan_txt)
                .params("tuikuan_type", tuikuan_type)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //提交退款
    public static void submitTuifang(Activity activity, final int what, String token, int order_id, String tuikuan_txt,
                                     String tuikuan_type, final CallBack myCallBack) {
        OkGo.<String>post(Constant.SUBMIT_TUIKUAN_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("order_id", order_id)
                .params("tuikuan_txt", tuikuan_txt)
                .params("tuikuan_type", tuikuan_type)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //申请退款页面
    public static void applyTuikuanPage(Activity activity, final int what, String token, int order_id, final CallBack myCallBack) {
        OkGo.<String>post(Constant.APPLY_TUIKUAN_PAGE_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("order_id", order_id)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //提前退房申请页面
    public static void applyTuifangPage(Activity activity, final int what, String token, int order_id, final CallBack myCallBack) {
        OkGo.<String>post(Constant.APPLY_TUIFANG_PAGE_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("order_id", order_id)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //城市选择
    public static void citySelect(Activity activity, final int what, String token, final CallBack myCallBack) {
        OkGo.<String>post(Constant.CITY_SELECT_URL)
                .tag(App.getInstance())
                .params("token", token)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //订单列表-待入住
    public static void dairuzhuOrder(Activity activity, final int what, String token, final CallBack myCallBack) {
        OkGo.<String>post(Constant.DAIRUZHU_ORDER_URL)
                .tag(App.getInstance())
                .params("token", token)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //聊天列表
    public static void chatList(Activity activity, final int what, String token, final CallBack myCallBack) {
        OkGo.<String>post(Constant.CHAT_LIST_URL)
                .tag(App.getInstance())
                .params("token", token)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //添加聊天列表
    public static void addChatList(Activity activity, final int what, String token, int js_user_id, final CallBack myCallBack) {
        OkGo.<String>post(Constant.ADD_CHAT_LIST_URL)
                .tag(App.getInstance())
                .params("fs_token", token)
                .params("js_user_id", js_user_id)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //房东全部订单
    public static void lanlordAllOrderList(Activity activity, final int what, String token, final CallBack myCallBack) {
        OkGo.<String>post(Constant.LANDLORD_ORDER_URL)
                .tag(App.getInstance())
                .params("token", token)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //房东待入住
    public static void lanlordDairuzhuOrderList(Activity activity, final int what, String token, final CallBack myCallBack) {
        OkGo.<String>post(Constant.LANDLORD_DAIRUZHU_ORDER_URL)
                .tag(App.getInstance())
                .params("token", token)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //房东入住中
    public static void lanlordRuzhuzhongOrderList(Activity activity, final int what, String token, final CallBack myCallBack) {
        OkGo.<String>post(Constant.LANDLORD_RUZHUZHONG_ORDER_URL)
                .tag(App.getInstance())
                .params("token", token)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }


    //改变订单状态
    public static void changeOrderStatus(final int what, String token, String order_id,
                                         String id, final CallBack myCallBack) {
        OkGo.<String>post(Constant.CHANGE_ORDER_STATUS_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("order_id", order_id)
                .params("id", id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //获取融云token
    public static void getRongyunToken(Activity activity, final int what, String token,
                                       int id, final CallBack myCallBack) {
        OkGo.<String>post(Constant.GET_RONGYUN_TOKEN_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("id", id)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //房东确认入住
    public static void sureRuzhu(Activity activity, final int what, String token,
                                 int order_id, final CallBack myCallBack) {
        OkGo.<String>post(Constant.LANDLORD_SURE_RUZHU_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("order_id", order_id)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //房东确认退房
    public static void sureTuiFang(Activity activity, final int what, String token,
                                   int order_id, final CallBack myCallBack) {
        OkGo.<String>post(Constant.LANDLORD_SURE_TUIFANG_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("order_id", order_id)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //(房东)我的订单-退款申请
    public static void tuikuanApply(Activity activity, final int what, String token, final CallBack myCallBack) {
        OkGo.<String>post(Constant.LANDLORD_TUIKUAN_APPLY_URL)
                .tag(App.getInstance())
                .params("token", token)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //(房东)确认退款
    public static void sureTuiKuan(Activity activity, final int what, String token, int order_id, int is_tuikuan, final CallBack myCallBack) {
        OkGo.<String>post(Constant.LANDLORD_SURE_TUIKUAN_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("order_id", order_id)
                .params("is_tuikuan", is_tuikuan)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //(房东)我的订单-提前退房
    public static void tiqianTuiFang(Activity activity, final int what, String token, final CallBack myCallBack) {
        OkGo.<String>post(Constant.LANDLORD_TIQIAN_TUIFANG_URL)
                .tag(App.getInstance())
                .params("token", token)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //(房东)确认提前退房
    public static void sureTiQianTuiFang(Activity activity, final int what, String token, int order_id, int is_tuifang, final CallBack myCallBack) {
        OkGo.<String>post(Constant.LANDLORD_SURE_TIQIAN_TUIFANG_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("order_id", order_id)
                .params("is_tuifang", is_tuifang)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //(房东)我的订单-已退房
    public static void landlordYiTuiFang(Activity activity, final int what, String token, final CallBack myCallBack) {
        OkGo.<String>post(Constant.LANDLORD_YITUIFANG_ORDER_URL)
                .tag(App.getInstance())
                .params("token", token)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //我的银行卡列表
    public static void bankcardList(Activity activity, final int what, String token, final CallBack myCallBack) {
        OkGo.<String>post(Constant.MY_BANKCARD_LIST_URL)
                .tag(App.getInstance())
                .params("token", token)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //我的余额
    public static void myYue(Activity activity, final int what, String token, final CallBack myCallBack) {
        OkGo.<String>post(Constant.MY_YUE_URL)
                .tag(App.getInstance())
                .params("token", token)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //添加银行卡
    public static void addbankCard(Activity activity, final int what, String token,
                                   String verify, String bank_code, String bank_name,
                                   String name, String name_code, String phone, final CallBack myCallBack) {
        OkGo.<String>post(Constant.ADD_BANKCARD_LIST_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("verify", verify)
                .params("bank_code", bank_code)
                .params("bank_name", bank_name)
                .params("name", name)
                .params("name_code", name_code)
                .params("phone", phone)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //提现申请确认
    public static void tixianApply(Activity activity, final int what, String token, String bank_id, String price,String tx_bl, final CallBack myCallBack) {
        OkGo.<String>post(Constant.TIXIAN_APPLY_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("bank_id", bank_id)
                .params("price", price)
                .params("tx_bl", tx_bl)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //房东收支记录
    public static void landlordShouzhi(Activity activity, final int what, String token, String log_type, final CallBack myCallBack) {
        OkGo.<String>post(Constant.LANDLORD_SHOUZHI_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("log_type", log_type)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //房源评价列表
    public static void roomCommentList(Activity activity, final int what, String token, String house_id, final CallBack myCallBack) {
        OkGo.<String>post(Constant.COMMENT_LIST_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("house_id", house_id)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //房源评价点赞
    public static void roomComment(Activity activity, final int what, String token, String content, int house_id, final CallBack myCallBack) {
        OkGo.<String>post(Constant.COMMENT_ORDER_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("content", content)
                .params("house_id", house_id)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //房源评价点赞
    public static void roomDianzan(final int what, String token, int comment_id, final CallBack myCallBack) {
        OkGo.<String>post(Constant.CLICK_DIANZAN_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("comment_id", comment_id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //房源评价取消点赞
    public static void roomCancelDianzan(final int what, String token, int comment_id, final CallBack myCallBack) {
        OkGo.<String>post(Constant.CANCEL_DIANZAN_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("comment_id", comment_id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //房源评价回复页面
    public static void replyPage(final int what, String token, int comment_id, final CallBack myCallBack) {
        OkGo.<String>post(Constant.COMMENT_REPLY_PAGE_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("comment_id", comment_id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //订单（房源）评价回复
    public static void commentReply(final int what, String token, int comment_id, String content, final CallBack myCallBack) {
        OkGo.<String>post(Constant.COMMENT_REPLY_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("comment_id", comment_id)
                .params("content", content)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //常用旅客列表
    public static void passengerList(Activity activity, final int what, String token, final CallBack myCallBack) {
        OkGo.<String>post(Constant.PASSENGER_LIST_URL)
                .tag(App.getInstance())
                .params("token", token)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //常用旅客列表
    public static void addPassenger(Activity activity, final int what, String token, String name, String zj_type,
                                    String zj_code, String lk_type, final CallBack myCallBack) {
        OkGo.<String>post(Constant.ADD_PASSENGER_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("name", name)
                .params("zj_type", zj_type)
                .params("zj_code", zj_code)
                .params("lk_type", lk_type)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //常用旅客编辑页面
    public static void editPassengerPage(Activity activity, final int what, String token, int id, final CallBack myCallBack) {
        OkGo.<String>post(Constant.EDIT_PASSENGER_PAGE_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("id", id)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //常用旅客编辑
    public static void editPassenger(Activity activity, final int what, String token, int id,
                                     String name, String zj_type,
                                     String zj_code, String lk_type, final CallBack myCallBack) {
        OkGo.<String>post(Constant.EDIT_PASSENGER_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("id", id)
                .params("name", name)
                .params("zj_type", zj_type)
                .params("zj_code", zj_code)
                .params("lk_type", lk_type)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //常用旅客删除
    public static void deletePassenger(Activity activity, final int what, String token, int id, final CallBack myCallBack) {
        OkGo.<String>post(Constant.DELETE_PASSENGER_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("id", id)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //已取消订单列表
    public static void yiquxiao(Activity activity, final int what, String token, final CallBack myCallBack) {
        OkGo.<String>post(Constant.YIQUXIAO_ORDER_URL)
                .tag(App.getInstance())
                .params("token", token)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //订单列表-提前退房
    public static void tiqianTuifang(Activity activity, final int what, String token, final CallBack myCallBack) {
        OkGo.<String>post(Constant.TUIFANG_ORDER_URL)
                .tag(App.getInstance())
                .params("token", token)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //微信支付
    public static void weixinPay(Activity activity, final int what, String token, String order_id, int youhui_price, final CallBack myCallBack) {
        OkGo.<String>post(Constant.WEXIN_PAY_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("order_id", order_id)
                .params("youhui_price", youhui_price)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //阿里支付
    public static void aliPay(Activity activity, final int what, String token, String order_id, int youhui_price, final CallBack myCallBack) {
        OkGo.<String>post(Constant.ALI_PAY_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("order_id", order_id)
                .params("youhui_price", youhui_price)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //提现页面
    public static void tixianPage(Activity activity, final int what, String token, final CallBack myCallBack) {
        OkGo.<String>post(Constant.TIXIAN_PAGE_URL)
                .tag(App.getInstance())
                .params("token", token)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }
}
