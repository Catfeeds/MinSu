package com.minsu.minsu.houseResource;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.lzy.okgo.model.Response;
import com.minsu.minsu.R;
import com.minsu.minsu.api.DataProvider;
import com.minsu.minsu.api.MinSuApi;
import com.minsu.minsu.api.callback.CallBack;
import com.minsu.minsu.base.BaseActivity;
import com.minsu.minsu.common.adapter.BGABannerAdapter;
import com.minsu.minsu.houseResource.adapter.CityAdapter;
import com.minsu.minsu.houseResource.adapter.FacilitiesBean;
import com.minsu.minsu.houseResource.adapter.ProvinceAdapter;
import com.minsu.minsu.houseResource.bean.CityBean;
import com.minsu.minsu.houseResource.bean.HouseBean;
import com.minsu.minsu.user.camera.FileUtil;
import com.minsu.minsu.utils.StorageUtil;
import com.minsu.minsu.utils.ToastManager;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.bgabanner.BGABanner;
import me.iwf.photopicker.PhotoPicker;

public class PublishRommActivity extends BaseActivity implements View.OnClickListener {


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
    @BindView(R.id.tag1)
    TextView tag1;
    @BindView(R.id.room_description)
    TextView roomDescription;
    @BindView(R.id.rl_room_description)
    RelativeLayout rlRoomDescription;
    @BindView(R.id.tag2)
    TextView tag2;
    @BindView(R.id.room_info)
    TextView roomInfo;
    @BindView(R.id.rl_room_info)
    RelativeLayout rlRoomInfo;
    @BindView(R.id.tag3)
    TextView tag3;
    @BindView(R.id.room_price)
    TextView roomPrice;
    @BindView(R.id.rl_room_price)
    RelativeLayout rlRoomPrice;
    @BindView(R.id.tag4)
    TextView tag4;
    @BindView(R.id.support_facilities)
    TextView supportFacilities;
    @BindView(R.id.rl_support_facilities)
    RelativeLayout rlSupportFacilities;
    @BindView(R.id.tag5)
    TextView tag5;
    @BindView(R.id.unsubscribe_policy)
    TextView unsubscribePolicy;
    @BindView(R.id.rl_unsubscribe_policy)
    RelativeLayout rlUnsubscribePolicy;
    @BindView(R.id.tag6)
    TextView tag6;
    @BindView(R.id.other_service)
    TextView otherService;
    @BindView(R.id.rl_other_service)
    RelativeLayout rlOtherService;
    @BindView(R.id.tag7)
    TextView tag7;
    @BindView(R.id.room_address)
    TextView roomAddress;
    @BindView(R.id.rl_room_address)
    RelativeLayout rlRoomAddress;
    @BindView(R.id.release_room)
    TextView releaseRoom;
    @BindView(R.id.add_room_picture)
    ImageView addRoomPicture;
    @BindView(R.id.banner_pager)
    BGABanner bannerPager;
    private EditText itemDescription;
    private EditText itemPrice;
    private Button confirm;
    private TextView mProvince;
    private TextView mCity;
    private TextView mArea;
    private TextView mStreet;
    private static final String TAG = "PublishRommActivity";
    private ArrayList<HouseBean.Data1> data1;
    private String tokenId;
    private RecyclerView mRecyclerView;
    private PopupWindow popWindow3;
    public Map<Integer, Boolean> dat;
    private List<FacilitiesBean> choose;
    private ArrayList<String> photos;

    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {
        toolbarTitle.setText("发布房源");
        tokenId = StorageUtil.getTokenId(this);
        data1 = (ArrayList<HouseBean.Data1>) getIntent().getSerializableExtra("data1");
        Log.i(TAG, "setListener: " + data1);
        ivLeft.setVisibility(View.VISIBLE);
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        dat = new HashMap(30);
        for (int i = 0; i < 30; i++) {
            dat.put(i, false);
        }


        rlRoomDescription.setOnClickListener(this);
        rlRoomInfo.setOnClickListener(this);
        rlRoomPrice.setOnClickListener(this);
        rlSupportFacilities.setOnClickListener(this);
        rlUnsubscribePolicy.setOnClickListener(this);
        rlOtherService.setOnClickListener(this);
        rlRoomAddress.setOnClickListener(this);
        releaseRoom.setOnClickListener(this);
        addRoomPicture.setOnClickListener(this);

    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_publish_romm);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_room_description:
                showPopup01(view);//显示
                popupInputMethodWindow();//异步操作打开软键盘
                break;
            case R.id.rl_room_info:
                showPopup05(view);//显示
                popupInputMethodWindow();//异步操作打开软键盘
                break;
            case R.id.rl_room_price:
                showPopup02(view);//显示
                popupInputMethodWindow();//异步操作打开软键盘
                break;
            case R.id.rl_room_address:
                showPopup03(view);//显示
                break;
            case R.id.rl_support_facilities:
                showPopup04(view);//显示
//                Intent intent = new Intent(this, FacilitiesActivity.class);
//                startActivity(intent);
                break;
            case R.id.release_room:
                String province_id = StorageUtil.getValue(PublishRommActivity.this, "province_id");
                String city_id = StorageUtil.getValue(PublishRommActivity.this, "city_id");
                String area_id = StorageUtil.getValue(PublishRommActivity.this, "area_id");
                String street_id = StorageUtil.getValue(PublishRommActivity.this, "street_id");
                String room_type_id = StorageUtil.getValue(PublishRommActivity.this, "room_type_id");
                String space_type_id = StorageUtil.getValue(PublishRommActivity.this, "space_type_id");
                ArrayList<File> files = new ArrayList<>();
                String room_price = roomPrice.getText().toString();
                for (int i = 0; i < photos.size(); i++) {
                    files.add(FileUtil.getSmallBitmap(PublishRommActivity.this, photos.get(i)));
                }
                if (room_price.equals("")) {
                    ToastManager.show("房源价格不能为空");
                    return;
                }
                //"1,4,7,8,6"
                Log.i(TAG, "onClick: nihao" + choose.size());
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < choose.size(); i++) {
                    int status = choose.get(i).status;
                    sb.append(String.valueOf(status) + ",");

                }
                Log.i(TAG, "onClick: pinjie" + sb.toString());
                String roomTitle = roomDescription.getText().toString();
                String roomDetail = roomInfo.getText().toString();
                if (roomTitle.equals("")) {
                    ToastManager.show("请输入房间名称");
                    return;
                }
                if (roomDetail.equals("")) {
                    ToastManager.show("请输入房间详情");
                    return;
                }
                MinSuApi.addHouseSubmit(this, 0x004, tokenId, Integer.parseInt(room_type_id), Integer.parseInt(space_type_id), roomTitle,
                        roomDetail, room_price, sb.toString().substring(0, sb.toString().length() - 1),
                        Integer.parseInt(province_id), Integer.parseInt(city_id), Integer.parseInt(area_id),
                        Integer.parseInt(street_id), files, callBack);
                break;
            case R.id.add_room_picture:
                PhotoPicker.builder()
                        .setPhotoCount(9)
                        .setShowCamera(true)
                        .setShowGif(true)
                        .setPreviewEnabled(false)
                        .start(this, PhotoPicker.REQUEST_CODE);
                break;
        }
    }

    private void showPopup05(View parent) {
        View contentView = LayoutInflater.from(this).inflate(
                R.layout.room_detail_info_input, null);
        final PopupWindow popWindow = new PopupWindow(contentView,
                RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT,
                true);
        itemDescription = contentView.findViewById(R.id.item_description);
        TextView confirm_detail = contentView.findViewById(R.id.confirm_detail);
        confirm_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                roomInfo.setText(itemDescription.getText().toString());
                popWindow.dismiss();
            }
        });
        contentView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (popWindow != null) {
                        popWindow.dismiss();
                    }
                }
                return false;
            }
        });
