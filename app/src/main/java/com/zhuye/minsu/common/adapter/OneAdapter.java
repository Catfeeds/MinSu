package com.zhuye.minsu.common.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhuye.minsu.R;

/**
 * Created by geyifeng on 2017/6/3.
 */

public class OneAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public OneAdapter() {
        super(R.layout.item_one);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.text, item);
    }
}
