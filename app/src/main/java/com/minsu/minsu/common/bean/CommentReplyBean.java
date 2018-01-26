package com.minsu.minsu.common.bean;

import java.util.List;

/**
 * Created by hpc on 2018/1/25.
 */

public class CommentReplyBean {
    public String msg;
    public int code;
    public Data1 data1;
    public List<Data2> data2;

    public class Data1 {
        public int comment_id;
        public int u_coll;
        public String content;
        public String add_time;
        public String nickname;
        public String head_pic;
    }
    public class Data2 {
        public int comment_id;
        public String content;
        public String add_time;
        public String nickname;
        public String head_pic;
    }
}
