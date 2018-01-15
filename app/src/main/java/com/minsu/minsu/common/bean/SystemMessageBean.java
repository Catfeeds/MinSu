package com.minsu.minsu.common.bean;

import java.util.List;

/**
 * Created by hpc on 2017/12/26.
 */

public class SystemMessageBean {
    public String msg;
    public int code;
    public List<Data> data;
    public class Data{
        public String title;
        public String content;
        public String link_url;
        public String xx_img;
        public String add_time;
    }
}
