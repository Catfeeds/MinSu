package com.minsu.minsu.houseResource.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.minsu.minsu.R;
import com.minsu.minsu.houseResource.bean.HouseBean;

import java.util.List;

/**
 * Created by hpc on 2018/1/3.
 */

public class SpaceTypeAdapter extends BaseQuickAdapter<HouseBean.Data3, BaseViewHolder> {
    public SpaceTypeAdapter(int layoutResId, @Nullable List<HouseBean.Data3> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HouseBean.Data3 item) {
        helper.setText(R.id.pop_type, item.name);
    }
}
