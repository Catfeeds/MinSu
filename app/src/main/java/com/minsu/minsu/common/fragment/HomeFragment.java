package com.minsu.minsu.common.fragment;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.lzy.okgo.model.Response;
import com.minsu.minsu.App;
import com.minsu.minsu.R;
import com.minsu.minsu.api.Constant;
import com.minsu.minsu.api.MinSuApi;
import com.minsu.minsu.api.callback.CallBack;
import com.minsu.minsu.base.BaseFragment;
import com.minsu.minsu.common.CitySelectActivity;
import com.minsu.minsu.common.FragmentBackHandler;
import com.minsu.minsu.common.RoomDetailActivity;
import com.minsu.minsu.common.adapter.BGABannerAdapter;
import com.minsu.minsu.common.adapter.HomeListAdapter;
import com.minsu.minsu.common.bean.HomeBean;
import com.minsu.minsu.search.SearchActivity;
import com.minsu.minsu.sign.CalendarSignActivity;
import com.minsu.minsu.utils.StorageUtil;
import com.minsu.minsu.utils.ToastManager;
import com.minsu.minsu.utils.UIThread;
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
import butterknife.Unbinder;
import cn.bingoogolapple.bgabanner.BGABanner;
import io.rong.imkit.RongIM;


/**
 * Created by hpc on 2017/12/1.
 */

public class HomeFragment extends BaseFragment implements View.OnClickListener, FragmentBackHandler
{


