package com.zhuye.minsu.user.bean;

import java.util.List;

/**
 * Created by hpc on 2017/12/26.
 */

public class AddressListBean {
    public String msg;
    public int code;
    public List<Data> data;
    public class Data{
        public String name;
        public String address;
        public String mobile;
        public int is_default;
        public int address_id;
    }
}
