package com.zhuye.minsu.api;

import android.app.Activity;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.zhuye.minsu.App;
import com.zhuye.minsu.api.callback.CallBack;
import com.zhuye.minsu.api.callback.StringDialogCallback;

import java.io.File;

/**
 * Created by hpc on 2017/12/25.
 */

public class MinSuApi {
    //获取验证码
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
                .params("city", city)
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
                .execute(new StringDialogCallback(activity,"加载中...") {
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
                                     File h_fan_pic,File h_fc_pic ,final CallBack myCallBack) {
        OkGo.<String>post(Constant.LANDLORD_SUBMIT_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("h_name", h_name)
                .params("h_card", h_card)
                .params("h_mobile", h_mobile)
                .params("h_address", h_address)
                .params("h_zheng_pic", h_zheng_pic)
                .params("h_fan_pic", h_fan_pic)
                .params("h_fc_pic",h_fc_pic)
                .execute(new StringDialogCallback(activity,"加载中...") {
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
    public static void renzhenPage(Activity activity,final int what, String token, final CallBack myCallBack) {
        OkGo.<String>post(Constant.SHIMING_PAGE_URL)
                .tag(App.getInstance())
                .params("token", token)
                .execute(new StringDialogCallback(activity,"加载中...") {
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
    public static void landlordPage(Activity activity,final int what, String token, final CallBack myCallBack) {
        OkGo.<String>post(Constant.LANDLORD_PAGE_URL)
                .tag(App.getInstance())
                .params("token", token)
                .execute(new StringDialogCallback(activity,"加载中...") {
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