    @BindView(R.id.banner_pager)
    BGABanner bannerPager;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.search_tag)
    ImageView searchTag;
    @BindView(R.id.iv_sign)
    ImageView ivSign;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    Unbinder unbinder;
    @BindView(R.id.location_address)
    ImageView locationAddress;
    @BindView(R.id.smart_refresh)
    SmartRefreshLayout smartRefresh;
    private int page=1;
    private boolean isone = true;
    private View view;
    private String tokenId;
    private  String location_city;
    //定位
    private LocationClient mLocationClick = null;
    private int item_position;
    private List<HomeBean.Data1> data1;
    private List<HomeBean.Data1> data1List=new ArrayList<>();
    private HomeListAdapter homeListAdapter;
    private String networks;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container)
    {
        mLocationClick = new LocationClient(App.getInstance());
        mLocationClick.registerLocationListener(new MyLocationListener());
        view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    protected void initListener()
    {
        recyclerView.setNestedScrollingEnabled(false);
        tokenId = StorageUtil.getTokenId(getActivity());
        StorageUtil.setKeyValue(getActivity(), "isquxiao", "");
        location_city = StorageUtil.getValue(getActivity(), "location_city");
       if (location_city!=null&&location_city.length()!=0&&!location_city.equals(""))
       {
           ToastManager.show(location_city);
       }
        // location_city = StorageUtil.getValue(getActivity(), "location_city");
        toolbarTitle.setOnClickListener(this);
        ivSign.setOnClickListener(this);
        locationAddress.setOnClickListener(this);
        List<String> permission = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            permission.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
        {
            permission.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            permission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
        {
            permission.add(Manifest.permission.RECORD_AUDIO);
        }
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            permission.add(Manifest.permission.CAMERA);
        }
        if (!permission.isEmpty())
        {
            String[] permissions = permission.toArray(new String[permission.size()]);//将集合转化成数组
            //@onRequestPermissionsResult会接受次函数传的数据
            ActivityCompat.requestPermissions(getActivity(), permissions, 1);
        } else
        {
            initLoaction();
        }
        String isone = StorageUtil.getValue(getActivity(), "isone");
        if (isone.equals("") || isone == null)
        {
            MinSuApi.homeShow(getActivity(), 0x001, tokenId, "北京市",page, callBack);
        } else
        {
            MinSuApi.homeShow(getActivity(), 0x001, tokenId, location_city,page, callBack);
        }
        smartRefresh.setOnRefreshListener(new OnRefreshListener()
        {
            @Override
            public void onRefresh(RefreshLayout refreshlayout)
            {
                MinSuApi.homeShow(getActivity(), 0x001, tokenId, location_city,1, callBack);
               if (StorageUtil.getValue(getActivity(),"networks")!=null&&
                       StorageUtil.getValue(getActivity(),"networks").equals("no"))
               {
                   smartRefresh.finishRefresh(3000,false);
               }
            }
        });
        smartRefresh.setOnLoadmoreListener(new OnLoadmoreListener()
        {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout)
            {
                MinSuApi.homeShow(getActivity(), 0x004, tokenId, location_city,page, callBack);
                if (StorageUtil.getValue(getActivity(),"networks")!=null&&
                        StorageUtil.getValue(getActivity(),"networks").equals("no"))
                {
                    smartRefresh.finishLoadmore(3000,false);
                }

            }
        });

    }


    public class MyLocationListener implements BDLocationListener
    {

        @Override
        public void onReceiveLocation(BDLocation location)
        {
            //获取城市
            try
            {
                Thread.sleep(500);
                location_city = location.getCity();
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            smartRefresh.setOnRefreshListener(new OnRefreshListener()
            {
                @Override
                public void onRefresh(RefreshLayout refreshlayout)
                {
                    MinSuApi.homeShow(getActivity(), 0x001, tokenId, location_city,1, callBack);
                    if (StorageUtil.getValue(getActivity(),"networks")!=null&&
                            StorageUtil.getValue(getActivity(),"networks").equals("no"))
                    {
                        smartRefresh.finishRefresh(3000,false);
                    }
                }
            });
            smartRefresh.setOnLoadmoreListener(new OnLoadmoreListener()
            {
                @Override
                public void onLoadmore(RefreshLayout refreshlayout)
                {
                    MinSuApi.homeShow(getActivity(), 0x004, tokenId, location_city,page, callBack);
                    if (StorageUtil.getValue(getActivity(),"networks")!=null&&
                            StorageUtil.getValue(getActivity(),"networks").equals("no"))
                    {
                        smartRefresh.finishLoadmore(3000,false);
                    }
                }
            });
            Log.i("MyLocationListener--", location_city);
            StorageUtil.setKeyValue(getActivity(),"nowcity",location_city);
            StorageUtil.setKeyValue(getActivity(), "location_city", location_city);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        switch (requestCode)
        {
            case 1:
                if (grantResults.length > 0)
                {
                    for (int result : grantResults)
                    {
                        if (result != PackageManager.PERMISSION_GRANTED)
                        {
                            Toast.makeText(getActivity(), "" +
                                    "必须统一授权才能使用本程序", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    initLoaction();
                } else
                {
                    Toast.makeText(getActivity(), "" +
                            "发生未知错误", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    private CallBack callBack = new CallBack()
    {
        @Override
        public void onSuccess(int what, Response<String> result)
        {
            switch (what)
            {
                case 0x001:
                    try
                    {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        if (smartRefresh.isRefreshing())
                        {
                            smartRefresh.finishRefresh();
                        }
                        if (code == 200)
                        {
                            page=1;
                            page=page+1;
                            if (data1List!=null)
                            {
                                data1List.clear();
                            }
                            HomeBean homeBean = new Gson().fromJson(result.body(), HomeBean.class);
                            StorageUtil.setKeyValue(getActivity(), "isone", "is");
                            data1 = homeBean.data1;
                            data1List.addAll(data1);
                            List<HomeBean.Data2> data2 = homeBean.data2;
                            getBannerData(data2);
                            showHomeList(data1List);
                        }
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    break;
                case 0x002:
                    try
                    {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        if (code == 200)
                        {
                            ToastManager.show("取消成功");
                        }
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    break;
                case 0x003:
                    try
                    {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        if (code == 200)
                        {
                            ToastManager.show("收藏成功");
                        }
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    break;
                case 0x004:
                    JSONObject jsonObject = null;
                    try
                    {
                        jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        if (smartRefresh!=null)
                        {
                            smartRefresh.finishLoadmore();
                        }
                        if (code == 200)
                        {
                            page=page+1;
                            HomeBean homeBean = new Gson().fromJson(result.body(), HomeBean.class);
                            data1 = homeBean.data1;
                            data1List.addAll(data1);
                            List<HomeBean.Data2> data2 = homeBean.data2;
                            getBannerData(data2);
                            showHomeList(data1List);
                        }
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                    break;
            }

        }

        @Override
        public void onFail(int what, Response<String> result)
        {

        }

        @Override
        public void onFinish(int what)
        {

        }
    };

    @Override
    public void onHiddenChanged(boolean hidden)
    {
        super.onHiddenChanged(hidden);
        location_city = StorageUtil.getValue(getActivity(), "location_city");
        MinSuApi.homeShow(getActivity(), 0x001, tokenId, location_city,1, callBack);
        String is = StorageUtil.getValue(getActivity(), "isquxiao");
        if (is.equals("is"))
        {


        }
        if (is.equals("no"))
        {

        }
    }

    private void showHomeList(List<HomeBean.Data1> data1)
    {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        homeListAdapter = new HomeListAdapter(R.layout.item_home_list, data1);
        homeListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener()
        {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position)
            {
                switch (view.getId())
                {
                    case R.id.focus_room:
                        if (homeListAdapter.getItem(position).collect == 1)
                        {
                            homeListAdapter.getItem(position).collect=0;
                            homeListAdapter.notifyDataSetChanged();
                            MinSuApi.cancelCollect(0x002, tokenId, homeListAdapter.getItem(position).house_id, callBack);
                        } else if (homeListAdapter.getItem(position).collect == 0)
                        {
                            homeListAdapter.getItem(position).collect=1;
                            homeListAdapter.notifyDataSetChanged();
                            MinSuApi.addCollect(0x003, tokenId, homeListAdapter.getItem(position).house_id, callBack);
                        }
                        break;
                }
            }
        });
        homeListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position)
            {
                item_position = position;
                Intent intent = new Intent(getActivity(), RoomDetailActivity.class);
                intent.putExtra("house_id", homeListAdapter.getItem(position).house_id);
                startActivityForResult(intent, 3);
            }
        });
        if (data1.size() == 0)
        {
            homeListAdapter.setEmptyView(R.layout.empty, recyclerView);
        }
        recyclerView.setAdapter(homeListAdapter);
    }


    @Override
    protected void initData()
    {
        // MinSuApi.homeShow(getActivity(), 0x001, tokenId, location_city, callBack);
    }

    private void getBannerData(List<HomeBean.Data2> data)
    {
        bannerPager = view.findViewById(R.id.banner_pager);
        BGABannerAdapter adapter=new BGABannerAdapter(getActivity());
        bannerPager.setAdapter(adapter);
        adapter.setLongClick(new BGABannerAdapter.OnLongClick()
        {
            @Override
            public void itemLongClick(int position)
            {
                
            }
        });
        adapter.setItemClick(new BGABannerAdapter.ItemClick()
        {
            @Override
            public void onItemClick(View view)
            {

            }
        });
//        ArrayList<Integer> bannerImageData = DataProvider.getBannerImage();
        List<String> bannerImage = new ArrayList<>();
        for (int i = 0; i < data.size(); i++)
        {
            bannerImage.add(Constant.BASE2_URL + data.get(i).ad_code);
        }
        bannerPager.setData(bannerImage, null);
    }


    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == 0 && data != null)
        {
            //获取Bundle中的数据
            Bundle bundle = data.getExtras();
             location_city = bundle.getString("address");
            StorageUtil.setKeyValue(getActivity(), "location_city", location_city);
            //修改编辑框的内容
            ToastManager.show(location_city);
            page=1;
            MinSuApi.homeShow(getActivity(), 0x001, tokenId, location_city,page, callBack);
            smartRefresh.setOnRefreshListener(new OnRefreshListener()
            {
                @Override
                public void onRefresh(RefreshLayout refreshlayout)
                {
                    MinSuApi.homeShow(getActivity(), 0x001, tokenId, location_city,1, callBack);
                    if (StorageUtil.getValue(getActivity(),"networks")!=null&&
                            StorageUtil.getValue(getActivity(),"networks").equals("no"))
                    {
                        smartRefresh.finishRefresh(3000,false);
                    }
                }
            });
            smartRefresh.setOnLoadmoreListener(new OnLoadmoreListener()
            {
                @Override
                public void onLoadmore(RefreshLayout refreshlayout)
                {
                    MinSuApi.homeShow(getActivity(), 0x004, tokenId, location_city,page, callBack);
                    if (StorageUtil.getValue(getActivity(),"networks")!=null&&
                            StorageUtil.getValue(getActivity(),"networks").equals("no"))
                    {
                        smartRefresh.finishLoadmore(3000,false);
                    }
                }
            });
        }
       if (requestCode==3)
       {
           try
           {
               String isquxiao=StorageUtil.getValue(getActivity(),"isquxiao");
               if (isquxiao.equals("is"))
               {
                   homeListAdapter.getItem(item_position).collect=0;
                   homeListAdapter.notifyDataSetChanged();
               }else if (isquxiao.equals("no"))
               {
                   homeListAdapter.getItem(item_position).collect=1;
                   homeListAdapter.notifyDataSetChanged();
               }
               StorageUtil.setKeyValue(getActivity(),"isquxiao","");
           }catch (Exception e)
           {

           }
       }


    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.toolbar_title:
                startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
            case R.id.iv_sign:
                startActivity(new Intent(getActivity(), CalendarSignActivity.class));
                break;
            case R.id.location_address:
                String nowcity=StorageUtil.getValue(getActivity(),"nowcity");
                Intent intent = new Intent(getActivity(), CitySelectActivity.class);
                intent.putExtra("location_city", nowcity);
                startActivityForResult(intent, 0);
                break;
        }
    }

//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        super.onHiddenChanged(hidden);
//       location_city= StorageUtil.getValue(getActivity(),"location_city");
//        MinSuApi.homeShow(getActivity(), 0x001, tokenId, location_city, callBack);
//    }

    private boolean isDoubleClick = false;

    @Override
    public boolean onBackPressed()
    {
        if (isDoubleClick)
        {
            //RongIM.getInstance().disconnect();
            App.getInstance().exit(2);
        } else
        {
            ToastManager.show("再次点击一次退出程序");
            isDoubleClick = true;
            UIThread.getInstance().postDelay(new Runnable()
            {
                @Override
                public void run()
                {
                    isDoubleClick = false;
                }
            }, 1000);
        }
        return true;
    }


    //初始化定位信息并开始定位
    private void initLoaction()
    {
        //LocationClientOption必须初始化
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(2000 * 20);//设置实时更新当前数据，
        // 但是活动被销毁的时候要调用mLocationClick.stop
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//将定位模式指定
        //成传感器模式也就是说只能使用GPS进行定位
        option.setIsNeedAddress(true);//是否获取详细信息
        option.setIsNeedLocationPoiList(true);
        mLocationClick.setLocOption(option);
        mLocationClick.start();
    }

}
