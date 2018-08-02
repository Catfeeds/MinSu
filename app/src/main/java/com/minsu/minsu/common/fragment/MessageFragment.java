package com.minsu.minsu.common.fragment;



import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.minsu.minsu.App;
import com.minsu.minsu.common.FragmentBackHandler;
import com.minsu.minsu.common.adapter.SimpleFragmentPagerAdapter;
import com.minsu.minsu.common.bean.EventBusBean;
import com.minsu.minsu.common.bean.MessageEvent;
import com.minsu.minsu.rongyun.ConversationListActivity;
import com.minsu.minsu.utils.StorageUtil;
import com.minsu.minsu.utils.ToastManager;
import com.minsu.minsu.utils.UIThread;
import com.minsu.minsu.widget.BadgeView;
import com.minsu.minsu.R;
import com.minsu.minsu.base.BaseFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

import static com.minsu.minsu.utils.SizeUtils.dip2px;


/**
 * Created by hpc on 2017/12/1.
 */

public class MessageFragment extends BaseFragment implements FragmentBackHandler,ChatRecordFragment.CharMessage
{


   // @BindView(R.id.viewpager)
    ViewPager viewpager;
  //  Unbinder unbinder;
  //  @BindView(R.id.tab)
    FrameLayout tab;
    private View view;
    private List<String> mPageTitleList = new ArrayList<String>();
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private List<Integer> mBadgeCountList = new ArrayList<Integer>();

    private SimpleFragmentPagerAdapter mPagerAdapter;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private List<BadgeView> mBadgeViews;
    private int charnumber=0,sysnumber=0,ordermessage=0,allnumber=0;
    private int count = 0;
    private OrderPromptFragment orderPromptFragment;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container)
    {
        view = inflater.inflate(R.layout.fragment_message, container, false);
        return view;
    }

    @Override
    protected void initListener()
    {
        EventBus.getDefault().register(this);
        initFragments();
        initViews();

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventBusBean messageEvent) {
     if (messageEvent.getType().equals("order"))
     {
         if (messageEvent.getNumber()<=0)
         {
             ordermessage=0;
         }else {
             ordermessage=messageEvent.getNumber();
         }
         mBadgeCountList.set(0, ordermessage);
         setUpTabBadge();
         allnumber=sysnumber+ordermessage+charnumber;
         EventBus.getDefault().post(new MessageEvent(allnumber));
     }else if (messageEvent.getType().equals("system"))
     {
         if (messageEvent.getNumber()<=0)
         {
             sysnumber=0;
         }else {
             sysnumber=messageEvent.getNumber();
         }
         mBadgeCountList.set(2, sysnumber);
         setUpTabBadge();
         allnumber=sysnumber+ordermessage+charnumber;
         EventBus.getDefault().post(new MessageEvent(allnumber));
     }
    }
