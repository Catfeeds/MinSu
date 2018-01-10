package com.zhuye.minsu.api;

/**
 * Created by hpc on 2017/12/1.
 */

public class Constant {
    //基本URL
//    public static final String BASE_URL = "http://www.minsu.com/api/";
    public static final String BASE_URL = "http://192.168.1.30/api/";
    public static final String BASE2_URL = "http://192.168.1.30";
    //手机发送验证码
    public static final String SEND_SMSCODE_URL = BASE_URL + "user/MobileVerify";
    //动态码登录
    public static final String DYNAMIC_CODE_URL = BASE_URL + "user/register";
    //账号密码登陆
    public static final String LOGIN_URL = BASE_URL + "user/logo1";
    //第三方登录
    public static final String THIRD_LOGIN_URL = BASE_URL + "user/logo2";
    //个人中心
    public static final String USER_CENTER_URL = BASE_URL + "user/index";
    //用户个人资料
    public static final String USER_INFO_URL = BASE_URL + "user/user_info";
    //个人资料-性别修改
    public static final String USER_SEX_URL = BASE_URL + "user/user_sex";
    //个人资料-昵称修改
    public static final String USER_NICKNAME_URL = BASE_URL + "user/user_nickname";
    //个人资料-生日修改
    public static final String USER_BIRTHDAY_URL = BASE_URL + "user/user_birthday";
    //个人资料-邮箱修改
    public static final String USER_EMAIL_URL = BASE_URL + "user/user_email";
    //个人资料-头像修改
    public static final String USER_AVATAR_URL = BASE_URL + "user/user_head_pic";
    //实名认证提交
    public static final String SHIMING_SUBMIT_URL = BASE_URL + "user/add_name_user";
    //实名认证页面数据
    public static final String SHIMING_PAGE_URL = BASE_URL + "user/name_user";
    //房东认证提交
    public static final String LANDLORD_SUBMIT_URL = BASE_URL + "user/add_house_user";
    //房东认证页面数据
    public static final String LANDLORD_PAGE_URL = BASE_URL + "user/house_user";
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
    //系统消息
    public static final String SYSTEM_MESSAGE_URL = BASE_URL + "Message/system_msg";
    //用户端-首页
    public static final String HOME_PAGE_URL = BASE_URL + "index/index";
    //帮助
    public static final String HELP_URL = BASE_URL + "user/bangzhu";
    //关于
    public static final String ABOUT_US_URL = BASE_URL + "user/guanyu";
    //反馈
    public static final String FEEDBACK_URL = BASE_URL + "user/user_fankui";
    //取消收藏
    public static final String COLLECT_CANCEL_URL = BASE_URL + "user/qx_collect";
    //添加收藏
    public static final String COLLECT_ADD_URL = BASE_URL + "user/add_collect";
    //我的收藏
    public static final String MY_COLLECT_URL = BASE_URL + "user/mycollect";
    //优惠券列表
    public static final String COUPON_LIST_URL = BASE_URL + "quan/quan_list";
    //领取优惠券
    public static final String GET_COUPON_URL = BASE_URL + "quan/get_quan";
    //我的优惠券
    public static final String MY_COUPON_LIST_URL = BASE_URL + "quan/my_quan";
    //订单提示
    public static final String ORDER_PROMPT_URL = BASE_URL + "Message/order_msg";
    //房源详情
    public static final String HOUSE_DETAIL_URL = BASE_URL + "House/house_detail";
    //房源列表
    public static final String ROMM_RESOURCE_LIST_URL = BASE_URL + "House/index";

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


    //----------------------房东端------------------------------------------------
    //我的房源
    public static final String MY_HOUSE_LIST_URL = BASE_URL + "Fangdong/my_house";
    //添加房源页面
    public static final String ADD_HOUSE_URL = BASE_URL + "Fangdong/add_house";
    //添加房源提交
    public static final String ADD_HOUSE_SUBMIT_URL = BASE_URL + "Fangdong/add_house_cz";
    //市-信息
    public static final String CITY_URL = BASE_URL + "fangdong/city";
    //地区-信息
    public static final String AREA_URL = BASE_URL + "fangdong/district";
    //乡镇-信息
    public static final String TOWN_URL = BASE_URL + "fangdong/twon";


    //---------------------------前端-------------------------------------------------
    //积分
    public static final String INTEGRAL_URL = "http://192.168.1.36:8020/%E6%B0%91%E5%AE%BF_reg/integral.html?__hbt=1515133178500";

}
