package com.minsu.minsu.common.bean;

import java.util.List;

/**
 * Created by hpc on 2017/12/26.
 */

public class HomeBean {
    public String msg;
    public int code;
    public List<Data1> data1;
    public List<Data2> data2;
    public class Data1{
        public String title;
        public String house_id;
        public String house_info;
        public String house_price;
        public String province;
        public String city;
        public String district;
        public String town;
        public String house_img;
        public int collect;
    }
    public class Data2{
        public String ad_code;
        public String ad_link;
    }
}
