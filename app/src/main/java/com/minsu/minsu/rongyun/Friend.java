package com.minsu.minsu.rongyun;

/**
 * Created by hpc on 2018/1/24.
 */

public class Friend {
    private String id;
    private String name;
    private String head_img;

    public Friend(String id, String name, String head_img) {
        this.id = id;
        this.name = name;
        this.head_img = head_img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHead_img() {
        return head_img;
    }

    public void setHead_img(String head_img) {
        this.head_img = head_img;
    }
}