//        popWindow.setAnimationStyle(R.style.popupWindowAnimation);//设置动画
        popWindow.setFocusable(true);
        // 设置允许在外点击消失
        popWindow.setOutsideTouchable(true);
        // 设置背景，这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popWindow.setBackgroundDrawable(new BitmapDrawable());
        //软键盘不会挡着popupwindow
        popWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        //设置SelectPicPopupWindow弹出窗体的背景
//        WindowManager.LayoutParams lp = getWindow().getAttributes();
//        lp.alpha = 0.7f;
//        getWindow().setAttributes(lp);
        //设置菜单显示的位置
        popWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        //监听菜单的关闭事件
        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
//                WindowManager.LayoutParams lp = getWindow().getAttributes();
//                lp.alpha = 1f;
//                getWindow().setAttributes(lp);
            }
        });
        //监听触屏事件
        popWindow.setTouchInterceptor(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            if (data != null) {


                photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                Log.i(TAG, "onActivityResult: " + photos);
//                for (String imagePath : photos) {
//                    File bitmap = FilesUtil.getSmallBitmap(PublishRommActivity.this, imagePath);//压缩图片
//                    addRoomPicture.setImageURI(Uri.fromFile(bitmap));
//
//                }
                addRoomPicture.setVisibility(View.GONE);
                bannerPager.setAdapter(new BGABannerAdapter(PublishRommActivity.this));
//        ArrayList<Integer> bannerImageData = DataProvider.getBannerImage();
                List<String> bannerImage = new ArrayList<>();
                for (int i = 0; i < photos.size(); i++) {
                    bannerImage.add(photos.get(i));
                }
                bannerPager.setData(bannerImage, null);
            }
        }
    }

    private void showPopup04(View parent) {
        View contentView = LayoutInflater.from(this).inflate(
                R.layout.room_facilities, null);
        final PopupWindow popWindow = new PopupWindow(contentView,
                RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT,
                true);
//        RecyclerView recyclerView = contentView.findViewById(R.id.recyclerView);
        choose = new ArrayList<>();
        final LayoutInflater mInflater = LayoutInflater.from(this);
        final TagFlowLayout mFlowLayout = contentView.findViewById(R.id.id_flowlayout);
        final ImageView cancel = contentView.findViewById(R.id.cancel);
        final Button conform = contentView.findViewById(R.id.confirm);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popWindow.dismiss();
            }
        });
        conform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (int i = 0; i < DataProvider.getFacilitiesList().size(); i++) {
                    if (dat.get(i)) {
                        choose.add(DataProvider.getFacilitiesList().get(i));
                    }
                }
                Log.i(TAG, "onClick: " + choose);
                if (choose.size() == 0) {
                    ToastManager.show("请选择配套设施");
                    return;
                }
                popWindow.dismiss();

            }
        });
