package com.minsu.minsu.user;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.minsu.minsu.R;
import com.minsu.minsu.base.BaseActivity;
import com.minsu.minsu.common.fragment.ChatRecordFragment;
import com.minsu.minsu.common.fragment.OrderPromptFragment;
import com.minsu.minsu.common.fragment.SystemMessageFragment;
import com.minsu.minsu.widget.SmartTab.UtilsV4.v4.FragmentPagerItem;
import com.minsu.minsu.widget.SmartTab.UtilsV4.v4.FragmentPagerItemAdapter;
import com.minsu.minsu.widget.SmartTab.UtilsV4.v4.FragmentPagerItems;

import butterknife.BindView;

public class OrderActivity extends BaseActivity {


    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.iv_left)
    ImageView ivLeft;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tab)
    FrameLayout tab;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {
        setTitle();
        tab.addView(LayoutInflater.from(this).inflate(R.layout.tab_top_layout, tab, false));
        SmartTabLayout viewPagerTab = findViewById(R.id.viewpagertab);
        FragmentPagerItems pages = new FragmentPagerItems(this);
        pages.add(FragmentPagerItem.of("全部", OrderPromptFragment.class));
        pages.add(FragmentPagerItem.of("待入住", ChatRecordFragment.class));
        pages.add(FragmentPagerItem.of("入住中", SystemMessageFragment.class));
        pages.add(FragmentPagerItem.of("已退房", SystemMessageFragment.class));
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(getSupportFragmentManager(), pages);
        viewpager.setAdapter(adapter);
        viewPagerTab.setViewPager(viewpager);
    }

    private void setTitle() {
        toolbarTitle.setText("我的订单");
        ivLeft.setVisibility(View.VISIBLE);
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_order);
    }

    @Override
    protected Context getActivityContext() {
        return this;
    }


}
