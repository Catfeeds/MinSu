package com.minsu.minsu.user.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.minsu.minsu.R;
import com.minsu.minsu.user.bean.ShouZhiBean;

import java.util.List;

/**
 * Created by hpc on 2018/1/25.
 */

public class ShouZhiAdapter extends BaseQuickAdapter<ShouZhiBean.Data, BaseViewHolder> {
    private String type;

    public ShouZhiAdapter(int layoutResId, @Nullable List<ShouZhiBean.Data> data, String type) {
        super(layoutResId, data);
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, ShouZhiBean.Data item) {
        helper.setText(R.id.count_money, item.price);
        helper.setText(R.id.time_money, "时间:" + item.add_time);
        if (type.equals("income")) {
            helper.setText(R.id.tag, "收入");
        } else if (type.equals("expense")) {
            helper.setText(R.id.tag, "支出");
        }

    }
}
