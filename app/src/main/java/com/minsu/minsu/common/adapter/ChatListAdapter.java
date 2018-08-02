package com.minsu.minsu.common.adapter;

import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.minsu.minsu.R;
import com.minsu.minsu.api.Constant;
import com.minsu.minsu.common.bean.ChatBean;

import java.util.List;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * Created by hpc on 2018/1/16.
 */

public class ChatListAdapter extends BaseQuickAdapter<ChatBean.Data, BaseViewHolder> {
    public ChatListAdapter(int layoutResId, @Nullable List<ChatBean.Data> data) {
        super(layoutResId, data);
    }
   private int counts;
    @Override
    protected void convert(final BaseViewHolder helper, ChatBean.Data item) {
        helper.addOnClickListener(R.id.delete);
        RongIMClient.getInstance().getUnreadCount(Conversation.ConversationType.PRIVATE,item.user_id,new RongIMClient.ResultCallback<Integer>()
        {
            @Override
            public void onSuccess(Integer integer)
            {
                //targetId=String.valueOf(integer);
                // helper.setText(R.id.tv_message_counts,  unread + "");
                TextView count = helper.getView(R.id.tv_message_counts);
                if(integer ==0||integer==-1){
                    helper.getView(R.id.tv_message_counts).setVisibility(View.GONE);
                }else {
                    if (integer>=10)
                    {
                        count.setTextSize(6);
                    }else {
                        count.setTextSize(9);
                    }
                    counts = counts +integer;
                    helper.getView(R.id.tv_message_counts).setVisibility(View.VISIBLE);
                    helper.setText(R.id.tv_message_counts, String.valueOf(integer) );
                    if (integer>99)
                    {
                        helper.setText(R.id.tv_message_counts, "..." );
                    }
                }

            }
            @Override
            public void onError(RongIMClient.ErrorCode errorCode)
            {

            }
        });
        helper.setText(R.id.chat_name, item.nickname);
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
