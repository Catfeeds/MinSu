package com.minsu.minsu.user.bean;

import java.util.List;

/**
 * Created by hpc on 2018/1/25.
 */

public class ShouZhiBean {
    public String msg;
    public int code;
    public List<Data> data;

    public class Data {
        public String price;
        public String add_time;
    }
}
