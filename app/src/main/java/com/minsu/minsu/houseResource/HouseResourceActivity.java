package com.minsu.minsu.houseResource;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.lzy.okgo.model.Response;
import com.minsu.minsu.R;
import com.minsu.minsu.api.MinSuApi;
import com.minsu.minsu.api.callback.CallBack;
import com.minsu.minsu.base.BaseActivity;
import com.minsu.minsu.common.RoomDetailActivity;
import com.minsu.minsu.houseResource.adapter.HouseListAdapter;
import com.minsu.minsu.houseResource.bean.HouseListBean;
import com.minsu.minsu.utils.CheckUtil;
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

public class HouseResourceActivity extends BaseActivity implements View.OnClickListener
{

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
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private String tokenId;
    private List<HouseListBean.Data> data =new ArrayList<>();
    int page = 1;

    @Override
    protected void processLogic()
    {

        MinSuApi.myHouseResource(this, 0x001, tokenId, page, callBack);
    }

    @Override
    protected void setListener()
    {
        tokenId = StorageUtil.getTokenId(this);
        toolbarTitle.setText("我的房源");
        ivLeft.setVisibility(View.VISIBLE);
        ivRight.setVisibility(View.VISIBLE);
        ivRight.setImageResource(R.mipmap.add_01);
        ivLeft.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
        ivRight.setOnClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener()
        {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout)
            {
                MinSuApi.myHouseResource(HouseResourceActivity.this, 0x002, tokenId, page, callBack);
            }
        });
        refreshLayout.setOnRefreshListener(new OnRefreshListener()
        {
            @Override
            public void onRefresh(RefreshLayout refreshlayout)
            {
                MinSuApi.myHouseResource(HouseResourceActivity.this, 0x001, tokenId, 1, callBack);
            }
        });
    }

    @Override
    protected void loadViewLayout()
    {
        setContentView(R.layout.activity_house_resource);
    }

    @Override
    protected Context getActivityContext()
    {
        return this;
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
                        String msg = jsonObject.getString("msg");
                        int code = jsonObject.getInt("code");
                        if (refreshLayout!=null)
                        {
                            refreshLayout.finishLoadmore();
                            refreshLayout.finishRefresh();
                        }
                        if (refreshLayout!=null)
                        {
                            refreshLayout.finishRefresh();
                        }
                        if (code == 289)
                        {
                            ToastManager.show(msg);
                        } else if (code == 101)
                        {
                            ToastManager.show(msg);
                        } else if (code == 200)
                        {
                            page=page+1;
                            HouseListBean houseListBean = new Gson().fromJson(result.body(), HouseListBean.class);
                            if (data!=null)
                            {
                                data.clear();
                                data.addAll(houseListBean.data);
                            }else {
                                return;
                            }
                            final HouseListAdapter houseListAdapter = new HouseListAdapter(R.layout.item_home_list, data, "home");
                            recyclerView.setAdapter(houseListAdapter);
                            houseListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
                            {
                                @Override
                                public void onItemClick(BaseQuickAdapter adapter, View view, int position)
                                {
                                    Intent intent = new Intent(HouseResourceActivity.this, XiuGaiHouseActivity.class);
                                    intent.putExtra("type","xiugai");
                                    intent.putExtra("house_id", houseListAdapter.getItem(position).house_id + "");
                                    startActivityForResult(intent,1001);
                                }
                            });
                         houseListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener()
                         {
                             @Override
                             public void onItemChildClick(BaseQuickAdapter adapter, View view, int position)
                             {
                                 switch (view.getId())
                                 {
                                     case R.id.delete:
                                         LayoutInflater liEmail = LayoutInflater.from(HouseResourceActivity.this);
                                         View emailView = liEmail.inflate(R.layout.email_dialog, null);

                                         final AlertDialog.Builder alertDialogBuilderEmail = new AlertDialog.Builder(
                                                 HouseResourceActivity.this);

                                         // set prompts.xml to alertdialog builder
                                         alertDialogBuilderEmail.setView(emailView);
                                         // create alert dialog
                                         final AlertDialog alertDialogEmail = alertDialogBuilderEmail.create();
                                         final EditText EmailInputName = emailView.findViewById(R.id.input_name);
                                         final TextView EmailCancel = emailView.findViewById(R.id.cancel);
                                         final TextView EmailSure = emailView.findViewById(R.id.sure);

                                         EmailInputName.setText("您确定要删除吗");
                                         EmailInputName.setEnabled(false);
                                         EmailInputName.setFocusable(false);
                                         EmailInputName.setFocusableInTouchMode(false);
                                         EmailCancel.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View view) {
                                                 alertDialogEmail.dismiss();
                                             }
                                         });
                                         EmailSure.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View view) {
                                                 String email = EmailInputName.getText().toString();
                                                 boolean email1 = CheckUtil.isEmail(email);
                                                 if (email1) {
                                                    // MinSuApi.dele
                                                     alertDialogEmail.dismiss();
                                                 } else {

                                                 }


                                             }
                                         });
                                         alertDialogBuilderEmail
                                                 .setCancelable(true);
                                         // show it
                                         alertDialogEmail.show();
                                         break;
                                 }
                             }
                         });
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
                        String msg = jsonObject.getString("msg");
                        int code = jsonObject.getInt("code");
                        if (refreshLayout!=null)
                        {
                            refreshLayout.finishLoadmore();
                            refreshLayout.finishRefresh();
                        }
                        if (refreshLayout!=null)
                        {
                            refreshLayout.finishLoadmore();
                        }
                        if (code == 289)
                        {
                            ToastManager.show(msg);
                        } else if (code == 101)
                        {
                            ToastManager.show(msg);
                        } else if (code == 200)
                        {
                            page=page+1;
                            HouseListBean houseListBean = new Gson().fromJson(result.body(), HouseListBean.class);
                            if (data!=null)
                            {
                                data.addAll(houseListBean.data);
                            }else {
                                return;
                            }
                            final HouseListAdapter houseListAdapter = new HouseListAdapter(R.layout.item_home_list,data, "home");
                            recyclerView.setAdapter(houseListAdapter);
                            houseListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
                            {
                                @Override
                                public void onItemClick(BaseQuickAdapter adapter, View view, int position)
                                {
                                    Intent intent = new Intent(HouseResourceActivity.this, RoomDetailActivity.class);
                                    intent.putExtra("type","xiugai");
                                    intent.putExtra("house_id", houseListAdapter.getItem(position).house_id + "");
                                    startActivity(intent);
                                }
                            });
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
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.iv_right:
                String landlord = StorageUtil.getValue(HouseResourceActivity.this, "role");
                String isname = StorageUtil.getValue(HouseResourceActivity.this, "isname");
                if (landlord.equals("landlord"))
                {
                    startActivityForResult(new Intent(this, AddHouseActivity.class),1);
                }else {
                    ToastManager.show("认证后才能发布房源");
                    return;
                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1)
        {
            String fabu=StorageUtil.getValue(HouseResourceActivity.this,"fabu");
            StorageUtil.setKeyValue(HouseResourceActivity.this,"fabu","");
            if (fabu!=null&&fabu.equals("yes"))
            {
                MinSuApi.myHouseResource(this, 0x001, tokenId, page, callBack);
            }
        }
        if (requestCode==1001)
        {
            String xiugai=StorageUtil.getValue(HouseResourceActivity.this,"xiugai");
            if (xiugai!=null&&xiugai.equals("yes"))
            {
                MinSuApi.myHouseResource(HouseResourceActivity.this, 0x001, tokenId, 1, callBack);
                StorageUtil.setKeyValue(HouseResourceActivity.this,"xiugai","no");
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
