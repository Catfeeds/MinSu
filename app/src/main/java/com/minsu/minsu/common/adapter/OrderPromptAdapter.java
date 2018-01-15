package com.minsu.minsu.common.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.minsu.minsu.common.bean.OrderBean;

import java.util.List;

/**
 * Created by hpc on 2018/1/8.
 */

public class OrderPromptAdapter extends BaseQuickAdapter<OrderBean.Data, BaseViewHolder> {
    public OrderPromptAdapter(int layoutResId, @Nullable List<OrderBean.Data> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderBean.Data item) {

    }
}
