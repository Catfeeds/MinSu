package com.minsu.minsu.user.bean;

import java.util.List;

/**
 * Created by hpc on 2018/1/8.
 */

public class CouponListBean {
    public String msg;
    public int code;
    public List<Data> data;
    public class Data{
        public int quan_id;
        public String info;
        public int price;
        public int sum;
        public int get_num;
        public String start_time;
        public String end_time;
        public int is_type;
    }
}
