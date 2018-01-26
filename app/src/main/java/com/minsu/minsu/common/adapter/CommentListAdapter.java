package com.minsu.minsu.common.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.minsu.minsu.R;
import com.minsu.minsu.api.Constant;
import com.minsu.minsu.common.bean.CommentBean;

import java.util.List;

/**
 * Created by hpc on 2018/1/25.
 */

public class CommentListAdapter extends BaseQuickAdapter<CommentBean.Data, BaseViewHolder> {
    public CommentListAdapter(int layoutResId, @Nullable List<CommentBean.Data> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CommentBean.Data item) {

        helper.setText(R.id.comment_content, item.content);
        helper.setText(R.id.comment_name, item.nickname);
        helper.setText(R.id.comment_time, item.add_time);
        helper.setText(R.id.comment_zan, item.c_count + "赞");
        helper.setText(R.id.comment_reply_count, item.hf_count + "回复");
        helper.addOnClickListener(R.id.comment_zan);
        helper.addOnClickListener(R.id.comment_reply_count);


        if (item.head_pic.contains("http")) {
            Glide.with(mContext).load(item.head_pic).into((ImageView) helper.getView(R.id.comment_user_img));
        } else {
            Glide.with(mContext).load(Constant.BASE2_URL + item.head_pic).into((ImageView) helper.getView(R.id.comment_user_img));
        }
    }
}
