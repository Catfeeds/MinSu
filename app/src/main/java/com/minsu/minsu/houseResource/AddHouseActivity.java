package com.minsu.minsu.houseResource;

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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.google.gson.Gson;
import com.lzy.okgo.model.Response;
import com.minsu.minsu.R;
import com.minsu.minsu.api.MinSuApi;
import com.minsu.minsu.api.callback.CallBack;
import com.minsu.minsu.base.BaseActivity;
import com.minsu.minsu.houseResource.adapter.RoomTypeAdapter;
import com.minsu.minsu.houseResource.adapter.SpaceTypeAdapter;
import com.minsu.minsu.houseResource.bean.HouseBean;
import com.minsu.minsu.user.LandlordAuthenticationActivity;
import com.minsu.minsu.user.camera.PermissionUtil;
import com.minsu.minsu.utils.StorageUtil;
import com.minsu.minsu.utils.ToastManager;

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
    @BindView(R.id.rl_click_one)
    RelativeLayout rlClickOne;
    @BindView(R.id.rl_click_two)
    RelativeLayout rlClickTwo;
    private RecyclerView rvone,rvtwo;
    private ArrayList<HouseBean.Data1> data1;
    private ArrayList<HouseBean.Data2> data2;
    private ArrayList<HouseBean.Data3> data3;
    private LinearLayout rootView;

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
        rlClickOne.setOnClickListener(this);
        rlClickTwo.setOnClickListener(this);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_add_house);
        rootView = (LinearLayout) LayoutInflater.from(AddHouseActivity.this).inflate(R.layout.activity_add_house,null);

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
            case R.id.rl_click_one:
//                rvone.setVisibility(View.VISIBLE);
//                final RoomTypeAdapter roomTypeAdapter = new RoomTypeAdapter(R.layout.room_type, data2);
//                LinearLayout roomTypeView = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.room_type_list, null);
//                LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                params.setMargins(0,550,0,0);
//                final PopupWindow roomPopupWindow = new PopupWindow(this);
//                roomTypeView.setLayoutParams(params);
//               // roomPopupWindow.showAtLocation(rootView,0,0,555);
//                roomPopupWindow.setContentView(roomTypeView);
//                RecyclerView recyclerView = roomTypeView.findViewById(R.id.recyclerView);
//                rvone.setLayoutManager(new LinearLayoutManager(this));
//                rvone.setAdapter(roomTypeAdapter);
//                roomTypeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                        roomType.setText(roomTypeAdapter.getItem(position).name);
//                        roomTypeDescription.setText(roomTypeAdapter.getItem(position).info);
//                        StorageUtil.setKeyValue(AddHouseActivity.this, "room_type_id", roomTypeAdapter.getItem(position).id + "");
//                       // roomPopupWindow.dismiss();
//                        rvone.setVisibility(View.GONE);
//                    }
//                });
//                roomPopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
//                roomPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
//                roomPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
//                roomPopupWindow.setOutsideTouchable(true);
//                roomPopupWindow.showAtLocation(roomTypeDescription, Gravity.CENTER_HORIZONTAL, 0, -255);
//                roomPopupWindow.showAsDropDown(imgClickOne);

                tan();
                break;
            case R.id.rl_click_two:

//                rvtwo.setVisibility(View.VISIBLE);
//                final SpaceTypeAdapter spaceTypeAdapter = new SpaceTypeAdapter(R.layout.room_type, data3);
//                LinearLayout spaceTypeView = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.room_type_list, null);
//                LinearLayout.LayoutParams param=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                param.setMargins(0,30,0,0);
//                spaceTypeView.setLayoutParams(param);
//                final PopupWindow spacePopupWindow = new PopupWindow(this);
//                spacePopupWindow.setContentView(spaceTypeView);
//                RecyclerView spaceRecyclerView = spaceTypeView.findViewById(R.id.recyclerView);
//                rvtwo.setLayoutManager(new LinearLayoutManager(this));
//                rvtwo.setAdapter(spaceTypeAdapter);
//                spaceTypeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                        spaceType.setText(spaceTypeAdapter.getItem(position).name);
//                        spaceTypeDescription.setText(spaceTypeAdapter.getItem(position).info);
//                        StorageUtil.setKeyValue(AddHouseActivity.this, "space_type_id", spaceTypeAdapter.getItem(position).id + "");
//                    //    spacePopupWindow.dismiss();
//                        rvtwo.setVisibility(View.GONE);
//                    }
//                });
//                spacePopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
//                spacePopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
//                spacePopupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
//                spacePopupWindow.setOutsideTouchable(true);
//                spacePopupWindow.showAtLocation(spaceTypeView, Gravity.CENTER_HORIZONTAL, 0, 0);
//                spacePopupWindow.showAsDropDown(imgClickTwo);
                tanjian();
                break;
        }
    }

    //下弹
    private void tan()
    {

        final String[] stringItems = {"短租", "民宿","酒店"};
        if (data2==null)
        {
            ToastManager.show("网络有点卡请等待");
            return;
        }
        for (int i=0;i<data2.size();i++)
        {

        }
        final ActionSheetDialog dialog = new ActionSheetDialog(mContext, stringItems, null);
        dialog.title("房源类型").titleTextSize_SP(14.5f).show();


        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                roomType.setText(data2.get(position).name);
                roomTypeDescription.setText(data2.get(position).info);
                StorageUtil.setKeyValue(AddHouseActivity.this, "room_type_id", data2.get(position).id + "");
                dialog.dismiss();

            }
        });
    }
    //但双肩
    private void tanjian()
    {
        final String[] stringItems = {"单人间", "双人间"};
        final ActionSheetDialog dialog = new ActionSheetDialog(mContext, stringItems, null);
        dialog.title("空间类型").titleTextSize_SP(14.5f).show();


        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                spaceType.setText(data3.get(position).name);
                spaceTypeDescription.setText(data3.get(position).info);
                StorageUtil.setKeyValue(AddHouseActivity.this, "space_type_id", data3.get(position).id + "");
                dialog.dismiss();

            }
        });
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
