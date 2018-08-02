package com.minsu.minsu.user.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.minsu.minsu.R;
import com.minsu.minsu.api.Constant;
import com.minsu.minsu.common.bean.OrderBean;

import java.util.List;

/**
 * Created by hpc on 2018/1/15.
 */

public class OrderListAdapter extends BaseQuickAdapter<OrderBean.Data, BaseViewHolder> {
    public OrderListAdapter(int layoutResId, @Nullable List<OrderBean.Data> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderBean.Data item) {

        helper.setText(R.id.order_userName, item.h_name);
        helper.setText(R.id.order_room_title, item.title);
        helper.setText(R.id.order_room_description, item.house_info);
        helper.setText(R.id.total_day, "共" + item.days + "晚");
        helper.setText(R.id.order_userName, item.h_name);
        helper.setText(R.id.ruzhu_time, "入住：" + item.check_time);
        helper.setText(R.id.leave_time, "离开：" + item.leave_time);
        helper.setText(R.id.order_price, "￥" + item.total_price);
        helper.setText(R.id.location_address, item.city + " " + item.district + " " + item.town);
        helper.getView(R.id.order_dells).setVisibility(View.GONE);
        if (item.pay_status == 0) {
            helper.setText(R.id.order_state, "待支付");
            //取消订单或者立即支付
            helper.addOnClickListener(R.id.order_cancel);
            helper.addOnClickListener(R.id.order_pay);
        } else if (item.pay_status == 1) {
            if (item.order_status == 0) {
                helper.setText(R.id.order_state, "待入住");
                helper.getView(R.id.order_cancel).setVisibility(View.GONE);
                helper.getView(R.id.order_pay).setVisibility(View.GONE);
                helper.getView(R.id.pingjia).setVisibility(View.GONE);
                helper.getView(R.id.yudin_again).setVisibility(View.GONE);
                helper.getView(R.id.order_delete).setVisibility(View.GONE);
                helper.getView(R.id.tuikuan_apply).setVisibility(View.VISIBLE);
                helper.addOnClickListener(R.id.tuikuan_apply);
            } else if (item.order_status == 1) {
                helper.setText(R.id.order_state, "入住中");
                helper.getView(R.id.order_cancel).setVisibility(View.GONE);
                helper.getView(R.id.order_pay).setVisibility(View.GONE);
                helper.getView(R.id.tiqian_tuifang).setVisibility(View.VISIBLE);
                helper.getView(R.id.yudin_again).setVisibility(View.GONE);
                helper.getView(R.id.order_delete).setVisibility(View.GONE);
                helper.getView(R.id.pingjia).setVisibility(View.GONE);
                helper.getView(R.id.tuikuan_apply).setVisibility(View.GONE);
                helper.addOnClickListener(R.id.tiqian_tuifang);
            } else if (item.order_status == 2) {
                if (item.comment==1)
                {
                    helper.setText(R.id.pingjia,"查看评价");
                    helper.getView(R.id.pingjia).setVisibility(View.VISIBLE);
                    helper.addOnClickListener(R.id.pingjia);
                }else {
                    helper.getView(R.id.pingjia).setVisibility(View.VISIBLE);
                    helper.addOnClickListener(R.id.pingjia);
                    helper.setText(R.id.pingjia,"评价");
                }
                helper.setText(R.id.order_state, "已退房");
                helper.getView(R.id.order_cancel).setVisibility(View.GONE);
                helper.getView(R.id.order_pay).setVisibility(View.GONE);
                helper.getView(R.id.tiqian_tuifang).setVisibility(View.GONE);
                helper.getView(R.id.yudin_again).setVisibility(View.VISIBLE);
               // helper.getView(R.id.pingjia).setVisibility(View.VISIBLE);
                helper.getView(R.id.order_delete).setVisibility(View.VISIBLE);
                helper.addOnClickListener(R.id.order_delete);
                helper.getView(R.id.tuikuan_apply).setVisibility(View.GONE);
                helper.addOnClickListener(R.id.yudin_again);
                helper.getView(R.id.pingjia).setVisibility(View.VISIBLE);
            } else if (item.order_status == 3) {
               if (item.is_tuikuan==1)
               {
                   helper.setText(R.id.order_state, "已退款");
                   helper.getView(R.id.order_cancel).setVisibility(View.GONE);
                   helper.getView(R.id.order_pay).setVisibility(View.GONE);
                   helper.getView(R.id.pingjia).setVisibility(View.GONE);
                   helper.getView(R.id.tiqian_tuifang).setVisibility(View.GONE);
                   helper.getView(R.id.yudin_again).setVisibility(View.VISIBLE);
                   helper.getView(R.id.order_dells).setVisibility(View.VISIBLE);
                   helper.addOnClickListener(R.id.order_dells);
                   helper.getView(R.id.tuikuan_apply).setVisibility(View.GONE);
               }else if (item.is_tuikuan==-1)
               {
                   helper.setText(R.id.order_state, "拒绝退款");
                   helper.getView(R.id.order_cancel).setVisibility(View.GONE);
                   helper.getView(R.id.order_pay).setVisibility(View.GONE);
                   helper.getView(R.id.pingjia).setVisibility(View.GONE);
                   helper.getView(R.id.tiqian_tuifang).setVisibility(View.GONE);
                   helper.getView(R.id.yudin_again).setVisibility(View.GONE);
                   helper.getView(R.id.order_delete).setVisibility(View.GONE);
                   helper.getView(R.id.tuikuan_apply).setVisibility(View.GONE);
               }else {
                   helper.setText(R.id.order_state, "退款审核中");
                   helper.getView(R.id.order_cancel).setVisibility(View.GONE);
                   helper.getView(R.id.order_pay).setVisibility(View.GONE);
                   helper.getView(R.id.pingjia).setVisibility(View.GONE);
                   helper.getView(R.id.tiqian_tuifang).setVisibility(View.GONE);
                   helper.getView(R.id.yudin_again).setVisibility(View.GONE);
                   helper.getView(R.id.order_delete).setVisibility(View.GONE);
                   helper.getView(R.id.tuikuan_apply).setVisibility(View.GONE);
               }
            } else if (item.order_status == 4) {
                if (item.is_tuifang==0){
                    helper.setText(R.id.order_state, "提前退房审核中");
                }else if (item.is_tuifang==1){
                    helper.setText(R.id.order_state, "提前退房成功");
                    helper.getView(R.id.order_delete).setVisibility(View.VISIBLE);
                    helper.addOnClickListener(R.id.order_delete);
                }else if (item.is_tuifang==-1){
                    helper.setText(R.id.order_state, "拒绝提前退房");
                   // helper.getView(R.id.order_delete).setVisibility(View.VISIBLE);
                }
                helper.getView(R.id.order_cancel).setVisibility(View.GONE);
                helper.getView(R.id.order_pay).setVisibility(View.GONE);
                helper.getView(R.id.tiqian_tuifang).setVisibility(View.GONE);
                helper.getView(R.id.yudin_again).setVisibility(View.GONE);
                helper.getView(R.id.order_delete).setVisibility(View.GONE);
                helper.getView(R.id.tuikuan_apply).setVisibility(View.GONE);
                helper.getView(R.id.pingjia).setVisibility(View.GONE);
            }
        } else if (item.pay_status == -1) {
            helper.setText(R.id.order_state, "已取消");
            helper.getView(R.id.order_cancel).setVisibility(View.GONE);
            helper.getView(R.id.order_pay).setVisibility(View.GONE);
            helper.getView(R.id.tuikuan_apply).setVisibility(View.GONE);
            helper.getView(R.id.yudin_again).setVisibility(View.VISIBLE);
            helper.getView(R.id.order_delete).setVisibility(View.VISIBLE);
            helper.addOnClickListener(R.id.order_delete);
            helper.addOnClickListener(R.id.yudin_again);
        }


        Glide.with(mContext)
                .load(Constant.BASE2_URL + item.house_img)
                .into((ImageView) helper.getView(R.id.order_roomImg));
        if (item.head_pic==null)
        {
            Glide.with(mContext)
                    .load("http://minsu.zyeo.net/Public/img/user.png")
                    .into((ImageView) helper.getView(R.id.order_userImg));
            return;
        }
        if (item.head_pic.contains("http")) {
            Glide.with(mContext)
                    .load(item.head_pic)
                    .into((ImageView) helper.getView(R.id.order_userImg));
        } else {
            Glide.with(mContext)
                    .load(Constant.BASE2_URL + item.head_pic)
                    .into((ImageView) helper.getView(R.id.order_userImg));
        }



    }
}
