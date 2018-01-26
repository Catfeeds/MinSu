package com.minsu.minsu.user.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.minsu.minsu.R;
import com.minsu.minsu.user.bean.CouponListBean;
import com.minsu.minsu.user.bean.PassengerListBean;

import java.util.List;

/**
 * Created by hpc on 2018/1/8.
 */

public class PassengerAdapter extends BaseQuickAdapter<PassengerListBean.Data, BaseViewHolder> {
    public PassengerAdapter(int layoutResId, @Nullable List<PassengerListBean.Data> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PassengerListBean.Data item) {
        helper.setText(R.id.passenger_name, item.name);
        helper.setText(R.id.passenger_idCard, item.zj_code);
        helper.setText(R.id.passenger_type, item.lk_type);
        helper.addOnClickListener(R.id.passenger_edit);
        helper.addOnClickListener(R.id.passenger_delete);
    }
}
