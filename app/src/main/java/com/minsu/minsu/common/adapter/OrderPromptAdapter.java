package com.minsu.minsu.common.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.minsu.minsu.R;
import com.minsu.minsu.api.Constant;
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
        helper.setText(R.id.order_name, item.title);
        helper.setText(R.id.order_content, item.house_info);
        Glide.with(mContext).load(Constant.BASE2_URL + item.house_img).into((ImageView) helper.getView(R.id.order_img));
    }
}
