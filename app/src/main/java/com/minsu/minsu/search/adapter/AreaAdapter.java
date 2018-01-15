package com.minsu.minsu.search.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.minsu.minsu.R;
import com.minsu.minsu.search.SearchBean;

import java.util.List;

/**
 * Created by hpc on 2018/1/9.
 */

public class AreaAdapter extends BaseQuickAdapter<SearchBean.Data2, BaseViewHolder> {
    public AreaAdapter(int layoutResId, @Nullable List<SearchBean.Data2> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SearchBean.Data2 item) {
        helper.setText(R.id.pop_type, item.name);
    }
}