//        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
//        FacilitiesAdapter facilitiesAdapter = new FacilitiesAdapter(R.layout.item_facilities, DataProvider.getFacilitiesList());
//        popWindow.setAnimationStyle(R.style.popupWindowAnimation);//设置动画
//        recyclerView.setAdapter(facilitiesAdapter);
        mFlowLayout.setAdapter(new TagAdapter<FacilitiesBean>(DataProvider.getFacilitiesList()) {
            @Override
            public View getView(FlowLayout parent, int position, FacilitiesBean item) {
                View view = mInflater.inflate(R.layout.item_facilities, mFlowLayout, false);
                ImageView peitaoTu = view.findViewById(R.id.peitao_img);
                TextView peitaoName = view.findViewById(R.id.peitao_name);
                peitaoName.setText(item.name);
                Glide.with(PublishRommActivity.this)
                        .load(item.img)
//                        .placeholder(R.mipmap.ic_launcher)
                        .into(peitaoTu);
                return view;
            }
        });
        mFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                ImageView iv = view.findViewById(R.id.peitao_img);
                TextView name = view.findViewById(R.id.peitao_name);
                if (dat.get(position)) {
                    Glide.with(PublishRommActivity.this)
                            .load(DataProvider.getFacilitiesImage().get(position))
                            .into(iv);
                    name.setTextColor(getResources().getColor(R.color.black));

                } else {
                    Glide.with(PublishRommActivity.this)
                            .load(DataProvider.getFacilitiesSecImage().get(position))
                            .into(iv);
                    name.setTextColor(getResources().getColor(R.color.orangered));

                }
                dat.put(position, !dat.get(position));
                return true;
            }
        });
        popWindow.setFocusable(true);
        // 设置允许在外点击消失
        popWindow.setOutsideTouchable(true);
        // 设置背景，这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popWindow.setBackgroundDrawable(new BitmapDrawable());
        //软键盘不会挡着popupwindow
        popWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        //设置SelectPicPopupWindow弹出窗体的背景
