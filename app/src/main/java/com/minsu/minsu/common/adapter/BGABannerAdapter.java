package com.minsu.minsu.common.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.minsu.minsu.R;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * Created by XY on 2016/9/14.
 */
public class BGABannerAdapter implements BGABanner.Adapter {
    private Context context;

    public BGABannerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public void fillBannerItem(final BGABanner banner, View view, Object model, int position) {
          ImageView imageView= (ImageView) view;
         // imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        Glide.with(context)
                .load(model.toString())
                .into(imageView);


        view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                itemClick.onItemClick(view);
            }
        });

        view.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View view)
            {
                longClick.itemLongClick(banner.getCurrentItem());
                return false;
            }
        });

    }
    public interface ItemClick{
         void onItemClick(View view);
    }
    private ItemClick itemClick;
    public void setItemClick(ItemClick itemClick)
    {
        this.itemClick=itemClick;
    }
    private OnLongClick longClick;
    public void setLongClick(OnLongClick longClick)
    {
        this.longClick=longClick;
    }
    public interface OnLongClick
    {
         void itemLongClick(int position);
    }
}
