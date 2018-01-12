package com.zhuye.minsu.search.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhuye.minsu.R;
import com.zhuye.minsu.search.SearchBean;

import java.util.List;

/**
 * Created by hpc on 2018/1/12.
 */

public class RoomTypeAdapter extends BaseQuickAdapter<SearchBean.Data3, BaseViewHolder> {
    public RoomTypeAdapter(int layoutResId, @Nullable List<SearchBean.Data3> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SearchBean.Data3 item) {
        helper.setText(R.id.pop_type, item.name);
    }
}