public void is()
{
   if (orderPromptFragment!=null)
   {
       orderPromptFragment.shuxin();
   }
}
    private void initFragments() {
        mPageTitleList.add("订单提示");
        mPageTitleList.add("聊天记录");
        mPageTitleList.add("系统消息");
        String sys=StorageUtil.getValue(getActivity(),"sysm");
        if (sys==null||sys.equals("0")||sys.equals(""))
        {
            sysnumber=0;
        }else {
            sysnumber=Integer.parseInt(sys);
        }
        String orderm=StorageUtil.getValue(getActivity(),"orderm");
        if (orderm==null||orderm.equals("0")||orderm.equals(""))
        {
            ordermessage=0;
        }else {
            ordermessage=Integer.parseInt(orderm);
        }
        String mn=StorageUtil.getValue(getActivity(),"charm");
        if (mn==null||mn.equals("0")||mn.equals(""))
        {
            charnumber=0;
        }else {
            charnumber=Integer.parseInt(mn);
        }
        mBadgeCountList.add(ordermessage);
        mBadgeCountList.add(charnumber);
        mBadgeCountList.add(sysnumber);
        orderPromptFragment = new OrderPromptFragment();
       mFragmentList.add(orderPromptFragment);
        ConversationListActivity chatRecordFragment=new ConversationListActivity();
      // chatRecordFragment.setViewPage(mViewPager);
        mFragmentList.add(chatRecordFragment);
        SystemMessageFragment systemMessageFragment=new SystemMessageFragment();
        mFragmentList.add(systemMessageFragment);
//        chatRecordFragment.setOnCharMessageLinster(new ChatRecordFragment.CharMessage()
//        {
//            @Override
//            public void charmessage(int number)
//            {
//                if (number<=0)
//                {
//                    charnumber=0;
//                }else {
//                    charnumber=number;
//                }
//                mBadgeCountList.set(1, charnumber);
//                setUpTabBadge();
//                allnumber=sysnumber+ordermessage+charnumber;
//                EventBus.getDefault().post(new MessageEvent(allnumber));
//            }
//        });
    }


    @Override
    public void onDestroy()
    {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void initViews() {

        mTabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        mViewPager =  view.findViewById(R.id.view_pager);
        mPagerAdapter = new SimpleFragmentPagerAdapter(getActivity(), getChildFragmentManager(),
                mFragmentList, mPageTitleList, mBadgeCountList);
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        initBadgeViews();
        setUpTabBadge();
        mTabLayout.setSelected(false);
        String select=StorageUtil.getValue(getActivity(),"selectview");
        if (select!=null&&select.equals("2"))
        {
            mViewPager.setCurrentItem(1,true);
        }else if (select!=null&&select.equals("3"))
        {
            mViewPager.setCurrentItem(0,true);
        }
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //tab被选的时候回调
                mBadgeCountList.set(tab.getPosition(), 0);
                if (tab.getPosition()==0)
                {

                    StorageUtil.setKeyValue(getActivity(),"orderm","0");
                    ordermessage=0;
                    allnumber=sysnumber+ordermessage+charnumber;
                    mBadgeCountList.set(1,charnumber);
                    mBadgeCountList.set(2,sysnumber);
                    EventBus.getDefault().post(new MessageEvent(allnumber));
                    setUpTabBadge();
                }else if (tab.getPosition()==1)
                {
//                    String mn=StorageUtil.getValue(getActivity(),"charm");
//                    if (mn==null||mn.equals("-1")||mn.equals(""))
//                    {
//                        mn="0";
//                    }
//                    charnumber=Integer.parseInt(mn);
//                    allnumber=sysnumber+ordermessage+charnumber;
//                    EventBus.getDefault().post(new MessageEvent(allnumber));
                    mBadgeCountList.set(0,ordermessage);
                    mBadgeCountList.set(2,sysnumber);

                }else if (tab.getPosition()==2)
                {

                    sysnumber=0;
                    StorageUtil.setKeyValue(getActivity(),"sysm","0");
                    allnumber=sysnumber+ordermessage+charnumber;
                    EventBus.getDefault().post(new MessageEvent(allnumber));
                    mBadgeCountList.set(0,ordermessage);
                    mBadgeCountList.set(1,charnumber);
                    setUpTabBadge();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //tab未被选择的时候回调
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //tab重新选择的时候回调
            }
        });
    }
    private void initBadgeViews() {
        if (mBadgeViews == null) {
            mBadgeViews = new ArrayList<BadgeView>();
            for (int i = 0; i < mFragmentList.size(); i++) {
                BadgeView tmp = new BadgeView(getActivity());
                tmp.setBadgeMargin(0, 16, 10, 0);
                tmp.setBadgeGravity(Gravity.RIGHT);
                //tmp.setTextSize(10);
                switch (i)
                {
                    case 0:
                        if (ordermessage>=10)
                        {
                            tmp.setTextSize(8);
                        }else {
                            tmp.setTextSize(10);
                        }
                            break;
                    case 1:
                        if (charnumber>=0)
                        {
                            tmp.setTextSize(8);
                        }else {
                            tmp.setTextSize(10);
                        }
                        break;
                    case 2:
                        if (sysnumber>=10)
                        {
                            tmp.setTextSize(8);
                        }else {
                            tmp.setTextSize(10);
                        }
                        break;
                }
                mBadgeViews.add(tmp);
            }
        }
    }

    private void setUpTabBadge() {
        // 1. 最简单
//        for (int i = 0; i < mFragmentList.size(); i++) {
//            mBadgeViews.get(i).setTargetView(((ViewGroup) mTabLayout.getChildAt(0)).getChildAt(i));
//            mBadgeViews.get(i).setText(formatBadgeNumber(mBadgeCountList.get(i)));
//        }

        // 2. 最实用
        for (int i = 0; i < mFragmentList.size(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);

            // 更新Badge前,先remove原来的customView,否则Badge无法更新
            View customView = tab.getCustomView();
            if (customView != null) {
                ViewParent parent = customView.getParent();
                if (parent != null) {
                    ((ViewGroup) parent).removeView(customView);
                }
            }

            // 更新CustomView
            tab.setCustomView(mPagerAdapter.getTabItemView(i));
        }


        // 需加上以下代码,不然会出现更新Tab角标后,选中的Tab字体颜色不是选中状态的颜色
        mTabLayout.getTabAt(mTabLayout.getSelectedTabPosition()).getCustomView().setSelected(true);
       reflex(mTabLayout);
    }
    @Override
    public void onHiddenChanged(boolean hidden)
    {
        super.onHiddenChanged(hidden);
    }

    @Override
    protected void initData()
    {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
       // unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
      //  unbinder.unbind();
    }

    private boolean isDoubleClick = false;

    @Override
    public boolean onBackPressed()
    {
        if (isDoubleClick)
        {
           //RongIM.getInstance().disconnect();
            App.getInstance().exit(2);
        } else
        {
            ToastManager.show("再次点击一次退出程序");
            isDoubleClick = true;
            UIThread.getInstance().postDelay(new Runnable()
            {
                @Override
                public void run()
                {
                    isDoubleClick = false;
                }
            }, 1000);
        }
        return true;

    }



    public void reflex(final TabLayout tabLayout){
        //了解源码得知 线的宽度是根据 tabView的宽度来设置的
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                try {
                    //拿到tabLayout的mTabStrip属性
                    LinearLayout mTabStrip = (LinearLayout) tabLayout.getChildAt(0);

                    int dp30 = dip2px(tabLayout.getContext(), 10);
                    int dp20=dip2px(tabLayout.getContext(),20);
                    for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                        View tabView = mTabStrip.getChildAt(i);

                        //拿到tabView的mTextView属性  tab的字数不固定一定用反射取mTextView
                        Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                        mTextViewField.setAccessible(true);

                        TextView mTextView = (TextView) mTextViewField.get(tabView);

                        tabView.setPadding(0, 0, 0, 0);

                        //因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度
                        int width = 50;
                       // width = mTextView.getWidth();
                        if (width == 0) {
                            mTextView.measure(0, 0);
                            width = mTextView.getMeasuredWidth();
                        }

                        //设置tab左右间距为10dp  注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                        params.width = width ;
                        params.leftMargin = dp30;
                        params.rightMargin = dp30;
                        tabView.setLayoutParams(params);

                        tabView.invalidate();
                    }

                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void charmessage(int number)
    {
        if (number>0)
        {
            charnumber=number;
        }
    }
}
