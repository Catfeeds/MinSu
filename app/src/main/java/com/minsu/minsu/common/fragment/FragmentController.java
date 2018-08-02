package com.minsu.minsu.common.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.ArrayList;

/**
 * Created by hpc on 2016/12/21.
 */

public class FragmentController {
    private int containerId;
    private FragmentManager fm;
    private ArrayList<Fragment> fragments;
    private static FragmentController controller;
    private Bundle bundle;
    private HomeFragment homeFragment;
    private MyOrderFragment myOrderFragment;
    private FindFragment findFragment;
    private MessageFragment messageFragment;
    private MeFragment meFragment;
    private FragmentTransaction ft;
    private MessageFragment messageFragment1;

    public static FragmentController getInstance(FragmentActivity activity, int containerId) {
     //   if (controller == null) {
        controller = new FragmentController(activity, containerId);
    //    }
        return controller;
    }

    public static void onDestroy() {
        controller = null;
    }

    public FragmentController(FragmentActivity activity, int containerId) {
        this.containerId = containerId;
        fm = activity.getSupportFragmentManager();
        initFragment();
    }

    public void initFragment() {
        if (fragments==null)
        {
            fragments = new ArrayList<Fragment>();
        }

        hideFragments();
//       if (homeFragment==null)
//       {
//           homeFragment = new HomeFragment();
//           fragments.add(0,homeFragment);//0
//       }
//        if (myOrderFragment==null)
//        {
//            myOrderFragment = new MyOrderFragment();
//            fragments.add(1,myOrderFragment);//0
//        }
//        if (findFragment==null)
//        {
//            findFragment = new FindFragment();
//            fragments.add(2,findFragment);//0
//        }
//        if (messageFragment==null)
//        {
//            messageFragment = new MessageFragment();
//            fragments.add(3,messageFragment);//0
//        }
//        if (meFragment==null)
//        {
//            meFragment = new MeFragment();
//            fragments.add(4,meFragment);//0
//        }
        messageFragment1 = new MessageFragment();
        fragments.add(new HomeFragment());
        fragments.add(new MyOrderFragment());//1
        fragments.add(new FindFragment());//2
        fragments.add(messageFragment1);//3
        fragments.add(new MeFragment());//4
         ft = fm.beginTransaction();
        for (Fragment fragment : fragments) {
            ft.add(containerId, fragment);
        }
        ft.commitAllowingStateLoss();
    }

    public void showFragment(int position) {
        if (position==3&&messageFragment1!=null)
        {
            messageFragment1.is();
        }
        hideFragments();
        Fragment fragment = fragments.get(position);
        FragmentTransaction ft = fm.beginTransaction();
        ft.show(fragment);
        ft.commitAllowingStateLoss();
    }

    public void hideFragments() {
        FragmentTransaction ft = fm.beginTransaction();
        for (Fragment fragment : fragments) {
            if (fragment != null) {
                ft.hide(fragment);
            }
        }
        ft.commitAllowingStateLoss();
    }

    public Fragment getFragment(int position) {

        return fragments.get(position);
    }
}
