package com.minsu.minsu.user.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.minsu.minsu.user.bean.ShouZhiBean;

import java.util.List;

/**
 * Created by hpc on 2018/1/25.
 */

public class ShouZhiAdapter extends BaseQuickAdapter<ShouZhiBean.Data, BaseViewHolder> {
    public ShouZhiAdapter(int layoutResId, @Nullable List<ShouZhiBean.Data> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ShouZhiBean.Data item) {

    }
}
