package com.minsu.minsu.common.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.minsu.minsu.App;
import com.minsu.minsu.R;
import com.minsu.minsu.user.bean.AddressListBean;

import java.util.List;

/**
 * Created by hpc on 2017/12/26.
 */

public class AddressListAdapter extends BaseQuickAdapter<AddressListBean.Data,BaseViewHolder>{
    public AddressListAdapter(int layoutResId, @Nullable List<AddressListBean.Data> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AddressListBean.Data item) {
            helper.setText(R.id.address_name,item.name);
            helper.setText(R.id.address_location,item.address);
            helper.setText(R.id.address_phone,item.mobile);
        int is_default = item.is_default;
        if (is_default==1){
            helper.setText(R.id.default_address,"默认地址");
            helper.setTextColor(R.id.default_address, App.getInstance().getResources().getColor(R.color.red));
        }else if (is_default==0){
            helper.setText(R.id.default_address,"设为默认地址");
            helper.setTextColor(R.id.default_address,App.getInstance().getResources().getColor(R.color.grey));
        }
        helper.addOnClickListener(R.id.address_delete);
        helper.addOnClickListener(R.id.address_edit);
        helper.addOnClickListener(R.id.default_address);
    }
}
