package com.minsu.minsu.user.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.minsu.minsu.R;
import com.minsu.minsu.user.bean.BankListBean;

import java.util.List;

/**
 * Created by hpc on 2018/1/24.
 */

public class BankListAdapter extends BaseQuickAdapter<BankListBean.Data, BaseViewHolder> {
    public BankListAdapter(int layoutResId, @Nullable List<BankListBean.Data> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BankListBean.Data item) {
        helper.setText(R.id.bank_name, item.bank_name);
        helper.setText(R.id.bank_number, "**** **** ****" + item.bank_code);
    }
}
