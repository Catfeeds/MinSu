package com.zhuye.minsu.common.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhuye.minsu.R;
import com.zhuye.minsu.api.Constant;
import com.zhuye.minsu.common.bean.SystemMessageBean;

import java.util.List;

/**
 * Created by hpc on 2017/12/26.
 */

public class SystemMessageAdapter extends BaseQuickAdapter<SystemMessageBean.Data, BaseViewHolder> {
    public SystemMessageAdapter(int layoutResId, @Nullable List<SystemMessageBean.Data> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SystemMessageBean.Data item) {
        helper.setText(R.id.message_title, item.title);
        helper.setText(R.id.message_description, item.content);
        helper.setText(R.id.add_time, item.add_time);
        Glide.with(mContext).load(Constant.BASE2_URL + item.xx_img)
                .placeholder(R.mipmap.me_background_1)
                .into((ImageView) helper.getView(R.id.message_img));
    }
}
