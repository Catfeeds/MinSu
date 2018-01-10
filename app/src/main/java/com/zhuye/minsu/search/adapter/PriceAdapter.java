package com.zhuye.minsu.search.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhuye.minsu.R;

import java.util.List;

/**
 * Created by hpc on 2018/1/9.
 */

public class PriceAdapter extends BaseQuickAdapter<String,BaseViewHolder> {
    public PriceAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.pop_type, item);
    }
}
