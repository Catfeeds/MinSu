package com.zhuye.minsu.houseResource.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by hpc on 2018/1/3.
 */

public class HouseBean implements Serializable{
    public String msg;
    public int code;
    public ArrayList<Data1> data1;
    public ArrayList<Data2> data2;
    public ArrayList<Data3> data3;
    public class Data1 implements Serializable{
        public int id;
        public String name;
        public int parent_id;
    }
    public class Data2{
        public int id;
        public String name;
        public String info;
    }
    public class Data3{
        public int id;
        public String name;
        public String info;
    }
}
