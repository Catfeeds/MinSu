package com.minsu.minsu.common.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.minsu.minsu.R;
import com.minsu.minsu.api.Constant;
import com.minsu.minsu.common.bean.ChatBean;

import java.util.List;

/**
 * Created by hpc on 2018/1/16.
 */

public class ChatListAdapter extends BaseQuickAdapter<ChatBean.Data, BaseViewHolder> {
    public ChatListAdapter(int layoutResId, @Nullable List<ChatBean.Data> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ChatBean.Data item) {
        helper.setText(R.id.chat_name, item.nickname);
        helper.setText(R.id.chat_content, item.nickname);
        helper.setText(R.id.chat_time, item.add_time);
        if (item.head_pic.contains("http")) {
            Glide.with(mContext)
                    .load(item.head_pic)
                    .into((ImageView) helper.getView(R.id.chat_img));
        } else {
            Glide.with(mContext)
                    .load(Constant.BASE2_URL + item.head_pic)
                    .into((ImageView) helper.getView(R.id.chat_img));
        }
    }
}
