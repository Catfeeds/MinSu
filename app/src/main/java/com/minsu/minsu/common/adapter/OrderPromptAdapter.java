package com.minsu.minsu.common.adapter;

import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.minsu.minsu.App;
import com.minsu.minsu.R;
import com.minsu.minsu.api.Constant;
import com.minsu.minsu.common.bean.OrderBean;
import com.minsu.minsu.common.bean.OrderBean2;
import com.minsu.minsu.utils.DateUtil;
import com.minsu.minsu.widget.RoundedCornerImageView;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by hpc on 2018/1/8.
 */

public class OrderPromptAdapter extends BaseQuickAdapter<OrderBean2, BaseViewHolder> {
    public OrderPromptAdapter(int layoutResId, @Nullable List<OrderBean2> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, OrderBean2 item) {
        helper.setText(R.id.dingdan_number, item.getOrder_sn());
        helper.setText(R.id.tv_name_fz, item.getTitle());
        helper.setText(R.id.dingdan_info, item.getHouse_info());
        helper.setText(R.id.dizhi, item.getCity()+item.getDistrict()+item.getTown());
        helper.setText(R.id.money, "￥ "+item.getTotal_price());
        helper.setText(R.id.ruzhu_shijian, item.getCheck_time());
        helper.addOnClickListener(R.id.tv_delete);
        helper.setText(R.id.all_tian, "共"+item.getDays()+"晚");
      //  Date date=new Date();
       // String time=date.getYear()+"-"+date.getMonth()+"-"+date.getDay();
        String time=item.getCreate_time()+"000";
        long l = Long.parseLong(time);
        Log.i("getCreate_time",System.currentTimeMillis()+"");
        Date date = new Date(l);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
        helper.setText(R.id.dingdan_time,formatter.format(date));
        helper.setText(R.id.tv_name_fd,item.getN_name());
        helper.setText(R.id.tv_number_fd,item.getFd_mobile());
        if (item.getHead_pic()==null)
        {
            return;
        }
        if (item.getHead_pic().contains("http")) {
            Glide.with(App.getInstance())
                    .load(item.getHead_pic())
                    .into((RoundedCornerImageView) helper.getView(R.id.iv_touxiang));
        } else {
            Glide.with(App.getInstance())
                    .load(Constant.BASE2_URL + item.getHead_pic())
                    .error(R.mipmap.default_back)
                    .into((RoundedCornerImageView) helper.getView(R.id.iv_touxiang));
        }
        ImageView imageView=helper.getView(R.id.dingdan_iv);
        Glide.with(App.getInstance())
                .load(Constant.BASE2_URL + item.getHouse_img())
                .error(R.mipmap.default_back)
                .into(imageView);

        if (item.getType()==1) {
            helper.setText(R.id.dingdan_state, "订单成功");
        } else if (item.getType()==2) {
            if (item.getIs_tuikuan()==1)
            {
                helper.setText(R.id.dingdan_state, "退款成功");
            }else if (item.getIs_tuikuan()==-1)
            {
                helper.setText(R.id.dingdan_state, "拒绝退款");
            }else {
                helper.setText(R.id.dingdan_state, "审核中");
            }

        }
    }
}
