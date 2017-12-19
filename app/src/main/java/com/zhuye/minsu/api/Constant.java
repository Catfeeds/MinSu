package com.zhuye.minsu.api;

/**
 * Created by hpc on 2017/12/1.
 */

public class Constant {
    //基本URL
    public static final String BASE_URL = "http://www.minsu.com/api/";
    //手机发送验证码
    public static final String SEND_SMSCODE_URL = BASE_URL + "user/MobileVerify";
    //动态码登录
    public static final String DYNAMIC_CODE_URL = BASE_URL + "user/register";
    //账号密码登陆
    public static final String LOGIN_URL = BASE_URL + "user/logo1";
    //第三方登录
    public static final String THIRD_LOGIN_URL = BASE_URL + "user/logo2";
    //个人资料-昵称修改
    public static final String NICHEN_CHANGE_URL = BASE_URL + "user/name_user";
    //个人资料-性别修改
    public static final String NICHEN_SEX_URL = BASE_URL + "user/user_sex";
    //个人资料-生日修改
    public static final String NICHEN_BIRTHDAY_URL = BASE_URL + "user/user_birthday";
    //删除收货地址
    public static final String DELETE_ADDRESS_URL = BASE_URL + "user/del_address";
    //编辑收货地址页面
    public static final String EDIT_ADDRESS_URL = BASE_URL + "user/edit_address";
    //设置默认收货地址
    public static final String SET_DEFAULT_ADDRESS_URL = BASE_URL + "user/set_default";
    //新增收货地址
    public static final String ADD_ADDRESS_URL = BASE_URL + "user/add_address";
    //送货地址列表
    public static final String SEND_ADDRESS_LIST_URL = BASE_URL + "user/address_list";
    //编辑收货地址提交
    public static final String EDIT_ADDRESS_SUBMIT_URL = BASE_URL + "user/edit_cz_address";
    //签到页面
    public static final String SIGN_PAGE_URL = BASE_URL + "user/sign_html";
    //点击签到
    public static final String SIGN_URL = BASE_URL + "user/sign";


    /*--------------------------------------------发现模块---------------------------------------------*/
    //文章详情
    public static final String ARTICLE_DETAIL_URL = BASE_URL + "Article/article_detail";
    //文章-评论回复
    public static final String ARTICLE_COMMENT_REPLY_URL = BASE_URL + "Article/com_hf";
    //文章-点赞
    public static final String DIANZAN_URL = BASE_URL + "Article/article_nice";
    //文章-取消点赞
    public static final String DIANZAN_CANCEL_URL = BASE_URL + "Article/article_qx_nice";
    //文章-文章-发表评论
    public static final String COMMENT_URL = BASE_URL + "Article/article_com";
    //游记列表
    public static final String YOUJI_LIST_URL = BASE_URL + "Article/youji";
    //攻略列表
    public static final String GONGLUE_LIST_URL = BASE_URL + "Article/gonglue";
    //积分兑换列表
    public static final String JIFEN_LIST_URL = BASE_URL + "user/jifen_list";
    //个人积分明细
    public static final String JIFEN_DETAIL_URL = BASE_URL + "user/sign_detail";
    //添加文章
    public static final String ADD_ARTICLE_URL = BASE_URL + "Article/add_article";
    //发现列表
    public static final String FIND_LIST_URL = BASE_URL + "Article/faxian";
}
