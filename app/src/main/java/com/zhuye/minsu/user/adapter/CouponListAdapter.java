package com.zhuye.minsu.user.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhuye.minsu.user.bean.CouponListBean;

import java.util.List;

/**
 * Created by hpc on 2018/1/8.
 */

public class CouponListAdapter extends BaseQuickAdapter<CouponListBean.Data,BaseViewHolder> {
    public CouponListAdapter(int layoutResId, @Nullable List<CouponListBean.Data> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CouponListBean.Data item) {

    }
}
