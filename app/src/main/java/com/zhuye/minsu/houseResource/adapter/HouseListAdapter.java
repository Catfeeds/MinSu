package com.zhuye.minsu.houseResource.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhuye.minsu.R;
import com.zhuye.minsu.api.Constant;
import com.zhuye.minsu.houseResource.bean.HouseListBean;

import java.util.List;

/**
 * Created by hpc on 2018/1/5.
 */

public class HouseListAdapter extends BaseQuickAdapter<HouseListBean.Data, BaseViewHolder> {
    public HouseListAdapter(int layoutResId, @Nullable List<HouseListBean.Data> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HouseListBean.Data item) {
        helper.setText(R.id.room_name, item.title);
        helper.setText(R.id.room_description, item.house_info);
        helper.setText(R.id.room_price, "￥" + item.house_price + "/天");
        helper.setText(R.id.room_address, item.city + " "+item.district + " "+item.town);
        Glide.with(mContext)
                .load(Constant.BASE2_URL + item.house_img)
                .into((ImageView) helper.getView(R.id.room_img));
        int collect = item.collect;
        helper.addOnClickListener(R.id.focus_room);
        if (collect == 1) {
            Glide.with(mContext)
                    .load(R.mipmap.collected)
                    .into((ImageView) helper.getView(R.id.focus_room));
        } else if (collect == 0) {
            Glide.with(mContext)
                    .load(R.mipmap.collect)
                    .into((ImageView) helper.getView(R.id.focus_room));
        }
    }
}
