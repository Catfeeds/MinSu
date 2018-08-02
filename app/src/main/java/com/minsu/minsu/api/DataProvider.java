package com.minsu.minsu.api;


import com.minsu.minsu.R;
import com.minsu.minsu.houseResource.adapter.FacilitiesBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr.Jude on 2016/1/6.
 */
public class DataProvider {


    //
    public static List<FacilitiesBean> getFacilitiesList() {
        ArrayList<FacilitiesBean> arr = new ArrayList<>();
        arr.add(new FacilitiesBean(1, "标准间", R.mipmap.pc_01,0));
        arr.add(new FacilitiesBean(2, "大床房", R.mipmap.pc_02,0));
        arr.add(new FacilitiesBean(3, "家庭房", R.mipmap.pc_03,0));
        arr.add(new FacilitiesBean(4, "24小时热水", R.mipmap.pc_04,0));
        arr.add(new FacilitiesBean(5, "电视", R.mipmap.pc_05,0));
        arr.add(new FacilitiesBean(6, "空调", R.mipmap.pc_06,0));
        arr.add(new FacilitiesBean(7, "整租", R.mipmap.pc_07,0));
        arr.add(new FacilitiesBean(8, "单间", R.mipmap.pc_08,0));
        arr.add(new FacilitiesBean(9, "wifi", R.mipmap.pc_09,0));
        arr.add(new FacilitiesBean(10, "三人行礼包", R.mipmap.pc_10,0));
        arr.add(new FacilitiesBean(11, "停车场", R.mipmap.pc_11,0));
        arr.add(new FacilitiesBean(12, "娱乐设施", R.mipmap.pc_12,0));
        arr.add(new FacilitiesBean(13, "周边景点", R.mipmap.pc_13,0));
        return arr;
    }

    public static List<FacilitiesBean> getFacilitiesList(String[] s) {
        ArrayList<FacilitiesBean> arr = new ArrayList<>();
        arr.add(new FacilitiesBean(1, "标准间", R.mipmap.pc_01,0));
        arr.add(new FacilitiesBean(2, "大床房", R.mipmap.pc_02,0));
        arr.add(new FacilitiesBean(3, "家庭房", R.mipmap.pc_03,0));
        arr.add(new FacilitiesBean(4, "24小时热水", R.mipmap.pc_04,0));
        arr.add(new FacilitiesBean(5, "电视", R.mipmap.pc_05,0));
        arr.add(new FacilitiesBean(6, "空调", R.mipmap.pc_06,0));
        arr.add(new FacilitiesBean(7, "整租", R.mipmap.pc_07,0));
        arr.add(new FacilitiesBean(8, "单间", R.mipmap.pc_08,0));
        arr.add(new FacilitiesBean(9, "wifi", R.mipmap.pc_09,0));
        arr.add(new FacilitiesBean(10, "三人行礼包", R.mipmap.pc_10,0));
        arr.add(new FacilitiesBean(11, "停车场", R.mipmap.pc_11,0));
        arr.add(new FacilitiesBean(12, "娱乐设施", R.mipmap.pc_12,0));
        arr.add(new FacilitiesBean(13, "周边景点", R.mipmap.pc_13,0));
        for (int i=0;i<s.length;i++)
        {
            arr.set(Integer.parseInt(s[i])-1,new FacilitiesBean(i+1,getname(Integer.parseInt(s[i])),FacilitiesSecImage[Integer.parseInt(s[i])-1],1));
        }
        return arr;
    }
    public static String getname(int i)
    {
        if (i==1)
        {
            return "标准件";
        }else if (i==2)
        {
            return "大床房";
        }else if (i==3)
        {
            return "家庭房";
        }else if (i==4)
        {
            return "24小时热水";
        }else if (i==5)
        {
            return "电视";
        }else if (i==6)
        {
            return "空调";
        }else if (i==7)
        {
            return "整租";
        }else if (i==8)
        {
            return "单间";
        }else if (i==9)
        {
            return "wifi";
        }else if (i==10)
        {
            return "三人行礼包";

        }else if (i==11)
        {
            return "停车场";
        }else if (i==12)
        {
            return "娱乐设施";
        }else if (i==13)
        {
            return "周边景点";
        }else {
            return null;
        }
    }
//
//    public static List<Numbers> getNumberList(int page) {
//        ArrayList<Numbers> arr = new ArrayList<>();
//        if (page == 12) return arr;
//
//        arr.add(new Numbers("29经验值", "http://i2.hdslb.com/52_52/user/61175/6117592/myface.jpg", "123444", "叫我华哥", "10%", "1", "中国教育出版社"));
//        arr.add(new Numbers("29经验值", "http://i1.hdslb.com/52_52/user/6738/673856/myface.jpg", "123444", "侯鹏成", "10%", "2", "北京大学出版社"));
//        arr.add(new Numbers("29经验值", "http://i1.hdslb.com/account/face/1467772/e1afaf4a/myface.png", "123444", "可乐成", "10%", "3", "中国教育出版社"));
//        arr.add(new Numbers("29经验值", "http://i0.hdslb.com/52_52/user/18494/1849483/myface.jpg", "123444", "天之骄子", "10%", "4", "中国教育出版社"));
//        arr.add(new Numbers("29经验值", "http://i0.hdslb.com/52_52/account/face/4613528/303f4f5a/myface.png", "123444", "放羊娃", "10%", "5", "中国教育出版社"));
//        arr.add(new Numbers("29经验值", "http://i0.hdslb.com/52_52/account/face/611203/76c02248/myface.png", "123444", "张三丰", "10%", "6", "中国教育出版社"));
//        arr.add(new Numbers("29经验值", "http://i2.hdslb.com/52_52/user/46230/4623018/myface.jpg", "123444", "张无忌", "10%", "7", "北京大学出版社"));
//        arr.add(new Numbers("29经验值", "http://i2.hdslb.com/52_52/user/66723/6672394/myface.jpg", "123444", "张翻过", "10%", "8", "中国教育出版社"));
//        arr.add(new Numbers("29经验值", "http://i1.hdslb.com/user/3039/303946/myface.jpg", "123444", "袁崇焕", "10%", "1", "北京大学出版社"));
//        arr.add(new Numbers("29经验值", "http://i2.hdslb.com/account/face/9034989/aabbc52a/myface.png", "123444", "毛文龙", "10%", "9", "中国教育出版社"));
//        arr.add(new Numbers("29经验值", "http://i0.hdslb.com/account/face/1557783/8733bd7b/myface.png", "123444", "张承宪", "10%", "10", "北京大学出版社"));
//        arr.add(new Numbers("29经验值", "http://i2.hdslb.com/user/3716/371679/myface.jpg", "123444", "司马坑", "10%", "11", "北京大学出版社"));
//        arr.add(new Numbers("29经验值", "http://i1.hdslb.com/account/face/9045165/4b11d894/myface.png", "123444", "李世明", "10%", "12", "中国教育出版社"));
//        return arr;
//    }
//
//    public static List<Notification> getNotifiList() {
//        ArrayList<Notification> arr = new ArrayList<>();
//        arr.add(new Notification("2017.02.23 9:46", "【环球网报道 】据俄新社2月22日消息， 俄罗斯外交部发言人扎哈罗娃表示，俄外交部官网开设了一个栏目，专门用来揭露那些关于俄罗斯的不实消息。"));
//        arr.add(new Notification("2017.02.23 9:47", "【环球网报道 】据俄新社2月22日消息， 俄罗斯外交部发言人扎哈罗娃表示，俄外交部官网开设了一个栏目，专门用来揭露那些关于俄罗斯的不实消息。"));
//        arr.add(new Notification("2017.02.23 9:48", "【环球网报道 】据俄新社2月22日消息， 俄罗斯外交部发言人扎哈罗娃表示，俄外交部官网开设了一个栏目，专门用来揭露那些关于俄罗斯的不实消息。"));
//        arr.add(new Notification("2017.02.23 9:50", "【环球网报道 】据俄新社2月22日消息， 俄罗斯外交部发言人扎哈罗娃表示，俄外交部官网开设了一个栏目，专门用来揭露那些关于俄罗斯的不实消息。"));
//        arr.add(new Notification("2017.02.23 9:55", "【环球网报道 】据俄新社2月22日消息， 俄罗斯外交部发言人扎哈罗娃表示，俄外交部官网开设了一个栏目，专门用来揭露那些关于俄罗斯的不实消息。"));
//        arr.add(new Notification("2017.02.23 9:59", "【环球网报道 】据俄新社2月22日消息， 俄罗斯外交部发言人扎哈罗娃表示，俄外交部官网开设了一个栏目，专门用来揭露那些关于俄罗斯的不实消息。"));
//        return arr;
//    }
//
//
//    public static List<Object> getPersonWithAds(int page) {
//        ArrayList<Object> arrAll = new ArrayList<>();
//        List<Ad> arrAd = getAdList();
//        int index = 0;
//        for (Person person : getPersonList(page)) {
//            arrAll.add(person);
//            //按比例混合广告
//            if (Math.random() < 0.2) {
//                arrAll.add(arrAd.get(index % arrAd.size()));
//                index++;
//            }
//        }
//
//        return arrAll;
//    }


