package com.minsu.minsu.houseResource.adapter;

import java.io.Serializable;

/**
 * Created by hpc on 2018/1/4.
 */

public class FacilitiesBean implements Serializable{
    public int status;
    public String name;
    public Integer img;
    public int tcolor;

    public FacilitiesBean(int status, String name, Integer img,int tcolor) {
        this.status = status;
        this.name = name;
        this.img = img;
        this.tcolor=tcolor;
    }

}
