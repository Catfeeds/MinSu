package com.minsu.minsu.common.bean;

import java.util.List;

/**
 * Created by hpc on 2018/1/16.
 */

public class ChatBean {
    public String msg;
    public int code;
    public List<Data> data;
    public class Data{
        public int id;
        public String targetid;
        public String nickname;
        public String head_pic;
        public String add_time;
        public String user_id;
    }
}
