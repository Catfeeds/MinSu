package com.zhuye.minsu.houseResource;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.lzy.okgo.model.Response;
import com.zhuye.minsu.R;
import com.zhuye.minsu.api.MinSuApi;
import com.zhuye.minsu.api.callback.CallBack;
import com.zhuye.minsu.base.BaseActivity;
import com.zhuye.minsu.houseResource.adapter.RoomTypeAdapter;
import com.zhuye.minsu.houseResource.adapter.SpaceTypeAdapter;
import com.zhuye.minsu.houseResource.bean.HouseBean;
import com.zhuye.minsu.user.AddressActivity;
import com.zhuye.minsu.utils.StorageUtil;
import com.zhuye.minsu.utils.ToastManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddHouseActivity extends BaseActivity implements View.OnClickListener {


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
    @BindView(R.id.nextStep)
    TextView nextStep;
    @BindView(R.id.room_type)
    TextView roomType;
    @BindView(R.id.img_click_one)
    ImageView imgClickOne;
    @BindView(R.id.space_type)
    TextView spaceType;
    @BindView(R.id.img_click_two)
    ImageView imgClickTwo;
    String tokenId;
    @BindView(R.id.room_type_description)
    TextView roomTypeDescription;
    @BindView(R.id.space_type_description)
    TextView spaceTypeDescription;
    private ArrayList<HouseBean.Data1> data1;
    private ArrayList<HouseBean.Data2> data2;
    private ArrayList<HouseBean.Data3> data3;

    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {
        tokenId = StorageUtil.getTokenId(this);
        toolbarTitle.setText("添加房源");
        ivLeft.setVisibility(View.VISIBLE);
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        MinSuApi.addHouse(0x001, tokenId, callBack);
        nextStep.setOnClickListener(this);
        imgClickOne.setOnClickListener(this);
        imgClickTwo.setOnClickListener(this);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_add_house);
    }

    @Override
    protected Context getActivityContext() {
        return this;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.nextStep:
                if (roomType.getText().toString().equals("")) {
                    ToastManager.show("请选择房源类型");
                    return;
                }
                if (spaceType.getText().toString().equals("")) {
                    ToastManager.show("请选择空间类型");
                    return;
                }
                Intent intent = new Intent(this, PublishRommActivity.class);
                intent.putExtra("data1", (Serializable) data1);
                startActivity(intent);
                break;
            case R.id.img_click_one:
                final RoomTypeAdapter roomTypeAdapter = new RoomTypeAdapter(R.layout.room_type, data2);
                View roomTypeView = LayoutInflater.from(this).inflate(R.layout.room_type_list, null);
                final PopupWindow roomPopupWindow = new PopupWindow(this);
                roomPopupWindow.setContentView(roomTypeView);
                RecyclerView recyclerView = roomTypeView.findViewById(R.id.recyclerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setAdapter(roomTypeAdapter);
                roomTypeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        roomType.setText(roomTypeAdapter.getItem(position).name);
                        roomTypeDescription.setText(roomTypeAdapter.getItem(position).info);
                        StorageUtil.setKeyValue(AddHouseActivity.this,"room_type_id",roomTypeAdapter.getItem(position).id+"");
                        roomPopupWindow.dismiss();
                    }
                });
                roomPopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                roomPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                roomPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                roomPopupWindow.setOutsideTouchable(true);
                roomPopupWindow.showAtLocation(roomTypeView, Gravity.CENTER_HORIZONTAL, 0, 0);
                roomPopupWindow.showAsDropDown(imgClickOne);
                break;
            case R.id.img_click_two:
                final SpaceTypeAdapter spaceTypeAdapter = new SpaceTypeAdapter(R.layout.room_type, data3);
                View spaceTypeView = LayoutInflater.from(this).inflate(R.layout.room_type_list, null);
                final PopupWindow spacePopupWindow = new PopupWindow(this);
                spacePopupWindow.setContentView(spaceTypeView);
                RecyclerView spaceRecyclerView = spaceTypeView.findViewById(R.id.recyclerView);
                spaceRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                spaceRecyclerView.setAdapter(spaceTypeAdapter);
                spaceTypeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        spaceType.setText(spaceTypeAdapter.getItem(position).name);
                        spaceTypeDescription.setText(spaceTypeAdapter.getItem(position).info);
                        StorageUtil.setKeyValue(AddHouseActivity.this,"space_type_id",spaceTypeAdapter.getItem(position).id+"");
                        spacePopupWindow.dismiss();
                    }
                });
                spacePopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                spacePopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                spacePopupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                spacePopupWindow.setOutsideTouchable(true);
                spacePopupWindow.showAtLocation(spaceTypeView, Gravity.CENTER_HORIZONTAL, 0, 0);
                spacePopupWindow.showAsDropDown(imgClickTwo);
                break;
        }
    }

    private CallBack callBack = new CallBack() {
        @Override
        public void onSuccess(int what, Response<String> result) {
            switch (what) {
                case 0x001:
                    try {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        String msg = jsonObject.getString("msg");
                        if (code == 200) {
                            HouseBean houseBean = new Gson().fromJson(result.body(), HouseBean.class);

                            data1 = houseBean.data1;
                            data2 = houseBean.data2;
                            data3 = houseBean.data3;
                        } else if (code == 111) {
                            ToastManager.show(msg);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }

        @Override
        public void onFail(int what, Response<String> result) {

        }

        @Override
        public void onFinish(int what) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
