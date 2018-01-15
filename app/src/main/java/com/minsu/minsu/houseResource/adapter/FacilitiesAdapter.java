package com.minsu.minsu.houseResource.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.minsu.minsu.R;

import java.util.List;

/**
 * Created by hpc on 2018/1/3.
 */

public class FacilitiesAdapter extends BaseQuickAdapter<FacilitiesBean, BaseViewHolder> {
    public FacilitiesAdapter(int layoutResId, @Nullable List<FacilitiesBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FacilitiesBean item) {
        Glide.with(mContext)
                .load(item.img)
//                .placeholder(R.mipmap.ic_launcher)
                .into((ImageView) helper.getView(R.id.peitao_img));
        helper.setText(R.id.peitao_name, item.name);
    }
}
