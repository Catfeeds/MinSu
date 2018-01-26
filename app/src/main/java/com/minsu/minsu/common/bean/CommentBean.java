package com.minsu.minsu.common.bean;

import java.util.List;

/**
 * Created by hpc on 2018/1/25.
 */

public class CommentBean {
    public String msg;
    public int code;
    public List<Data> data;

    public class Data {
        public int comment_id;
        public int user_id;
        public int c_count;
        public int hf_count;
        public int u_coll;
        public String content;
        public String add_time;
        public String nickname;
        public String head_pic;
    }
}