//        WindowManager.LayoutParams lp = getWindow().getAttributes();
//        lp.alpha = 0.7f;
//        getWindow().setAttributes(lp);
        //设置菜单显示的位置
        popWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        //监听菜单的关闭事件
        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
//                WindowManager.LayoutParams lp = getWindow().getAttributes();
//                lp.alpha = 1f;
//                getWindow().setAttributes(lp);
            }
        });
        //监听触屏事件
        popWindow.setTouchInterceptor(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                return false;
            }
        });
    }

    private void showPopup03(View parent) {
        View contentView = LayoutInflater.from(this).inflate(
                R.layout.room_address_input, null);
        popWindow3 = new PopupWindow(contentView,
                RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT,
                true);
        mProvince = contentView.findViewById(R.id.province);
        mCity = contentView.findViewById(R.id.city);
        mArea = contentView.findViewById(R.id.area);
        mStreet = contentView.findViewById(R.id.street);

        mRecyclerView = contentView.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mProvince.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProvinceAdapter provinceAdapter = new ProvinceAdapter(R.layout.room_type, data1);
                mRecyclerView.setAdapter(provinceAdapter);
                provinceAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        MinSuApi.getCity(0x001, tokenId, provinceAdapter.getItem(position).id, callBack);
                        StorageUtil.setKeyValue(PublishRommActivity.this, "province_id", provinceAdapter.getItem(position).id + "");
                        mProvince.setText(provinceAdapter.getItem(position).name);
                    }
                });
            }
        });
        contentView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (popWindow3 != null) {
                        popWindow3.dismiss();
                    }
                }
                return false;
            }
        });
//        popWindow.setAnimationStyle(R.style.popupWindowAnimation);//设置动画
        popWindow3.setFocusable(true);
        // 设置允许在外点击消失
        popWindow3.setOutsideTouchable(true);
        // 设置背景，这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popWindow3.setBackgroundDrawable(new BitmapDrawable());
        //设置SelectPicPopupWindow弹出窗体的背景
//        WindowManager.LayoutParams lp = getWindow().getAttributes();
//        lp.alpha = 0.7f;
//        getWindow().setAttributes(lp);
        //设置菜单显示的位置
        popWindow3.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        //监听菜单的关闭事件
        popWindow3.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
//                WindowManager.LayoutParams lp = getWindow().getAttributes();
//                lp.alpha = 1f;
//                getWindow().setAttributes(lp);
            }
        });
        //监听触屏事件
        popWindow3.setTouchInterceptor(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                return false;
            }
        });
    }

    private void showPopup02(View parent) {
        View contentView = LayoutInflater.from(this).inflate(
                R.layout.room_price_input, null);
        final PopupWindow popWindow = new PopupWindow(contentView,
                RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT,
                true);
        itemPrice = contentView.findViewById(R.id.item_price);
        confirm = contentView.findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                roomPrice.setText(itemPrice.getText().toString());
                popWindow.dismiss();
            }
        });
//        popWindow.setAnimationStyle(R.style.popupWindowAnimation);//设置动画
        popWindow.setFocusable(true);
        // 设置允许在外点击消失
        popWindow.setOutsideTouchable(true);
        // 设置背景，这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popWindow.setBackgroundDrawable(new BitmapDrawable());
        //软键盘不会挡着popupwindow
        popWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        //设置SelectPicPopupWindow弹出窗体的背景
//        WindowManager.LayoutParams lp = getWindow().getAttributes();
//        lp.alpha = 0.7f;
//        getWindow().setAttributes(lp);
        //设置菜单显示的位置
        popWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        //监听菜单的关闭事件
        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
