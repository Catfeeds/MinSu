package com.zhuye.minsu.user;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhuye.minsu.R;
import com.zhuye.minsu.api.DataProvider;
import com.zhuye.minsu.base.BaseActivity;
import com.zhuye.minsu.houseResource.adapter.FacilitiesBean;
import com.zhuye.minsu.utils.ToastManager;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FacilitiesActivity extends BaseActivity {


    @BindView(R.id.id_flowlayout)
    TagFlowLayout idFlowlayout;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.iv_left)
    ImageView ivLeft;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private ImageView peitaoImg;
    private static final String TAG = "FacilitiesActivity";

    @Override
    protected void processLogic() {

    }

    public Map<Integer, Boolean> dat;

    @Override
    protected void setListener() {
        toolbarTitle.setText("设施配套");
        ivLeft.setVisibility(View.VISIBLE);
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("确定");
        tvRight.setTextColor(Color.BLACK);
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<FacilitiesBean> choose = new ArrayList<>();
                for (int i = 0; i < DataProvider.getFacilitiesList().size(); i++) {
                    if (dat.get(i)) {
                        choose.add(DataProvider.getFacilitiesList().get(i));
                    }
                }
                if (choose.size() == 0) {
                    ToastManager.show("请选择配套设施");
                    return;
                }
                Log.i(TAG, "onClick: " + choose);

            }
        });
        final LayoutInflater mInflater = LayoutInflater.from(this);
        dat = new HashMap(30);
        for (int i = 0; i < 30; i++) {
            dat.put(i, false);
        }
        idFlowlayout.setAdapter(new TagAdapter<FacilitiesBean>(DataProvider.getFacilitiesList()) {
            @Override
            public View getView(FlowLayout parent, int position, FacilitiesBean item) {
                View view = mInflater.inflate(R.layout.item_facilities, idFlowlayout, false);
                TextView peitaoName = view.findViewById(R.id.peitao_name);

                peitaoImg = view.findViewById(R.id.peitao_img);
                peitaoName.setText(item.name);
                Glide.with(FacilitiesActivity.this)
                        .load(item.img)
//                        .placeholder(R.mipmap.ic_launcher)
                        .into(peitaoImg);
                return view;
            }
        });


        idFlowlayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {

                ImageView iv = view.findViewById(R.id.peitao_img);
                TextView name = view.findViewById(R.id.peitao_name);
                if (dat.get(position)) {
                    Glide.with(FacilitiesActivity.this)
                            .load(DataProvider.getFacilitiesImage().get(position))
                            .into(iv);
                    name.setTextColor(getResources().getColor(R.color.black));

                } else {
                    Glide.with(FacilitiesActivity.this)
                            .load(DataProvider.getFacilitiesSecImage().get(position))
                            .into(iv);
                    name.setTextColor(getResources().getColor(R.color.orangered));

                }
                dat.put(position, !dat.get(position));
                return true;
            }
        });
        Set<Integer> selectedList = idFlowlayout.getSelectedList();
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_facilities);
    }

    @Override
    protected Context getActivityContext() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
