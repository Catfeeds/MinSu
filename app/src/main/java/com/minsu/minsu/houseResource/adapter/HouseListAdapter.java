package com.minsu.minsu.houseResource.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.minsu.minsu.R;
import com.minsu.minsu.api.Constant;
import com.minsu.minsu.houseResource.bean.HouseListBean;

import java.util.List;

/**
 * Created by hpc on 2018/1/5.
 */

public class HouseListAdapter extends BaseQuickAdapter<HouseListBean.Data, BaseViewHolder> {
    private String type;

    public HouseListAdapter(int layoutResId, @Nullable List<HouseListBean.Data> data, String type) {
        super(layoutResId, data);
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, HouseListBean.Data item) {
        helper.setText(R.id.room_name, item.title);
        helper.setText(R.id.room_description, item.house_info);
        helper.setText(R.id.room_price, "￥" + item.house_price + "元/天");
        helper.setText(R.id.room_address, item.city + " " + item.district + " " + item.town);
        Glide.with(mContext)
                .load(Constant.BASE2_URL + item.house_img)
                .into((ImageView) helper.getView(R.id.room_img));

        helper.addOnClickListener(R.id.focus_room);
        helper.getView(R.id.focus_room).setVisibility(View.GONE);
//        if (type.equals("home")) {
//
//        }
        int collect = item.collect;
        if (collect == 1) {
            Glide.with(mContext)
                    .load(R.mipmap.collected)
                    .into((ImageView) helper.getView(R.id.focus_room));
            helper.setBackgroundRes(R.id.focus_room,R.mipmap.collected);
            helper.getView(R.id.focus_room).setVisibility(View.VISIBLE);
        } else if (collect == 0) {
            Glide.with(mContext)
                    .load(R.mipmap.collect)
                    .into((ImageView) helper.getView(R.id.focus_room));
        }

    }

    @Override
    public int getItemViewType(int position)
    {
        return super.getItemViewType(position);
    }
}
