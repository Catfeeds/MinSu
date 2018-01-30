package com.minsu.minsu.api;

/**
 * Created by hpc on 2017/12/1.
 */

public class Constant {
    //基本URL
//    public static final String BASE_URL = "http://www.minsu.com/api/";
//    public static final String BASE_URL = "http://192.168.1.30/api/";
//    public static final String BASE2_URL = "http://192.168.1.30";
    public static final String BASE_URL = "http://minsu.zyeo.net/api/";
    public static final String BASE2_URL = "http://minsu.zyeo.net/";
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
    //修改密码
    public static final String CHANGE_PASSWORD_URL = BASE_URL + "user/edit_pwd";
    //忘记密码
    public static final String FORGET_PASSWORD_URL = BASE_URL + "user/wj_pwd";
    //订单确认提交
    public static final String ORDER_SUBMIT_URL = BASE_URL + "order/order_sub";
    //取消订单
    public static final String ORDER_CANCEL_URL = BASE_URL + "order/qx_order";
    //提交退款
    public static final String SUBMIT_TUIKUAN_URL = BASE_URL + "order/tuikuan_cz";
    //提前退房提交
    public static final String SUBMIT_TUIFANG_URL = BASE_URL + "order/tuifang_cz";
    //申请退款页面
    public static final String APPLY_TUIKUAN_PAGE_URL = BASE_URL + "order/tuikuan_html";
    //提前退房申请页面
    public static final String APPLY_TUIFANG_PAGE_URL = BASE_URL + "order/tuifang_html";
    //删除订单
    public static final String ORDER_DELETE_URL = BASE_URL + "order/del_order";
    //订单支付页面
    public static final String ORDER_PAY_PAGE_URL = BASE_URL + "order/order_zhifu";
    //我的全部订单列表
    public static final String ALL_MY_ORDER_URL = BASE_URL + "order/my_order";
    //订单列表-入住中
    public static final String RUZHUZHONG_ORDER_URL = BASE_URL + "order/order_rzz";
    //订单列表-已退房
    public static final String YITUIFANG_ORDER_URL = BASE_URL + "order/order_ytf";
    //订单列表-待入住
    public static final String DAIRUZHU_ORDER_URL = BASE_URL + "order/order_drz";
    //订单列表-已取消
    public static final String YIQUXIAO_ORDER_URL = BASE_URL + "order/order_yqx";
    //订单列表-提前退房
    public static final String TUIFANG_ORDER_URL = BASE_URL + "order/order_tqtf";
    //城市搜索
    public static final String CITY_SEARCH_URL = BASE_URL + "index/cs_sousuo";
    //选择城市
    public static final String CITY_SELECT_URL = BASE_URL + "index/chengshi";
    //聊天列表
    public static final String CHAT_LIST_URL = BASE_URL + "message/chat_msg";
    //添加聊天列表
    public static final String ADD_CHAT_LIST_URL = BASE_URL + "Message/add_chat";
    //支付成功-修改订单状态
    public static final String CHANGE_ORDER_STATUS_URL = BASE_URL + "order/zhifu_cg";
    //聊天用户token
    public static final String GET_RONGYUN_TOKEN_URL = BASE_URL + "message/get_token";
    //房源评价列表
    public static final String COMMENT_LIST_URL = BASE_URL + "house/com_list";
    //房源评价取消点赞
    public static final String CANCEL_DIANZAN_URL = BASE_URL + "House/house_com_qx_nice";
    //房源评价点赞
    public static final String CLICK_DIANZAN_URL = BASE_URL + "House/house_com_nice";
    //房源评价回复页面
    public static final String COMMENT_REPLY_PAGE_URL = BASE_URL + "house/com_hf_list";
    //订单（房源）评价回复
    public static final String COMMENT_REPLY_URL = BASE_URL + "order/order_com_hf";
    //订单（房源）评价
    public static final String COMMENT_ORDER_URL = BASE_URL + "order/order_com";
    //常用旅客列表
    public static final String PASSENGER_LIST_URL = BASE_URL + "user/lk_list";
    //新增常用旅客
    public static final String ADD_PASSENGER_URL = BASE_URL + "user/add_lk";
    //常用旅客编辑页面
    public static final String EDIT_PASSENGER_PAGE_URL = BASE_URL + "user/edit_lk_html";
    //常用旅客编辑提交
    public static final String EDIT_PASSENGER_URL = BASE_URL + "user/edit_lk";
    //常用旅客删除
    public static final String DELETE_PASSENGER_URL = BASE_URL + "user/del_lk";
    //微信支付
    public static final String WEXIN_PAY_URL = BASE_URL + "payment/weixin_payment";
    //支付宝支付
    public static final String ALI_PAY_URL = BASE_URL + "payment/alipay_payment";

    public static final String WX_APP_ID = "wxa372aef05ce3769b";


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
    //(房东)我的订单
    public static final String LANDLORD_ORDER_URL = BASE_URL + "Fangdong/fd_order";
    //(房东)待入住
    public static final String LANDLORD_DAIRUZHU_ORDER_URL = BASE_URL + "Fangdong/fd_order_drz";
    //(房东)入住中
    public static final String LANDLORD_RUZHUZHONG_ORDER_URL = BASE_URL + "Fangdong/fd_order_rzz";
    //(房东)已退房
    public static final String LANDLORD_YITUIFANG_ORDER_URL = BASE_URL + "Fangdong/fd_order_ytf";
    //(房东)确认入住
    public static final String LANDLORD_SURE_RUZHU_URL = BASE_URL + "Fangdong/sub_ruzhu";
    //(房东)确认退房
    public static final String LANDLORD_SURE_TUIFANG_URL = BASE_URL + "Fangdong/sub_tuifang";
    //(房东)退款申请
    public static final String LANDLORD_TUIKUAN_APPLY_URL = BASE_URL + "Fangdong/fd_order_tksq";
    //(房东)确认退款
    public static final String LANDLORD_SURE_TUIKUAN_URL = BASE_URL + "Fangdong/sub_tuikuan";
    //(房东)我的订单-提前退房
    public static final String LANDLORD_TIQIAN_TUIFANG_URL = BASE_URL + "Fangdong/fd_order_tqtf";
    //(房东)确认提前退房
    public static final String LANDLORD_SURE_TIQIAN_TUIFANG_URL = BASE_URL + "Fangdong/sub_tqtf";
    //我的银行卡列表
    public static final String MY_BANKCARD_LIST_URL = BASE_URL + "fangdong/my_bank";
    //添加银行卡
    public static final String ADD_BANKCARD_LIST_URL = BASE_URL + "fangdong/add_bank";
    //我的余额页面
    public static final String MY_YUE_URL = BASE_URL + "fangdong/my_yue";
    //提现申请确认
    public static final String TIXIAN_APPLY_URL = BASE_URL + "fangdong/tx_sub";
    //提现页面
    public static final String TIXIAN_PAGE_URL = BASE_URL + "fangdong/tx_html";
    //房东收支记录
    public static final String LANDLORD_SHOUZHI_URL = BASE_URL + "fangdong/shouzhi";

    //---------------------------前端-------------------------------------------------
    //积分
    public static final String INTEGRAL_URL = BASE_URL+"faxian/jf_list/token/";
    public static final String HELP_WEB_URL = BASE_URL+"faxian/bangzhu/token/";//帮助

}
