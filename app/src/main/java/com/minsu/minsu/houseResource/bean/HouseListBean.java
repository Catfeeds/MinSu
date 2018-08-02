package com.minsu.minsu.houseResource.bean;

import java.util.ArrayList;

/**
 * Created by hpc on 2018/1/5.
 */

public class HouseListBean {
    public String msg;
    public int code;
    public ArrayList<Data> data;

    public class Data {
        public String house_id;
        public String title;
        public String house_info;
        public String house_price;
        public String province;
        public String city;
        public String district;
        public String town;
        public String house_img;
        public int collect;
    }
}
