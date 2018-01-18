package com.minsu.minsu.common.bean;

import java.util.List;

/**
 * Created by hpc on 2018/1/16.
 */

public class CityBean {
    public String msg;
    public int code;
    public List<Data1> data1;
    public List<Data2> data2;
    public List<Data3> data3;

    public class Data1 {
        public int id;
        public String name;
        public int parent_id;//省份id
        public String zimu;//城市首字母
    }

    public class Data2 {
        public int c_num;
        public String name;
        public int city_id;//城市id
    }

    public class Data3 {
        public int c_num;
        public String name;
        public int city_id;//城市id
    }
}
