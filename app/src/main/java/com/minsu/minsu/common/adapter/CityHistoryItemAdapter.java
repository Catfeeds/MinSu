package com.minsu.minsu.common.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.minsu.minsu.R;
import com.minsu.minsu.common.bean.CityBean;

import java.util.List;

/**
 * Created by hpc on 2018/1/16.
 */

public class CityHistoryItemAdapter extends BaseQuickAdapter<CityBean.Data3, BaseViewHolder> {
    public CityHistoryItemAdapter(int layoutResId, @Nullable List<CityBean.Data3> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CityBean.Data3 item) {
        helper.setText(R.id.item_city, item.name);
    }
}