    static final int[] BannerImage = {
            R.mipmap.default_banner,
            R.mipmap.default_banner,
            R.mipmap.default_banner,
    };

    public static ArrayList<Integer> getBannerImage() {
        ArrayList<Integer> arrayList = new ArrayList<>();
//        if (page == 4) return arrayList;

        for (int i = 0; i < BannerImage.length; i++) {
            arrayList.add(BannerImage[i]);
        }
        return arrayList;
    }

    static final int[] FacilitiesImage = {
            R.mipmap.pc_01,
            R.mipmap.pc_02,
            R.mipmap.pc_03,
            R.mipmap.pc_04,
            R.mipmap.pc_05,
            R.mipmap.pc_06,
            R.mipmap.pc_07,
            R.mipmap.pc_08,
            R.mipmap.pc_09,
            R.mipmap.pc_10,
            R.mipmap.pc_11,
            R.mipmap.pc_12,
            R.mipmap.pc_13,
    };

    public static ArrayList<Integer> getFacilitiesImage() {
        ArrayList<Integer> arrayList = new ArrayList<>();
//        if (page == 4) return arrayList;

        for (int i = 0; i < FacilitiesImage.length; i++) {
            arrayList.add(FacilitiesImage[i]);
        }
        return arrayList;
    }
    static final int[] FacilitiesSecImage = {
            R.mipmap.pc_s01,
            R.mipmap.pc_s02,
            R.mipmap.pc_s03,
            R.mipmap.pc_s04,
            R.mipmap.pc_s05,
            R.mipmap.pc_s06,
            R.mipmap.pc_s07,
            R.mipmap.pc_s08,
            R.mipmap.pc_s09,
            R.mipmap.pc_s10,
            R.mipmap.pc_s11,
            R.mipmap.pc_s12,
            R.mipmap.pc_s13,
    };

    public static ArrayList<Integer> getFacilitiesSecImage() {
        ArrayList<Integer> arrayList = new ArrayList<>();
//        if (page == 4) return arrayList;

        for (int i = 0; i < FacilitiesSecImage.length; i++) {
            arrayList.add(FacilitiesSecImage[i]);
        }
        return arrayList;
    }
}
