package com.minsu.minsu.user.bean;

import java.util.List;

/**
 * Created by hpc on 2018/1/8.
 */

public class BankListBean {
    public String msg;
    public int code;
    public List<Data> data;

    public class Data {
        public String bank_name;
        public String bank_code;
    }
}
