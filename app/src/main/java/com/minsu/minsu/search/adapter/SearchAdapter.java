package com.minsu.minsu.search.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.minsu.minsu.R;
import com.minsu.minsu.api.Constant;
import com.minsu.minsu.search.SearchBean;

import java.util.List;

/**
 * Created by hpc on 2018/1/9.
 */

public class SearchAdapter extends BaseQuickAdapter<SearchBean.Data1,BaseViewHolder> {
    public SearchAdapter(int layoutResId, @Nullable List<SearchBean.Data1> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SearchBean.Data1 item) {
        helper.setText(R.id.room_name, item.title);
        helper.setText(R.id.room_description, item.house_info);
        helper.setText(R.id.room_price, "￥" + item.house_price + "/天");
        helper.setText(R.id.room_address, item.city + " " + item.district + " " + item.town);
        Glide.with(mContext)
                .load(Constant.BASE2_URL + item.house_img)
                .into((ImageView) helper.getView(R.id.room_img));
        //focus_room
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
