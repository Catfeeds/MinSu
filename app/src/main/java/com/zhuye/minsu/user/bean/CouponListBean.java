package com.zhuye.minsu.user.bean;

import java.util.List;

/**
 * Created by hpc on 2018/1/8.
 */

public class CouponListBean {
    public String msg;
    public int code;
    public List<Data> data;
    public class Data{
        public String info;
        public String price;
        public String start_time;
        public int end_time;
        public int is_type;
    }
}
