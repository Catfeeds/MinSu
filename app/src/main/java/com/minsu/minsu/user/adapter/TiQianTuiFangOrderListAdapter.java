package com.minsu.minsu.user.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

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

public class TiQianTuiFangOrderListAdapter extends BaseQuickAdapter<OrderBean.Data, BaseViewHolder> {
    public TiQianTuiFangOrderListAdapter(int layoutResId, @Nullable List<OrderBean.Data> data) {
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
        helper.setText(R.id.order_price, "￥：" + item.total_price);
        helper.setText(R.id.location_address, item.city + " " + item.district + " " + item.town);
        helper.getView(R.id.delete_ordertqtf).setVisibility(View.GONE);
        if (item.pay_status == 0) {
            helper.setText(R.id.order_state, "待支付");
            //取消订单或者立即支付
//            helper.addOnClickListener(R.id.order_cancel);
//            helper.addOnClickListener(R.id.order_pay);
        } else if (item.pay_status == 1) {
            if (item.order_status == 0) {
                helper.setText(R.id.order_state, "待入住");
//                helper.addOnClickListener(R.id.tuikuan_apply);
            } else if (item.order_status == 1) {
                helper.setText(R.id.order_state, "入住中");
            } else if (item.order_status == 2) {
                helper.setText(R.id.order_state, "已退房");
            } else if (item.order_status == 3) {
                helper.setText(R.id.order_state, "已退款");
            } else if (item.order_status == 4) {
                if (item.is_tuifang==0){
                    helper.setText(R.id.order_state, "提前退房审核中");
                }else if (item.is_tuifang==1){
                    helper.setText(R.id.order_state, "提前退房成功");
                    helper.addOnClickListener(R.id.delete_ordertqtf);
                    helper.getView(R.id.delete_ordertqtf).setVisibility(View.VISIBLE);
                }else if (item.is_tuifang==-1){
                    //helper.getView(R.id.delete_ordertqtf).setVisibility(View.VISIBLE);
                    helper.setText(R.id.order_state, "拒绝提前退房");
                }

            }
        } else if (item.pay_status == -1) {
            helper.setText(R.id.order_state, "已取消");
//            helper.addOnClickListener(R.id.order_delete);
//            helper.addOnClickListener(R.id.yudin_again);
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
