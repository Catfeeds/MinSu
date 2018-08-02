package com.minsu.minsu.common.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hpc on 2018/1/8.
 */

public class OrderBean {
    public String msg;
    public int code;
    public List<Data> data;

    public class Data implements Serializable {
        public int type;
        public String user_name;
        public String h_mobile;
        public String is_house;
        public String user_mobile;
        public String order_sn;
        public String add_time;
        public String pay_time;
        public String user_is;
        public String name;
        public String time;
        public String create_time;
        public String title;
        public String check_time;
        public String leave_time ;
        public String days;
        public String total_price;
        public String province;
        public String city;
        public String district;
        public String town;
        public String head_n;
        public String h_name;
        public String head_pic;
        public String fd_mobile;
        public String house_info;
        public String house_img;
        public int order_status;
        public int pay_status;
        public int is_tuikuan;
        public int order_id;
        public int house_id;
        public int is_tuifang;
        public int comment;
        public String is_pj;
        public String tuikuan_type;
        public String tuikuan_txt;

    }
}
