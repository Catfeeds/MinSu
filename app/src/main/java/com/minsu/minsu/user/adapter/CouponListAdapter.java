package com.minsu.minsu.user.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.minsu.minsu.R;
import com.minsu.minsu.user.bean.CouponListBean;

import java.util.List;

/**
 * Created by hpc on 2018/1/8.
 */

public class CouponListAdapter extends BaseQuickAdapter<CouponListBean.Data, BaseViewHolder> {
    private String type;

    public CouponListAdapter(int layoutResId, @Nullable List<CouponListBean.Data> data, String type) {
        super(layoutResId, data);
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, CouponListBean.Data item) {
        helper.setText(R.id.coupon_name, item.info);
        helper.setText(R.id.coupon_price, item.price + "");
        if (type.equals("mo_person")) {
            helper.addOnClickListener(R.id.coupon_status);
            if (item.is_type == 1) {
                helper.setText(R.id.coupon_status, "已领取");
            } else if (item.is_type == 0) {
                helper.setText(R.id.coupon_status, "未领取");
            }
        } else {
            if (item.is_type == 1) {
                helper.setText(R.id.coupon_status, "已过期");
            } else if (item.is_type == 0) {
                helper.setText(R.id.coupon_status, "未过期");
            }
        }
        helper.setText(R.id.coupon_term_validity, "有效期:" + item.start_time + "至" + item.end_time);
    }
}
