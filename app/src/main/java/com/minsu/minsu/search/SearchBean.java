package com.minsu.minsu.search;

import java.util.List;

/**
 * Created by hpc on 2018/1/9.
 */

public class SearchBean {
    public String msg;
    public int code;
    public List<Data1> data1;
    public List<Data2> data2;
    public List<Data3> data3;

    public class Data1 {
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

    public class Data2 {
        public int id;
        public String name;
        public int parent_id;

    }
    public class Data3 {
        public int id;
        public String name;

    }
}
