package com.minsu.minsu.user.bean;

import java.util.List;

/**
 * Created by hpc on 2018/1/8.
 */

public class PassengerListBean {
    public String msg;
    public int code;
    public List<Data> data;

    public class Data {
        public String name;
        public String zj_type;
        public String zj_code;
        public String lk_type;
        public int id;
    }
}