//                WindowManager.LayoutParams lp = getWindow().getAttributes();
//                lp.alpha = 1f;
//                getWindow().setAttributes(lp);
            }
        });
        //监听触屏事件
        popWindow.setTouchInterceptor(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                return false;
            }
        });
    }

    private void popupInputMethodWindow() {
        Handler handle = new Handler();
        handle.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 0);
    }

    private void showPopup01(View parent) {
        View contentView = LayoutInflater.from(this).inflate(
                R.layout.room_detail_input, null);
        final PopupWindow popWindow = new PopupWindow(contentView,
                RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT,
                true);
        itemDescription = contentView.findViewById(R.id.item_description);
        TextView confirm_title = contentView.findViewById(R.id.confirm_title);
        confirm_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                roomDescription.setText(itemDescription.getText().toString());
                popWindow.dismiss();
            }
        });
        contentView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (popWindow != null) {
                        popWindow.dismiss();
                    }
                }
                return false;
            }
        });
//        popWindow.setAnimationStyle(R.style.popupWindowAnimation);//设置动画
        popWindow.setFocusable(true);
        // 设置允许在外点击消失
        popWindow.setOutsideTouchable(true);
        // 设置背景，这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popWindow.setBackgroundDrawable(new BitmapDrawable());
        //软键盘不会挡着popupwindow
        popWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        //设置SelectPicPopupWindow弹出窗体的背景
//        WindowManager.LayoutParams lp = getWindow().getAttributes();
//        lp.alpha = 0.7f;
//        getWindow().setAttributes(lp);
        //设置菜单显示的位置
        popWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        //监听菜单的关闭事件
        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
//                WindowManager.LayoutParams lp = getWindow().getAttributes();
//                lp.alpha = 1f;
//                getWindow().setAttributes(lp);
            }
        });
        //监听触屏事件
        popWindow.setTouchInterceptor(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                return false;
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
                        if (code == 200) {
                            CityBean cityBean = new Gson().fromJson(result.body(), CityBean.class);
                            final CityAdapter cityAdapter = new CityAdapter(R.layout.room_type, cityBean.data);
                            mRecyclerView.setAdapter(cityAdapter);
                            cityAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                    mCity.setText(cityAdapter.getItem(position).name);
                                    StorageUtil.setKeyValue(PublishRommActivity.this, "city_id", cityAdapter.getItem(position).id + "");
                                    MinSuApi.getArea(0x002, tokenId, cityAdapter.getItem(position).id, callBack);
                                }
                            });

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 0x002:
                    //公用CityBean room_type
                    try {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        if (code == 200) {
                            CityBean cityBean = new Gson().fromJson(result.body(), CityBean.class);
                            final CityAdapter cityAdapter = new CityAdapter(R.layout.room_type, cityBean.data);
                            mRecyclerView.setAdapter(cityAdapter);
                            cityAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                    mArea.setText(cityAdapter.getItem(position).name);
                                    StorageUtil.setKeyValue(PublishRommActivity.this, "area_id", cityAdapter.getItem(position).id + "");
                                    MinSuApi.gettown(0x003, tokenId, cityAdapter.getItem(position).id, callBack);
                                }
                            });

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 0x003:
                    //公用CityBean room_type
                    try {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        if (code == 200) {
                            CityBean cityBean = new Gson().fromJson(result.body(), CityBean.class);
                            final CityAdapter cityAdapter = new CityAdapter(R.layout.room_type, cityBean.data);
                            mRecyclerView.setAdapter(cityAdapter);
                            cityAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                    mStreet.setText(cityAdapter.getItem(position).name);
                                    StorageUtil.setKeyValue(PublishRommActivity.this, "street_id", cityAdapter.getItem(position).id + "");
                                    roomAddress.setText(mProvince.getText().toString() + "/" + mCity.getText().toString()
                                            + "/" + mArea.getText().toString() + "/" + mStreet.getText().toString());
                                    popWindow3.dismiss();
                                }
                            });

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 0x004:
                    //公用CityBean room_type
                    try {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        String msg = jsonObject.getString("msg");
                        if (code == 200) {
                            ToastManager.show("发布成功");
                            finish();

                        } else if (code == 100) {
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
}
