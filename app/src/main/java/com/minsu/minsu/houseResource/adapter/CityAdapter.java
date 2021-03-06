package com.minsu.minsu.houseResource.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.minsu.minsu.R;
import com.minsu.minsu.houseResource.bean.CityBean;

import java.util.List;

/**
 * Created by hpc on 2018/1/3.
 */

public class CityAdapter extends BaseQuickAdapter<CityBean.Data, BaseViewHolder> {
    public CityAdapter(int layoutResId, @Nullable List<CityBean.Data> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CityBean.Data item) {
        helper.setText(R.id.pop_type, item.name);
    }
}
