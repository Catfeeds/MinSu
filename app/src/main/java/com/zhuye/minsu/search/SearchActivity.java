package com.zhuye.minsu.search;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.zhuye.minsu.R;
import com.zhuye.minsu.api.MinSuApi;
import com.zhuye.minsu.api.callback.CallBack;
import com.zhuye.minsu.base.BaseActivity;
import com.zhuye.minsu.search.adapter.AreaAdapter;
import com.zhuye.minsu.search.adapter.PriceAdapter;
import com.zhuye.minsu.search.adapter.RoomTypeAdapter;
import com.zhuye.minsu.search.adapter.SearchAdapter;
import com.zhuye.minsu.utils.StorageUtil;
import com.zhuye.minsu.utils.ToastManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends BaseActivity implements View.OnClickListener {


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
    @BindView(R.id.tv_all)
    TextView tvAll;
    @BindView(R.id.ll_all)
    LinearLayout llAll;
    @BindView(R.id.tv_area)
    TextView tvArea;
    @BindView(R.id.ll_area)
    LinearLayout llArea;
    @BindView(R.id.tv_paixu)
    TextView tvPaixu;
    @BindView(R.id.ll_paixu)
    LinearLayout llPaixu;
    @BindView(R.id.tv_shaixuan)
    TextView tvShaixuan;
    @BindView(R.id.ll_shaixuan)
    LinearLayout llShaixuan;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private String tokenId;
    private String location_city;
    private SearchBean searchBean;
    private int quyuId;
    private int houseTypeId;


    @Override
    protected void processLogic() {
        MinSuApi.roomList(this, 0x001, tokenId, location_city, "", "", "", "", "", callBack);
    }

    @Override
    protected void setListener() {
        tokenId = StorageUtil.getTokenId(this);
        location_city = StorageUtil.getValue(this, "location_city");
        toolbarTitle.setText("搜索");
        ivLeft.setVisibility(View.VISIBLE);
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        llArea.setOnClickListener(this);
        llPaixu.setOnClickListener(this);
        llAll.setOnClickListener(this);
        llShaixuan.setOnClickListener(this);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_search);
    }

    @Override
    protected Context getActivityContext() {
        return this;
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

                            searchBean = new Gson().fromJson(result.body(), SearchBean.class);
                            SearchAdapter searchAdapter = new SearchAdapter(R.layout.item_home_list, searchBean.data1);
                            recyclerView.setAdapter(searchAdapter);
                            searchAdapter.setEmptyView(R.layout.empty, recyclerView);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_area:
                showPopView01();
                break;
            case R.id.ll_paixu:
                showPopView02();
                break;
            case R.id.ll_shaixuan:
                showPopView03();
                break;
            case R.id.ll_all:
                tvArea.setText("区域");
                tvPaixu.setText("排序");
                tvShaixuan.setText("筛选");
                MinSuApi.roomList(this, 0x001, tokenId, location_city, "", "", "", "", "", callBack);
                break;
        }
    }

    private void showPopView03() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_area, null);
        //处理popWindow 显示内容
        final PopupWindow popWindow = new PopupWindow(contentView,
                RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT,
                true);
        RecyclerView recyclerView = contentView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final RoomTypeAdapter roomTypeAdapter = new RoomTypeAdapter(R.layout.room_type, searchBean.data3);
        roomTypeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                tvShaixuan.setText(roomTypeAdapter.getItem(position).name);
                houseTypeId = roomTypeAdapter.getItem(position).id;
                String shaixuan = tvShaixuan.getText().toString();
                if (shaixuan.equals("筛选")) {
                    if (tvArea.getText().toString().equals("区域")) {
                        if (tvPaixu.getText().toString().equals("价格从高到低")) {
                            MinSuApi.roomList(SearchActivity.this, 0x001, tokenId, location_city, "", "1", "", "", "", callBack);
                        } else if (tvPaixu.getText().toString().equals("价格从低到高")) {
                            MinSuApi.roomList(SearchActivity.this, 0x001, tokenId, location_city, "", "", "1", "", "", callBack);
                        }

                    } else {
                        if (tvPaixu.getText().toString().equals("价格从高到低")) {
                            MinSuApi.roomList(SearchActivity.this, 0x001, tokenId, location_city, String.valueOf(quyuId), "1", "", "", "", callBack);
                        } else if (tvPaixu.getText().toString().equals("价格从低到高")) {
                            MinSuApi.roomList(SearchActivity.this, 0x001, tokenId, location_city, String.valueOf(quyuId), "", "1", "", "", callBack);
                        }
                    }
                } else {
                    if (tvArea.getText().toString().equals("区域")) {
                        if (tvPaixu.getText().toString().equals("价格从高到低")) {
                            MinSuApi.roomList(SearchActivity.this, 0x001, tokenId, location_city, "", "1", "", String.valueOf(houseTypeId), "", callBack);
                        } else if (tvPaixu.getText().toString().equals("价格从低到高")) {
                            MinSuApi.roomList(SearchActivity.this, 0x001, tokenId, location_city, "", "", "1", String.valueOf(houseTypeId), "", callBack);
                        } else if (tvPaixu.getText().toString().equals("排序")) {
                            MinSuApi.roomList(SearchActivity.this, 0x001, tokenId, location_city, "", "", "", String.valueOf(houseTypeId), "", callBack);
                        }

                    } else {
                        if (tvPaixu.getText().toString().equals("价格从高到低")) {
                            MinSuApi.roomList(SearchActivity.this, 0x001, tokenId, location_city, String.valueOf(quyuId), "1", "", String.valueOf(houseTypeId), "", callBack);
                        } else if (tvPaixu.getText().toString().equals("价格从低到高")) {
                            MinSuApi.roomList(SearchActivity.this, 0x001, tokenId, location_city, String.valueOf(quyuId), "", "1", String.valueOf(houseTypeId), "", callBack);
                        } else if (tvPaixu.getText().toString().equals("排序")) {
                            MinSuApi.roomList(SearchActivity.this, 0x001, tokenId, location_city, String.valueOf(quyuId), "", "", String.valueOf(houseTypeId), "", callBack);
                        }
                    }
                }
                popWindow.dismiss();
            }
        });
        recyclerView.setAdapter(roomTypeAdapter);
        //创建并显示popWindow
        popWindow.setTouchable(true);
        // 设置允许在外点击消失
        popWindow.setOutsideTouchable(true);
        // 设置背景，这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popWindow.setBackgroundDrawable(new BitmapDrawable());
        popWindow.showAsDropDown(llShaixuan);
    }

    private void showPopView02() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_area, null);
        //处理popWindow 显示内容
        final PopupWindow popWindow = new PopupWindow(contentView,
                RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT,
                true);
        RecyclerView recyclerView = contentView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<String> priceList = new ArrayList<>();
        priceList.add("价格从高到低");
        priceList.add("价格从低到高");
        final PriceAdapter areaAdapter = new PriceAdapter(R.layout.room_type, priceList);
        areaAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String priceContent = areaAdapter.getItem(position);
                tvPaixu.setText(priceContent);
                if (priceContent.equals("价格从高到低")) {
                    if (tvArea.getText().toString().equals("区域")) {
                        if (tvShaixuan.getText().toString().equals("筛选")) {
                            MinSuApi.roomList(SearchActivity.this, 0x001, tokenId, location_city, "", "1", "", "", "", callBack);
                        } else {
                            MinSuApi.roomList(SearchActivity.this, 0x001, tokenId, location_city, "", "1", "", String.valueOf(houseTypeId), "", callBack);
                        }
                    } else {
                        if (tvShaixuan.getText().toString().equals("筛选")) {
                            MinSuApi.roomList(SearchActivity.this, 0x001, tokenId, location_city, String.valueOf(quyuId), "1", "", "", "", callBack);
                        } else {
                            MinSuApi.roomList(SearchActivity.this, 0x001, tokenId, location_city, String.valueOf(quyuId), "1", "", String.valueOf(houseTypeId), "", callBack);
                        }
                    }
                } else if (priceContent.equals("价格从低到高")) {
                    if (quyuId == 0) {
                        if (houseTypeId == 0) {
                            MinSuApi.roomList(SearchActivity.this, 0x001, tokenId, location_city, "", "", "1", "", "", callBack);
                        } else {
                            MinSuApi.roomList(SearchActivity.this, 0x001, tokenId, location_city, "", "", "1", String.valueOf(houseTypeId), "", callBack);
                        }
                    } else {
                        if (houseTypeId == 0) {
                            MinSuApi.roomList(SearchActivity.this, 0x001, tokenId, location_city, String.valueOf(quyuId), "", "1", "", "", callBack);
                        } else {
                            MinSuApi.roomList(SearchActivity.this, 0x001, tokenId, location_city, String.valueOf(quyuId), "", "1", String.valueOf(houseTypeId), "", callBack);
                        }
                    }
                }

                popWindow.dismiss();
            }
        });
        recyclerView.setAdapter(areaAdapter);
        //创建并显示popWindow
        popWindow.setTouchable(true);
        // 设置允许在外点击消失
        popWindow.setOutsideTouchable(true);
        // 设置背景，这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popWindow.setBackgroundDrawable(new BitmapDrawable());
        popWindow.showAsDropDown(llArea);
    }

    private void showPopView01() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_area, null);
        //处理popWindow 显示内容
        final PopupWindow popWindow = new PopupWindow(contentView,
                RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT,
                true);
        RecyclerView recyclerView = contentView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final AreaAdapter areaAdapter = new AreaAdapter(R.layout.room_type, searchBean.data2);
        areaAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                tvArea.setText(areaAdapter.getItem(position).name);
                quyuId = areaAdapter.getItem(position).id;
                String paixu = tvPaixu.getText().toString();
                if (paixu.equals("排序")) {
                    if (tvArea.getText().toString().equals("区域")) {
                        if (tvShaixuan.getText().toString().equals("筛选")) {
                            MinSuApi.roomList(SearchActivity.this, 0x001, tokenId, location_city, "", "", "", "", "", callBack);
                        } else {
                            MinSuApi.roomList(SearchActivity.this, 0x001, tokenId, location_city, "", "", "", String.valueOf(houseTypeId), "", callBack);
                        }
                    } else {
                        if (tvShaixuan.getText().toString().equals("筛选")) {
                            MinSuApi.roomList(SearchActivity.this, 0x001, tokenId, location_city, String.valueOf(quyuId), "", "", "", "", callBack);
                        } else {
                            MinSuApi.roomList(SearchActivity.this, 0x001, tokenId, location_city, String.valueOf(quyuId), "", "", String.valueOf(houseTypeId), "", callBack);
                        }
                    }
                } else if (paixu.equals("价格从低到高")) {
                    if (tvArea.getText().toString().equals("区域")) {
                        if (tvShaixuan.getText().toString().equals("筛选")) {
                            MinSuApi.roomList(SearchActivity.this, 0x001, tokenId, location_city, "", "", "1", "", "", callBack);
                        } else {
                            MinSuApi.roomList(SearchActivity.this, 0x001, tokenId, location_city, "", "", "1", String.valueOf(houseTypeId), "", callBack);
                        }
                    } else {
                        if (tvShaixuan.getText().toString().equals("筛选")) {
                            MinSuApi.roomList(SearchActivity.this, 0x001, tokenId, location_city, String.valueOf(quyuId), "", "1", "", "", callBack);
                        } else {
                            MinSuApi.roomList(SearchActivity.this, 0x001, tokenId, location_city, String.valueOf(quyuId), "", "1", String.valueOf(houseTypeId), "", callBack);
                        }
                    }
                } else if (paixu.equals("价格从高到低")) {
                    if (tvArea.getText().toString().equals("区域")) {
                        if (tvShaixuan.getText().toString().equals("筛选")) {
                            MinSuApi.roomList(SearchActivity.this, 0x001, tokenId, location_city, "", "1", "", "", "", callBack);
                        } else {
                            MinSuApi.roomList(SearchActivity.this, 0x001, tokenId, location_city, "", "1", "", String.valueOf(houseTypeId), "", callBack);
                        }
                    } else {
                        if (tvShaixuan.getText().toString().equals("筛选")) {
                            MinSuApi.roomList(SearchActivity.this, 0x001, tokenId, location_city, String.valueOf(quyuId), "1", "", "", "", callBack);
                        } else {
                            MinSuApi.roomList(SearchActivity.this, 0x001, tokenId, location_city, String.valueOf(quyuId), "1", "", String.valueOf(houseTypeId), "", callBack);
                        }
                    }
                }
                popWindow.dismiss();
            }
        });
        recyclerView.setAdapter(areaAdapter);
        //创建并显示popWindow
        popWindow.setTouchable(true);
        // 设置允许在外点击消失
        popWindow.setOutsideTouchable(true);
        // 设置背景，这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popWindow.setBackgroundDrawable(new BitmapDrawable());
        popWindow.showAsDropDown(llArea);
    }

}
