package com.minsu.minsu.houseResource.bean;

import java.util.ArrayList;

/**
 * Created by hpc on 2018/1/3.
 */

public class CityBean {
    public String msg;
    public int code;
    public ArrayList<Data> data;

    public class Data {
        public int id;
        public String name;
        public int parent_id;
    }
}
