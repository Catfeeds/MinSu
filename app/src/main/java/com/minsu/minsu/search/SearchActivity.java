package com.minsu.minsu.search;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.lzy.okgo.model.Response;
import com.minsu.minsu.R;
import com.minsu.minsu.api.MinSuApi;
import com.minsu.minsu.api.callback.CallBack;
import com.minsu.minsu.base.BaseActivity;
import com.minsu.minsu.common.CitySelectActivity;
import com.minsu.minsu.common.RoomDetailActivity;
import com.minsu.minsu.search.adapter.AreaAdapter;
import com.minsu.minsu.search.adapter.PriceAdapter;
import com.minsu.minsu.search.adapter.RoomTypeAdapter;
import com.minsu.minsu.search.adapter.SearchAdapter;
import com.minsu.minsu.utils.StorageUtil;
import com.minsu.minsu.utils.ToastManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends BaseActivity implements View.OnClickListener {


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
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.toolbar_title)
    EditText toolbarTitle;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.location_address)
    ImageView locationAddress;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private String tokenId;
    private String location_city;
    private SearchBean searchBean;
    private int quyuId;
    private int houseTypeId;
    private SearchAdapter searchAdapter;
    private int localPosition;
    private int page=1;
    private List<SearchBean.Data1> data=new ArrayList<>();
    private String price_desc="";
    private String qy_id="";
    private String price_asc="";
    private String hose_type_id="";
    private String title="";

    @Override
    protected void processLogic() {
        MinSuApi.roomList(this, 0x001, tokenId, location_city, "", "", "", "", "", page,callBack);
    }

    @Override
    protected void setListener() {
        tokenId = StorageUtil.getTokenId(this);
        location_city = StorageUtil.getValue(this, "location_city");

        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
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
        toolbarTitle.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //如果actionId是搜索的id，则进行下一步的操作
                     title = toolbarTitle.getText().toString();
                     if (title!=null&&title.length()!=0)
                     {
                         MinSuApi.roomList(SearchActivity.this, 0x001, tokenId, location_city, "", "", "", "", title,1, callBack);
                     }else {
                         ToastManager.show("请输入搜索内容");
                     }

                   // MinSuApi.homeShow(SearchActivity.this, 0x001, tokenId, location_city, callBack);
                }
                return false;
            }
        });
        refreshLayout.setOnRefreshListener(new OnRefreshListener()
        {
            @Override
            public void onRefresh(RefreshLayout refreshlayout)
            {
                title = toolbarTitle.getText().toString();
                if (title.length()==0)
                {
                    title="";
                }
                MinSuApi.roomList(SearchActivity.this, 0x001, tokenId, location_city, qy_id, price_desc, price_asc, hose_type_id, title,1, callBack);
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener()
        {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout)
            {
                title = toolbarTitle.getText().toString();
                if (title.length()==0)
                {
                    title="";
                }
                MinSuApi.roomList(SearchActivity.this, 0x004, tokenId, location_city, qy_id, price_desc, price_asc, hose_type_id, title,page, callBack);
            }
        });
        locationAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchActivity.this, CitySelectActivity.class);
                intent.putExtra("location_city", location_city);
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == 0 && data != null) {
            //获取Bundle中的数据
            Bundle bundle = data.getExtras();
            location_city = bundle.getString("address");
            //修改编辑框的内容
            page = 1;
            ToastManager.show(location_city);
            MinSuApi.roomList(SearchActivity.this, 0x001, tokenId, location_city, "", "", "", "", "",1, callBack);
           // MinSuApi.homeShow(SearchActivity.this, 0x001, tokenId, location_city, callBack);
        }
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
                        if (refreshLayout!=null)
                        {
                            refreshLayout.finishRefresh();
                        }
                        if (code == 200) {
                            page=1;
                            page=page+1;
                            searchBean = new Gson().fromJson(result.body(), SearchBean.class);
                            if (data!=null)
                            {
                                data.clear();
                                data.addAll(searchBean.data1);
                            }
                            searchAdapter = new SearchAdapter(R.layout.item_home_list, data);
                            recyclerView.setAdapter(searchAdapter);
                            searchAdapter.setEmptyView(R.layout.empty, recyclerView);
                            searchAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                    Intent intent = new Intent(SearchActivity.this, RoomDetailActivity.class);
                                    intent.putExtra("house_id", searchAdapter.getItem(position).house_id);
                                    startActivity(intent);
                                }
                            });
                            searchAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                                @Override
                                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                                    switch (view.getId()) {

                                        case R.id.focus_room:
                                            localPosition = position;
                                            if (searchAdapter.getItem(position).collect == 1) {
//                                                ImageView focus = view.findViewById(R.id.focus_room);
//                                                focus.setImageResource(R.mipmap.collect);
                                                MinSuApi.cancelCollect(0x002, tokenId, searchAdapter.getItem(position).house_id, callBack);
                                            } else if (searchAdapter.getItem(position).collect == 0) {
//                                                ImageView focus = view.findViewById(R.id.focus_room);
//                                                focus.setImageResource(R.mipmap.collected);
//                                                searchAdapter.notifyItemChanged(position,focus);
                                                MinSuApi.addCollect(0x003, tokenId, searchAdapter.getItem(position).house_id, callBack);
                                            }
                                            break;
                                    }
                                }
                            });
                        } else if (code == 111) {
                            ToastManager.show(msg);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 0x002:
                    try {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        if (code == 200) {
                            ToastManager.show("取消成功");
                            searchBean.data1.remove(localPosition);
                            searchAdapter.getItem(localPosition).collect = 0;
                            searchAdapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 0x003:
                    try {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        if (code == 200) {
                            ToastManager.show("收藏成功");
                            searchAdapter.getItem(localPosition).collect = 1;
                            searchAdapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 0x004:
                    try
                    {
                    JSONObject jsonObject = new JSONObject(result.body());
                    int code = jsonObject.getInt("code");
                    String msg = jsonObject.getString("msg");
                    if (refreshLayout!=null)
                    {
                        refreshLayout.finishLoadmore();
                    }
                    if (code == 200) {
                        page=page+1;
                        searchBean = new Gson().fromJson(result.body(), SearchBean.class);
                        if (data!=null)
                        {
                            data.addAll(searchBean.data1);
                        }
                        searchAdapter.notifyDataSetChanged();
//                        searchAdapter = new SearchAdapter(R.layout.item_home_list, data);
//                        recyclerView.setAdapter(searchAdapter);
//                        searchAdapter.setEmptyView(R.layout.empty, recyclerView);
//                        searchAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                                Intent intent = new Intent(SearchActivity.this, RoomDetailActivity.class);
//                                intent.putExtra("house_id", searchAdapter.getItem(position).house_id);
//                                startActivity(intent);
//                            }
//                        });
//                        searchAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
//                            @Override
//                            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//                                switch (view.getId()) {
//
//                                    case R.id.focus_room:
//                                        localPosition = position;
//                                        if (searchAdapter.getItem(position).collect == 1) {
////                                                ImageView focus = view.findViewById(R.id.focus_room);
////                                                focus.setImageResource(R.mipmap.collect);
//                                            MinSuApi.cancelCollect(0x002, tokenId, searchAdapter.getItem(position).house_id, callBack);
//                                        } else if (searchAdapter.getItem(position).collect == 0) {
////                                                ImageView focus = view.findViewById(R.id.focus_room);
////                                                focus.setImageResource(R.mipmap.collected);
////                                                searchAdapter.notifyItemChanged(position,focus);
//                                            MinSuApi.addCollect(0x003, tokenId, searchAdapter.getItem(position).house_id, callBack);
//                                        }
//                                        break;
//                                }
//                            }
//                        });
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
                MinSuApi.roomList(this, 0x001, tokenId, location_city, "", "", "", "", "",1, callBack);
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
                            price_desc = "1";
                            qy_id = "";
                            price_asc = "";
                            hose_type_id = "";
                            title = toolbarTitle.getText().toString();
                            if (title.length()==0)
                            {
                                title="";
                            }
                            MinSuApi.roomList(SearchActivity.this, 0x001, tokenId, location_city, qy_id, price_desc, price_asc, hose_type_id, title,1, callBack);
                        } else if (tvPaixu.getText().toString().equals("价格从低到高")) {
                            price_desc = "";
                            qy_id = "";
                            price_asc = "1";
                            hose_type_id = "";
                            title = toolbarTitle.getText().toString();
                            if (title.length()==0)
                            {
                                title="";
                            }
                            MinSuApi.roomList(SearchActivity.this, 0x001, tokenId, location_city, qy_id, price_desc, price_asc, hose_type_id, title,1, callBack);
                        }

                    } else {
                        if (tvPaixu.getText().toString().equals("价格从高到低")) {
                            price_desc = "1";
                            qy_id = String.valueOf(quyuId);
                            price_asc = "";
                            hose_type_id = "";
                            title = toolbarTitle.getText().toString();
                            if (title.length()==0)
                            {
                                title="";
                            }
                            MinSuApi.roomList(SearchActivity.this, 0x001, tokenId, location_city, qy_id, price_desc, price_asc, hose_type_id, title,1, callBack);
                        } else if (tvPaixu.getText().toString().equals("价格从低到高")) {
                            price_desc = "";
                            qy_id = String.valueOf(quyuId);
                            price_asc = "1";
                            hose_type_id = "";
                            title = toolbarTitle.getText().toString();
                            if (title.length()==0)
                            {
                                title="";
                            }
                            MinSuApi.roomList(SearchActivity.this, 0x001, tokenId, location_city, qy_id, price_desc, price_asc, hose_type_id, title,1, callBack);
                        }
                    }
                } else {
                    if (tvArea.getText().toString().equals("区域")) {
                        if (tvPaixu.getText().toString().equals("价格从高到低")) {
                            price_desc = "1";
                            qy_id ="";
                            price_asc = "";
                            hose_type_id = String.valueOf(houseTypeId);
                            title = toolbarTitle.getText().toString();
                            if (title.length()==0)
                            {
                                title="";
                            }
                            MinSuApi.roomList(SearchActivity.this, 0x001, tokenId, location_city, qy_id, price_desc, price_asc, hose_type_id, title,1, callBack);
                        } else if (tvPaixu.getText().toString().equals("价格从低到高")) {
                            price_desc = "";
                            qy_id ="";
                            price_asc = "1";
                            hose_type_id = String.valueOf(houseTypeId);
                            title = toolbarTitle.getText().toString();
                            if (title.length()==0)
                            {
                                title="";
                            }
                            MinSuApi.roomList(SearchActivity.this, 0x001, tokenId, location_city, qy_id, price_desc, price_asc, hose_type_id, title,1, callBack);
                        } else if (tvPaixu.getText().toString().equals("排序")) {
                            price_desc = "";
                            qy_id ="";
                            price_asc = "";
                            hose_type_id = String.valueOf(houseTypeId);
                            title = toolbarTitle.getText().toString();
                            if (title.length()==0)
                            {
                                title="";
                            }
                            MinSuApi.roomList(SearchActivity.this, 0x001, tokenId, location_city, qy_id, price_desc, price_asc, hose_type_id, title,1, callBack);
                        }

                    } else {
                        if (tvPaixu.getText().toString().equals("价格从高到低")) {
                            price_desc = "1";
                            qy_id =String.valueOf(quyuId);
                            price_asc = "";
                            hose_type_id = String.valueOf(houseTypeId);
                            title = toolbarTitle.getText().toString();
                            if (title.length()==0)
                            {
                                title="";
                            }
                            MinSuApi.roomList(SearchActivity.this, 0x001, tokenId, location_city, qy_id, price_desc, price_asc, hose_type_id, title,1, callBack);
                        } else if (tvPaixu.getText().toString().equals("价格从低到高")) {
                            price_desc = "";
                            qy_id =String.valueOf(quyuId);
                            price_asc = "1";
                            hose_type_id = String.valueOf(houseTypeId);
                            title = toolbarTitle.getText().toString();
                            if (title.length()==0)
                            {
                                title="";
                            }
                            MinSuApi.roomList(SearchActivity.this, 0x001, tokenId, location_city, qy_id, price_desc, price_asc, hose_type_id, title,1, callBack);
                        } else if (tvPaixu.getText().toString().equals("排序")) {
                            price_desc = "";
                            qy_id =String.valueOf(quyuId);
                            price_asc = "";
                            hose_type_id = String.valueOf(houseTypeId);
                            title = toolbarTitle.getText().toString();
                            if (title.length()==0)
                            {
                                title="";
                            }
                            MinSuApi.roomList(SearchActivity.this, 0x001, tokenId, location_city, qy_id, price_desc, price_asc, hose_type_id, title,1, callBack);
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
   //排序
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
                            price_desc = "1";
                            qy_id ="";
                            price_asc = "";
                            hose_type_id = "";
                            title = toolbarTitle.getText().toString();
                            if (title.length()==0)
                            {
                                title="";
                            }
                            MinSuApi.roomList(SearchActivity.this, 0x001, tokenId, location_city, qy_id, price_desc, price_asc, hose_type_id, title,1, callBack);
                        } else {
                            price_desc = "1";
                            qy_id ="";
                            price_asc = "";
                            hose_type_id = String.valueOf(houseTypeId);
                            title = toolbarTitle.getText().toString();
                            if (title.length()==0)
                            {
                                title="";
                            }
                            MinSuApi.roomList(SearchActivity.this, 0x001, tokenId, location_city, qy_id, price_desc, price_asc, hose_type_id, title,1, callBack);
                        }
                    } else {
                        if (tvShaixuan.getText().toString().equals("筛选")) {
                            price_desc = "1";
                            qy_id =String.valueOf(quyuId);
                            price_asc = "";
                            hose_type_id = "";
                            title = toolbarTitle.getText().toString();
                            if (title.length()==0)
                            {
                                title="";
                            }
                            MinSuApi.roomList(SearchActivity.this, 0x001, tokenId, location_city, qy_id, price_desc, price_asc, hose_type_id, title,1, callBack);
                        } else {
                            price_desc = "1";
                            qy_id =String.valueOf(quyuId);
                            price_asc = "";
                            hose_type_id = String.valueOf(houseTypeId);
                            title = toolbarTitle.getText().toString();
                            if (title.length()==0)
                            {
                                title="";
                            }
                            MinSuApi.roomList(SearchActivity.this, 0x001, tokenId, location_city, qy_id, price_desc, price_asc, hose_type_id, title,1, callBack);
                        }
                    }
                } else if (priceContent.equals("价格从低到高")) {
                    if (quyuId == 0) {
                        if (houseTypeId == 0) {
                            price_desc = "";
                            qy_id ="";
                            price_asc = "1";
                            hose_type_id ="1";
                            title = toolbarTitle.getText().toString();
                            if (title.length()==0)
                            {
                                title="";
                            }
                            MinSuApi.roomList(SearchActivity.this, 0x001, tokenId, location_city, qy_id, price_desc, price_asc, hose_type_id, title,1, callBack);
                        } else {
                            price_desc = "";
                            qy_id ="";
                            price_asc = "1";
                            hose_type_id =String.valueOf(houseTypeId);
                            title = toolbarTitle.getText().toString();
                            if (title.length()==0)
                            {
                                title="";
                            }
                            MinSuApi.roomList(SearchActivity.this, 0x001, tokenId, location_city, qy_id, price_desc, price_asc, hose_type_id, title,1, callBack);
                        }
                    } else {
                        if (houseTypeId == 0) {
                            price_desc = "";
                            qy_id ="";
                            price_asc = "1";
                            hose_type_id ="";
                            title = toolbarTitle.getText().toString();
                            if (title.length()==0)
                            {
                                title="";
                            }
                            MinSuApi.roomList(SearchActivity.this, 0x001, tokenId, location_city, qy_id, price_desc, price_asc, hose_type_id, title,1, callBack);
                        } else {
                            price_desc = "1";
                            qy_id ="";
                            price_asc = "";
                            hose_type_id =String.valueOf(houseTypeId);
                            title = toolbarTitle.getText().toString();
                            if (title.length()==0)
                            {
                                title="";
                            }
                            MinSuApi.roomList(SearchActivity.this, 0x001, tokenId, location_city, qy_id, price_desc, price_asc, hose_type_id, title,1, callBack);
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
                            price_desc = "";
                            qy_id =String.valueOf(quyuId);
                            price_asc = "";
                            hose_type_id ="";
                            title = toolbarTitle.getText().toString();
                            if (title.length()==0)
                            {
                                title="";
                            }
                            MinSuApi.roomList(SearchActivity.this, 0x001, tokenId, location_city, qy_id, price_desc, price_asc, hose_type_id, title,1, callBack);
                        } else {
                            price_desc = "";
                            qy_id =String.valueOf(quyuId);
                            price_asc = "";
                            hose_type_id =String.valueOf(houseTypeId);
                            title = toolbarTitle.getText().toString();
                            if (title.length()==0)
                            {
                                title="";
                            }
                            MinSuApi.roomList(SearchActivity.this, 0x001, tokenId, location_city, qy_id, price_desc, price_asc, hose_type_id, title,1, callBack);
                        }
                    } else {
                        if (tvShaixuan.getText().toString().equals("筛选")) {
                            price_desc = "";
                            qy_id =String.valueOf(quyuId);
                            price_asc = "";
                            hose_type_id ="";
                            title = toolbarTitle.getText().toString();
                            if (title.length()==0)
                            {
                                title="";
                            }
                            MinSuApi.roomList(SearchActivity.this, 0x001, tokenId, location_city, qy_id, price_desc, price_asc, hose_type_id, title,1, callBack);
                        } else {
                            price_desc = "";
                            qy_id =String.valueOf(quyuId);
                            price_asc = "";
                            hose_type_id =String.valueOf(houseTypeId);
                            title = toolbarTitle.getText().toString();
                            if (title.length()==0)
                            {
                                title="";
                            }
                            MinSuApi.roomList(SearchActivity.this, 0x001, tokenId, location_city, qy_id, price_desc, price_asc, hose_type_id, title,1, callBack);
                        }
                    }
                } else if (paixu.equals("价格从低到高")) {
                    if (tvArea.getText().toString().equals("区域")) {
                        if (tvShaixuan.getText().toString().equals("筛选")) {
                            price_desc = "";
                            qy_id =String.valueOf(quyuId);
                            price_asc = "1";
                            hose_type_id ="";
                            title = toolbarTitle.getText().toString();
                            if (title.length()==0)
                            {
                                title="";
                            }
                            MinSuApi.roomList(SearchActivity.this, 0x001, tokenId, location_city, qy_id, price_desc, price_asc, hose_type_id, title,1, callBack);
                        } else {
                            price_desc = "";
                            qy_id =String.valueOf(quyuId);
                            price_asc = "1";
                            hose_type_id =String.valueOf(houseTypeId);
                            title = toolbarTitle.getText().toString();
                            if (title.length()==0)
                            {
                                title="";
                            }
                            MinSuApi.roomList(SearchActivity.this, 0x001, tokenId, location_city, qy_id, price_desc, price_asc, hose_type_id, title,1, callBack);
                        }
                    } else {
                        if (tvShaixuan.getText().toString().equals("筛选")) {
                            price_desc = "";
                            qy_id =String.valueOf(quyuId);
                            price_asc = "1";
                            hose_type_id ="";
                            title = toolbarTitle.getText().toString();
                            if (title.length()==0)
                            {
                                title="";
                            }
                            MinSuApi.roomList(SearchActivity.this, 0x001, tokenId, location_city, qy_id, price_desc, price_asc, hose_type_id, title,1, callBack);
                        } else {
                            price_desc = "";
                            qy_id =String.valueOf(quyuId);
                            price_asc = "1";
                            hose_type_id = String.valueOf(houseTypeId);
                            title = toolbarTitle.getText().toString();
                            if (title.length()==0)
                            {
                                title="";
                            }
                            MinSuApi.roomList(SearchActivity.this, 0x001, tokenId, location_city, qy_id, price_desc, price_asc, hose_type_id, title,1, callBack);
                        }
                    }
                } else if (paixu.equals("价格从高到低")) {
                    if (tvArea.getText().toString().equals("区域")) {
                        if (tvShaixuan.getText().toString().equals("筛选")) {
                            price_desc = "1";
                            qy_id =String.valueOf(quyuId);
                            price_asc = "";
                            hose_type_id = tvShaixuan.getText().toString();
                            title = toolbarTitle.getText().toString();
                            if (title.length()==0)
                            {
                                title="";
                            }
                            MinSuApi.roomList(SearchActivity.this, 0x001, tokenId, location_city, qy_id, price_desc, price_asc, hose_type_id, title,1, callBack);
                        } else {
                            price_desc = "1";
                            qy_id =String.valueOf(quyuId);
                            price_asc = "";
                            hose_type_id =String.valueOf(houseTypeId);
                            title = toolbarTitle.getText().toString();
                            if (title.length()==0)
                            {
                                title="";
                            }
                            MinSuApi.roomList(SearchActivity.this, 0x001, tokenId, location_city, qy_id, price_desc, price_asc, hose_type_id, title,1, callBack);
                        }
                    } else {
                        if (tvShaixuan.getText().toString().equals("筛选")) {
                            price_desc = "1";
                            qy_id =String.valueOf(quyuId);
                            price_asc = "";
                            hose_type_id ="";
                            title = toolbarTitle.getText().toString();
                            if (title.length()==0)
                            {
                                title="";
                            }
                            MinSuApi.roomList(SearchActivity.this, 0x001, tokenId, location_city, qy_id, price_desc, price_asc, hose_type_id, title,1, callBack);
                        } else {
                            price_desc = "1";
                            qy_id =String.valueOf(quyuId);
                            price_asc = "";
                            hose_type_id =String.valueOf(houseTypeId);
                            title = toolbarTitle.getText().toString();
                            if (title.length()==0)
                            {
                                title="";
                            }
                            MinSuApi.roomList(SearchActivity.this, 0x001, tokenId, location_city, qy_id, price_desc, price_asc, hose_type_id, title,1, callBack);
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
