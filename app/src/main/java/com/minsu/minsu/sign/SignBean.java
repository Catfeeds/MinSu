package com.minsu.minsu.sign;

import java.util.List;

/**
 * Created by hpc on 2018/1/8.
 */

public class SignBean {
    public String msg;
    public int code;
    public int data2;
    public List<Data1> data1;
    public class Data1{
        public String sign_time;
    }
}
